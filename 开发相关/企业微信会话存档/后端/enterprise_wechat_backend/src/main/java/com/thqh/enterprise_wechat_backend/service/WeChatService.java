package com.thqh.enterprise_wechat_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thqh.enterprise_wechat_backend.config.WeChatApiConfig;
import com.thqh.enterprise_wechat_backend.dto.*;
import com.thqh.enterprise_wechat_backend.entity.*;
import com.thqh.enterprise_wechat_backend.exception.BusinessException;
import com.thqh.enterprise_wechat_backend.exception.ErrorCode;
import com.thqh.enterprise_wechat_backend.mapper.CustomerMapper;
import com.thqh.enterprise_wechat_backend.mapper.GroupChatMapper;
import com.thqh.enterprise_wechat_backend.mapper.WechatUserMapper;
import com.thqh.enterprise_wechat_backend.util.WeChatSdkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.stream.Collectors;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: WeChatService
 * @Description:
 * @Author liubin
 * @Date 2025/3/4 15:19
 * @Version V1.0
 */

@Service
public class WeChatService {

    private final Logger logger = LoggerFactory.getLogger(WeChatService.class);

    private final ChatDataService chatDataService;

    private final ChatMessageService chatMessageService;

    private final ChatMediaService chatMediaService;

    private final RestTemplate restTemplate;

    private final WechatUserMapper wechatUserMapper;

    private final CustomerMapper customerMapper;

    private final GroupChatMapper groupChatMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    private final WeChatApiConfig weChatApiConfig;

    public WeChatService(ChatDataService chatDataService, ChatMessageService chatMessageService, ChatMediaService chatMediaService, RestTemplate restTemplate, WechatUserMapper wechatUserMapper, CustomerMapper customerMapper, GroupChatMapper groupChatMapper, WeChatApiConfig weChatApiConfig, RedisTemplate<String, Object> redisTemplate) {
        this.chatDataService = chatDataService;
        this.chatMessageService = chatMessageService;
        this.chatMediaService = chatMediaService;
        this.restTemplate = restTemplate;
        this.wechatUserMapper = wechatUserMapper;
        this.customerMapper = customerMapper;
        this.groupChatMapper = groupChatMapper;
        this.weChatApiConfig = weChatApiConfig;
        this.redisTemplate = redisTemplate;
    }


    // 获取会话存档数据
    public void getChatData(DownloadChatDataRequest downloadChatDataRequest) {
        long sdk = WeChatSdkUtil.initSdk(weChatApiConfig.getCorpid(), weChatApiConfig.getSecret().getSessionArchive());
        try {
            downloadRawChatData(downloadChatDataRequest, sdk);
            //将原始chatData数据存入业务表
            chatDataService.insertRawChatDataIntoChatData();
            //解密chatData数据
            chatDataService.decryptChatData(sdk);
            //将解密后的数据生成消息表和多媒体表
            chatDataService.generateChatMessage();
            //下载媒体文件、校验及上传minio
            chatMediaService.downloadMediaDataAndUpload(sdk);
        } catch (BusinessException e) {
            throw new BusinessException(e.getCode(),e.getMessage() );
        }catch (Exception e) {
            logger.error("getChatData Exception:{}",e.toString());
            throw new RuntimeException();
        }  finally {
            // 销毁 SDK
            WeChatSdkUtil.destroySdk(sdk);
        }
    }


    public long getLong() {
        long sdk = WeChatSdkUtil.initSdk(weChatApiConfig.getCorpid(), weChatApiConfig.getSecret().getSessionArchive());
        try {
            return sdk;
        } catch (BusinessException e) {
            throw new BusinessException(e.getCode(),e.getMessage() );
        }catch (Exception e) {
            logger.error("getChatData Exception:{}",e.toString());
            throw new RuntimeException();
        }  finally {
            // 销毁 SDK
            WeChatSdkUtil.destroySdk(sdk);
        }
    }

    @Transactional
    public void getWechatUser(){
        int errcode = -1;
        //(1)从redis获取通讯录access_toekn，如果不存在则通过通讯录密钥获取access_toekn
        String addressBookAccessToekn = (String) redisTemplate.opsForValue().get("wechat:address_book:access_token");
        if(addressBookAccessToekn == null || addressBookAccessToekn.isEmpty()){
            EnterpriseTokenResponse enterpriseTokenResponse = getAccessToken(weChatApiConfig.getCorpid(), weChatApiConfig.getSecret().getAddressBook());
            errcode = enterpriseTokenResponse.getErrcode();
            if(errcode != 0 ){
                return ;
            }else{
                addressBookAccessToekn = enterpriseTokenResponse.getAccess_token();
                int expiresIn = enterpriseTokenResponse.getExpires_in();
                redisTemplate.opsForValue().set("wechat:address_book:access_token",addressBookAccessToekn,expiresIn-600 ,TimeUnit.SECONDS);
            }
        }
        //(2)调用<获取成员ID列表>接口
        List<DeptUser> deptUserList=null;
        MemberIdListResponse memberIdList = getMemberIdList(addressBookAccessToekn, null, 1000);
        errcode = memberIdList.getErrcode();
        if(errcode!=0){
            return ;
        }else{
            deptUserList = memberIdList.getDept_user();
        }
        //(3)从redis获取自建应用token，如果不存在则通过自建应用密钥获取access_token
        String selfBuiltApplicationAccessToken = (String) redisTemplate.opsForValue().get("wechat:self_build_app:access_token");
        if(selfBuiltApplicationAccessToken == null || selfBuiltApplicationAccessToken.isEmpty()){
            EnterpriseTokenResponse enterpriseTokenResponse = getAccessToken(weChatApiConfig.getCorpid(), weChatApiConfig.getSecret().getSelfBuiltApplication());
            errcode = enterpriseTokenResponse.getErrcode();
            if(errcode != 0 ){
                return ;
            }else{
                selfBuiltApplicationAccessToken = enterpriseTokenResponse.getAccess_token();
                int expiresIn = enterpriseTokenResponse.getExpires_in();
                redisTemplate.opsForValue().set("wechat:self_build_app:access_token",selfBuiltApplicationAccessToken,expiresIn-600 ,TimeUnit.SECONDS);
            }
        }
        //(4)调用<读取成员>接口获取员工信息
        List<WechatUser> wechatUserList = new ArrayList<>();
        for(DeptUser deptUser : deptUserList){
            String userid = deptUser.getUserid();
            WeChatUserDetailResponse userDetail = getUserDetail(selfBuiltApplicationAccessToken, userid);
            int errcode1 = userDetail.getErrcode();
            if(errcode1 != 0){
                continue;
            }else{
                WechatUser wechatUser = new WechatUser();
                wechatUser.setUserid(userid);
                wechatUser.setName(userDetail.getName());
                wechatUser.setStatus(userDetail.getStatus());
                wechatUserList.add(wechatUser);
            }
        }
        //(5)从redis获取会话存档token，如果不存在则通过会话存档密钥获取access_token
        String sessionArchiveAccessToken = (String) redisTemplate.opsForValue().get("wechat:session-archive:access_token");
        if(sessionArchiveAccessToken == null || sessionArchiveAccessToken.isEmpty()){
            EnterpriseTokenResponse enterpriseTokenResponse = getAccessToken(weChatApiConfig.getCorpid(), weChatApiConfig.getSecret().getSessionArchive());
            errcode = enterpriseTokenResponse.getErrcode();
            if(errcode != 0 ){
                return ;
            }else{
                sessionArchiveAccessToken = enterpriseTokenResponse.getAccess_token();
                int expiresIn = enterpriseTokenResponse.getExpires_in();
                redisTemplate.opsForValue().set("wechat:session-archive:access_token",sessionArchiveAccessToken,expiresIn-600 ,TimeUnit.SECONDS);
            }
        }
        //(5)调用<获取会话内容存档开启成员列表>
        List<String> ids = null;
        PermitUserListResponse permitUserList = getPermitUserList(sessionArchiveAccessToken, null);
        errcode = permitUserList.getErrcode();
        if(errcode != 0){
            return ;
        }else{
            ids = permitUserList.getIds();
        }
        if(!ids.isEmpty()){
            for(WechatUser wechatUser:wechatUserList){
                if (ids.contains(wechatUser.getUserid())){
                    wechatUser.setSessionArchiveFlag(1);
                }
            }
        }
        //(6)先查询所有WechatUser，再与wechatUserList对比判断(新增、修改、离职处理)
        List<WechatUser> allWechatUsers = wechatUserMapper.getAllWechatUsers();
        Map<String, List<WechatUser>> stringListMap = processWechatUsers(allWechatUsers, wechatUserList);
        // 批量插入新增数据
        if (!stringListMap.get("insertList").isEmpty()) {
            wechatUserMapper.batchInsertWechatUsers(stringListMap.get("insertList"));
        }
        // 批量更新已有数据
        if (!stringListMap.get("updateList").isEmpty()) {
            wechatUserMapper.batchUpdateWechatUsers(stringListMap.get("updateList"));
        }
    }


    /**
     * 根据数据库中已有的微信用户列表和新同步获取的微信用户列表，
     * 将数据分为新增（数据库中不存在，但新数据中存在）、修改（数据库与新数据均存在）、
     * 以及标记离职（数据库中存在，但新数据中不存在）三部分，
     * 并以 Map 方式返回。
     *
     * @param dbUsers  数据库中所有的 WechatUser 列表
     * @param newUsers 当前同步获取的 WechatUser 列表
     * @return Map，其中 key "insertList" 对应新增列表，"updateList" 对应修改列表
     */
    public Map<String, List<WechatUser>> processWechatUsers(List<WechatUser> dbUsers, List<WechatUser> newUsers) {
        // 从数据库用户列表中提取所有 userid 集合
        Set<String> dbUserIds = dbUsers.stream()
                .map(WechatUser::getUserid)
                .collect(Collectors.toSet());
        // 从新数据中提取所有 userid 集合
        Set<String> newUserIds = newUsers.stream()
                .map(WechatUser::getUserid)
                .collect(Collectors.toSet());

        // 新增：新数据中存在，但数据库中不存在
        List<WechatUser> insertList = newUsers.stream()
                .filter(user -> !dbUserIds.contains(user.getUserid()))
                .collect(Collectors.toList());

        // 修改：新数据与数据库中均存在
        List<WechatUser> updateList = newUsers.stream()
                .filter(user -> dbUserIds.contains(user.getUserid()))
                .collect(Collectors.toList());

        // 标记离职：数据库中存在，但新数据中不存在
        List<WechatUser> deleteList = dbUsers.stream()
                .filter(user -> !newUserIds.contains(user.getUserid()))
                .collect(Collectors.toList());

        // 增加逻辑：查找 updateList 与 deleteList 均存在的记录
        Set<String> deleteUserIds = deleteList.stream()
                .map(WechatUser::getUserid)
                .collect(Collectors.toSet());

        updateList.forEach(user -> {
            if (deleteUserIds.contains(user.getUserid())) {
                // 将 updateList 中对应记录的 status 置为 -1
                user.setStatus(-1);
            }
        });

        // 将三个列表封装到 Map 中返回
        Map<String, List<WechatUser>> result = new HashMap<>();
        result.put("insertList", insertList);
        result.put("updateList", updateList);
        result.put("deleteList", deleteList);

        return result;
    }

    public void downloadRawChatData(DownloadChatDataRequest downloadChatDataRequest, long sdk) throws JsonProcessingException {
        //区分是手动还是自动调用，手动则取获取DownloadChatDataRequest的seq参数，否则从业务表查询
        String type = downloadChatDataRequest.getType();
        long seq;
        if("MANUAL_MODE".equals(type)){
           seq  = downloadChatDataRequest.getSeq();
        }else{
           seq = chatDataService.getMaxSeqFromChatData();
        }
        logger.info("拉取聊天记录的seq为：{}",seq);
        String getChatDataResult = WeChatSdkUtil.getChatData(sdk, seq, downloadChatDataRequest.getLimit(), downloadChatDataRequest.getProxy(), downloadChatDataRequest.getPassword(), downloadChatDataRequest.getTimeout());
        // 将结果转为对象
        ObjectMapper objectMapper = new ObjectMapper();
        DownloadChatDataResponse downloadChatDataResponse = objectMapper.readValue(getChatDataResult, DownloadChatDataResponse.class);
        int errcode = downloadChatDataResponse.getErrcode();
        //非0则失败
        if (errcode != 0) {
            logger.error("获取会话内容存档失败:{}", downloadChatDataResponse);
            throw new BusinessException(ErrorCode.GET_CHATDATA_ERROR, downloadChatDataResponse.toString());
        }
        List<RawChatData> rawChatDatas = downloadChatDataResponse.getRawChatData();
        //保存初始数据到数据库表
        int rawChatDatasSize = rawChatDatas.size();
        logger.info("chatData 数量:{}", rawChatDatasSize);
        if (rawChatDatasSize > 0) {
            chatDataService.saveRawChatDatas(rawChatDatas);
        }
        //继续拉取数据
        while(rawChatDatasSize==1000) {
            //获取最大的seq
            seq = getMaxSeq(rawChatDatas);
            getChatDataResult = WeChatSdkUtil.getChatData(sdk, seq, downloadChatDataRequest.getLimit(), downloadChatDataRequest.getProxy(), downloadChatDataRequest.getPassword(), downloadChatDataRequest.getTimeout());
            downloadChatDataResponse = objectMapper.readValue(getChatDataResult, DownloadChatDataResponse.class);
            errcode = downloadChatDataResponse.getErrcode();
            //非0则失败
            if (errcode != 0) {
                logger.error("获取会话内容存档失败:{}", downloadChatDataResponse);
                throw new BusinessException(ErrorCode.GET_CHATDATA_ERROR, downloadChatDataResponse.toString());
            }
            rawChatDatas = downloadChatDataResponse.getRawChatData();
            rawChatDatasSize = rawChatDatas.size();
            logger.info("chatData 数量:{}", rawChatDatasSize);
            if (rawChatDatasSize > 0) {
                chatDataService.saveRawChatDatas(rawChatDatas);
            }
            if( rawChatDatasSize < 1000) {
                //已拉取所有会话,退出循环
                rawChatDatasSize = 9999;
            }
        }
    }

    /**
     * 获取本次消息列表的最大seq值，用于获取连续的消息
     * @param rawChatDatas
     * @return
     */
    public long getMaxSeq(List<RawChatData> rawChatDatas){
        if (rawChatDatas.isEmpty()) {
            return 0;
        }
        // 初始化为第一个元素的 seq
        long max = rawChatDatas.get(0).getSeq();
        for (RawChatData rawChatData : rawChatDatas) {
            if (rawChatData.getSeq() > max) {
                max = rawChatData.getSeq();
            }
        }
        logger.info("max Seq:{}", max);
        return max;
    }



    /**
     * <获取access_token>接口
     * @param corpId 企业id
     * @param corpSecret 密钥，会根据不同应用而不同
     * @return
     */
    public EnterpriseTokenResponse getAccessToken(String corpId, String corpSecret) {
        String url = weChatApiConfig.getUrl().getToken() + "?corpid=" + corpId + "&corpsecret=" + corpSecret;
        ResponseEntity<EnterpriseTokenResponse> response = restTemplate.getForEntity(url, EnterpriseTokenResponse.class);
        logger.info("getAccessToken Response:{}",response);
        return response.getBody();
    }


    /**
     * <获取成员ID列表>接口：获取所有用户的userid和department
     * @param accessToken 通讯录access_toekn
     * @param cursor 分页参数
     * @param limit 1000
     * @return
     */
    public MemberIdListResponse getMemberIdList(String accessToken, String cursor, int limit) {
        String url = weChatApiConfig.getUrl().getUserList() + "?access_token=" + accessToken;
        // 构造请求 JSON 字符串
        String requestJson;
//        if(cursor!=null){
//            requestJson = "{\"cursor\":\"" + cursor + "\",\"limit\":" + limit + "}";
//        }else {
//            requestJson = "{\"limit\":" + limit + "}";
//        }
        Map<String, Object> requestBody = new HashMap<>();
        if (cursor != null) {
            requestBody.put("cursor", cursor);
        }
        requestBody.put("limit", limit);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<MemberIdListResponse> response = restTemplate.postForEntity(url, entity, MemberIdListResponse.class);
        logger.info("getMemberIdList Response:{}",response);
        return response.getBody();
    }


    /**
     * <读取成员>接口：获取员工详情信息（无手机号邮箱等需要授权数据）
     * @param accessToken 自建应用 token
     * @param userId 用户id
     * @return
     */
    public WeChatUserDetailResponse getUserDetail(String accessToken, String userId) {
        String url = weChatApiConfig.getUrl().getUserDetail() + "?access_token=" + accessToken + "&userid=" + userId;
        ResponseEntity<WeChatUserDetailResponse> response = restTemplate.getForEntity(url, WeChatUserDetailResponse.class);
        logger.info("getUserDetail Response:{}",response);
        return response.getBody();
    }


    /**
     * <获取会话内容存档开启成员列表>接口
     * @param accessToken 调用接口凭证
     * @param type        拉取对应版本的开启成员列表。1：办公版；2：服务版；3：企业版；为 null 时返回全量成员列表
     * @return 返回 PermitUserListResponse 对象
     */
    public PermitUserListResponse getPermitUserList(String accessToken, Integer type) {
        String url = weChatApiConfig.getUrl().getMsgauditUserList()+ "?access_token=" + accessToken;
        // 设置请求头为 JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 构造请求体
        Map<String, Object> requestBody = new HashMap<>();
        if (type != null) {
            requestBody.put("type", type);
        }
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<PermitUserListResponse> response = restTemplate.postForEntity(url, entity, PermitUserListResponse.class);
        logger.info("getPermitUserList Response:{}",response);
        return response.getBody();
    }


    @Transactional
    public void getCustomer(){
        //(1)获取所有在职的用户并进行分批
        List<WechatUser> wechatUsers = wechatUserMapper.getWechatUsersByStatus(1);
        if(wechatUsers.isEmpty()){
            return;
        }
        // 提取所有企业成员的userid
        List<String> allUserIds = wechatUsers.stream()
                .map(WechatUser::getUserid)
                .collect(Collectors.toList());
        // 分批，每批最多100个
        List<List<String>> batches = partitionList(allUserIds, 100);

        //(2)从redis获取自建应用token，如果不存在则通过自建应用密钥获取access_token
        String selfBuiltApplicationAccessToken = (String) redisTemplate.opsForValue().get("wechat:self_build_app:access_token");
        if(selfBuiltApplicationAccessToken == null || selfBuiltApplicationAccessToken.isEmpty()){
            EnterpriseTokenResponse enterpriseTokenResponse = getAccessToken(weChatApiConfig.getCorpid(), weChatApiConfig.getSecret().getSelfBuiltApplication());
            int errcode = enterpriseTokenResponse.getErrcode();
            if(errcode != 0 ){
                return ;
            }else{
                selfBuiltApplicationAccessToken = enterpriseTokenResponse.getAccess_token();
                int expiresIn = enterpriseTokenResponse.getExpires_in();
                redisTemplate.opsForValue().set("wechat:self_build_app:access_token",selfBuiltApplicationAccessToken,expiresIn-600 ,TimeUnit.SECONDS);
            }
        }

        // (3)遍历每个批次调用批量获取客户详情接口
        // 用于存放所有获取到的 Customer 对象
        List<Customer> rawCustomerList = new ArrayList<>();
        for (List<String> batchUserIds : batches) {
            // 初始 cursor 为空
            String cursor = "";
            boolean hasMore = true;
            while (hasMore) {
                CustomerDetailResponse response = batchGetCustomerDetail(selfBuiltApplicationAccessToken,batchUserIds, cursor,100);
                if (response != null && response.getErrcode() == 0) {
                    List<CustomerDetail> detailList = response.getExternal_contact_list();
                    if (detailList != null) {
                        for (CustomerDetail detail : detailList) {
                            CustomerExternalContact extContact = detail.getExternal_contact();
                            CustomerFollowInfo followInfo = detail.getFollow_info();
                            if (extContact != null) {
                                // 将接口返回数据映射为 Customer 对象
                                Customer customer = mapToCustomer(extContact, followInfo);
                                rawCustomerList.add(customer);
                            }
                        }
                    }
                    // 分页处理：如果返回 next_cursor 不为空，则继续获取下一页
                    if (response.getNext_cursor() != null && !response.getNext_cursor().isEmpty()) {
                        cursor = response.getNext_cursor();
                    } else {
                        hasMore = false;
                    }
                } else {
                    // 如果接口调用失败，可记录日志，结束当前批次处理
                    logger.error("批量获取客户详情失败:{} {} {}",batchUserIds,response.getErrcode(),response.getErrmsg());
                    hasMore = false;
                }
            }
        }
        if(!rawCustomerList.isEmpty()){
            // (4) 批量插入所有客户数据到临时表 raw_customer
            customerMapper.deleteTodayRawCustomers();
            // 分批，每批200条记录插入临时表
            List<List<Customer>> partitions = partitionList(rawCustomerList, 1000);
            for (List<Customer> batch : partitions) {
                customerMapper.batchInsertRawCustomers(batch);
            }
            // 5. 通过临时表 raw_customer 合并数据到正式表 customer（新增或更新）
            // 更新 customer 和 customer_follow 表的 status 为 1
            customerMapper.updateCustomerStatusToInactive();
            customerMapper.updateCustomerFollowStatusToInactive();
            //更新数据到业务表
            customerMapper.mergeRawToCustomer();
            customerMapper.mergeRawToCustomerFollow();
        }
    }



    /**
     * 将一个 List 分割为多个子列表，每个子列表大小不超过指定 size
     *
     * @param list 原始列表
     * @param size 每个子列表的最大大小
     * @param <T>  列表中元素的类型
     * @return 分割后的子列表集合
     */
    private <T> List<List<T>> partitionList(List<T> list, int size) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(new ArrayList<>(list.subList(i, Math.min(i + size, list.size()))));
        }
        return partitions;
    }

    /**
     * 将 ExternalContact 与 FollowInfo 映射为 Customer 对象
     *
     * @param extContact 外部联系人基本信息
     * @param followInfo 跟进信息
     * @return Customer 对象
     */
    private Customer mapToCustomer(CustomerExternalContact extContact, CustomerFollowInfo followInfo) {
        Customer customer = new Customer();
        customer.setExternalUserId(extContact.getExternal_userid());
        customer.setName(extContact.getName());
        customer.setPosition(extContact.getPosition());
        customer.setAvatar(extContact.getAvatar());
        customer.setCorpName(extContact.getCorp_name());
        customer.setCorpFullName(extContact.getCorp_full_name());
        customer.setType((byte) extContact.getType());
        customer.setGender((byte) extContact.getGender());
        customer.setUnionid(extContact.getUnionid());
        if (followInfo != null) {
            customer.setFollowUserid(followInfo.getUserid());
            customer.setFollowRemark(followInfo.getRemark());
            customer.setFollowDescription(followInfo.getDescription());
            customer.setFollowCreatetime(followInfo.getCreatetime());
            // 将 tag_id 列表转换为逗号分隔字符串
            if (followInfo.getTag_id() != null) {
                String tagIds = String.join(",", followInfo.getTag_id());
                customer.setFollowTagIds(tagIds);
            }
            // 将 tag_id 列表转换为逗号分隔字符串
            if (followInfo.getTag_id() != null) {
                String tagIds = String.join(",", followInfo.getTag_id());
                customer.setFollowTagIds(tagIds);
            }
            // 将 remark_mobiles 列表转换为逗号分隔字符串
            if (followInfo.getRemark_mobiles() != null) {
                String remarkMobiles = String.join(",", followInfo.getRemark_mobiles());
                customer.setFollowRemarkMobiles(remarkMobiles);
            }
            customer.setFollowOperUserid(followInfo.getOper_userid());
            customer.setFollowAddWay((byte) followInfo.getAdd_way());
        }
        return customer;
    }


    /**
     * 批量获取客户详情接口
     * @param accessToken 企业微信自建应用token
     * @param batchUserIds 批量的用户id，<=100
     * @param cursor 用于分页查询的游标，字符串类型，由上一次调用返回，首次调用可不填
     * @param limit 返回的最大记录数，整型，最大值100，默认值50，超过最大值时取最大值
     * @return
     */
    private CustomerDetailResponse batchGetCustomerDetail(String accessToken,List<String> batchUserIds, String cursor,int limit) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userid_list", batchUserIds);
        requestBody.put("cursor", cursor);
        requestBody.put("limit", limit);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        String url = weChatApiConfig.getUrl().getCustomerDetail() + "?access_token=" + accessToken;
        ResponseEntity<CustomerDetailResponse> response = restTemplate.postForEntity(url, entity, CustomerDetailResponse.class);
        logger.info("batchGetCustomerDetail Response:{}",response);
        return response.getBody();
    }


    /**
     * <获取客户详情>接口
     * @param accessToken 调用接口凭证
     * @param externalUserid    外部联系人的userid
     * @param nextCursor      上次请求返回的next_cursor
     * @return 返回 PermitUserListResponse 对象
     */
//    public CustomerDetailResponse getCustomerDetail(String accessToken,String externalUserid,String nextCursor) {
//        String url = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get?access_token="
//                + accessToken + "&external_userid=" + externalUserid;
//        if(nextCursor != null){
//            url += "&cursor=" + nextCursor;
//        }
//        ResponseEntity<CustomerDetailResponse > response =
//                restTemplate.getForEntity(url, CustomerDetailResponse .class);
//        logger.info("getCustomerDetail Response:{}",response);
//        return response.getBody();
//    }

    /**
     * <获取客户列表>接口
     * @param accessToken 调用接口凭证
     * @param userId      企业微信userid
     * @return 返回 PermitUserListResponse 对象
     */
    public CustomerListResponse getCustomerList(String accessToken,String userId) {
        String urlList = weChatApiConfig.getUrl().getCustomerList() + "?access_token="
                + accessToken + "&userid=" + userId;
        ResponseEntity<CustomerListResponse> response =
                restTemplate.getForEntity(urlList, CustomerListResponse.class);
        logger.info("getCustomerList Response:{}",response);
        return response.getBody();
    }

    /**
     * 从企业微信拉取群聊及成员数据
     */
    @Transactional
    public void getGroupChat(){
        List<String> roomIds = chatMessageService.getDistinctRoomIds();
        if (roomIds == null || roomIds.isEmpty()){
            return;
        }
        int errcode = -1;
        //(1)从redis获取自建应用token，如果不存在则通过自建应用密钥获取access_token
        String selfBuiltApplicationAccessToken = (String) redisTemplate.opsForValue().get("wechat:self_build_app:access_token");
        if(selfBuiltApplicationAccessToken == null || selfBuiltApplicationAccessToken.isEmpty()){
            EnterpriseTokenResponse enterpriseTokenResponse = getAccessToken(weChatApiConfig.getCorpid(), weChatApiConfig.getSecret().getSelfBuiltApplication());
            errcode = enterpriseTokenResponse.getErrcode();
            if(errcode != 0 ){
                return ;
            }else{
                selfBuiltApplicationAccessToken = enterpriseTokenResponse.getAccess_token();
                int expiresIn = enterpriseTokenResponse.getExpires_in();
                redisTemplate.opsForValue().set("wechat:self_build_app:access_token",selfBuiltApplicationAccessToken,expiresIn-600 ,TimeUnit.SECONDS);
            }
        }
        //(2)从redis获取会话存档token，如果不存在则通过会话存档密钥获取access_token
        String sessionArchiveAccessToken = (String) redisTemplate.opsForValue().get("wechat:session-archive:access_token");
        if(sessionArchiveAccessToken == null || sessionArchiveAccessToken.isEmpty()){
            EnterpriseTokenResponse enterpriseTokenResponse = getAccessToken(weChatApiConfig.getCorpid(), weChatApiConfig.getSecret().getSessionArchive());
            errcode = enterpriseTokenResponse.getErrcode();
            if(errcode != 0 ){
                return ;
            }else{
                sessionArchiveAccessToken = enterpriseTokenResponse.getAccess_token();
                int expiresIn = enterpriseTokenResponse.getExpires_in();
                redisTemplate.opsForValue().set("wechat:session-archive:access_token",sessionArchiveAccessToken,expiresIn-600 ,TimeUnit.SECONDS);
            }
        }
        //(3)拉取群聊数据
        List<GroupChat> groupChatList = new ArrayList<>();
        List<GroupChat> errorGroupChatList = new ArrayList<>();
        List<GroupChatMember> groupChatMemberList = new ArrayList<>();
        for(String roomId : roomIds){
            if(roomId.isEmpty()){
                continue;
            }
            // 调用获取客户群详情接口
            GroupChatCustomerResponse groupChatCustomerInfo = getGroupChatCustomerInfo(selfBuiltApplicationAccessToken, roomId);
            errcode = groupChatCustomerInfo.getErrcode();
            if(errcode == 0 ){
                GroupChatCustomerResponse.GroupChatDetail groupChatDetail = groupChatCustomerInfo.getGroup_chat();
                //群聊主体
                GroupChat  groupChat=new GroupChat();
                groupChat.setChatId(roomId);
                groupChat.setChatType("customer");

                // 获取群聊名称，如果为空则使用"群聊"+roomId前14位
                String chatName = groupChatDetail.getName();
                if (chatName == null || chatName.trim().isEmpty()) {
                    // 确保roomId长度足够，避免StringIndexOutOfBoundsException
                    int length = Math.min(14, roomId.length());
                    chatName = "群聊" + roomId.substring(0, length);
                }
                groupChat.setName(chatName);

                groupChat.setOwner(groupChatDetail.getOwner());
                groupChat.setCreateTime(groupChatDetail.getCreate_time());
                groupChat.setNotice(groupChatDetail.getNotice());
                groupChat.setMemberVersion(groupChatDetail.getMember_version());
                groupChat.setAdminList( groupChatDetail.getAdmin_list().toString());
                groupChat.setStatus((byte) 0);
                groupChat.setErrorLog(null);
                groupChatList.add(groupChat);
                //群聊成员
                List<GroupChatCustomerResponse.Member> memberList = groupChatDetail.getMember_list();
                for(GroupChatCustomerResponse.Member member : memberList){
                    GroupChatMember groupChatMember=new GroupChatMember();
                    groupChatMember.setChatId(roomId);
                    groupChatMember.setUserid(member.getUserid());
                    groupChatMember.setType(member.getType());
                    groupChatMember.setUnionid(member.getUnionid());
                    groupChatMember.setJoinTime(member.getJoin_time());
                    groupChatMember.setJoinScene(member.getJoin_scene());
                    if(member.getInvitor() != null){
                        groupChatMember.setInvitorUserid(member.getInvitor().getUserid());
                    } else {
                        //处理无邀请人的情况（如设置默认值或记录日志）
                        groupChatMember.setInvitorUserid("");
                        logger.info("群聊{}的成员{}无邀请人",roomId,member.getUserid());
                    }
                    groupChatMember.setGroupNickname(member.getGroup_nickname());
                    groupChatMember.setMemberName(member.getName());
                    groupChatMember.setStatus((byte) 0);
                    groupChatMemberList.add(groupChatMember);
                }
            }else{
                //调用获取会话存档内部群信息接口
                GroupChatInternalResponse groupChatInternalInfo = getGroupChatInternalInfo(sessionArchiveAccessToken, roomId);
                errcode = groupChatInternalInfo.getErrcode();
                if(errcode == 0 ){
                    //群聊主体
                    GroupChat groupChat = new GroupChat();
                    groupChat.setChatId(roomId);
//                    groupChat.setName( groupChatInternalInfo.getRoomname());

                    // 获取群聊名称，如果为空则使用"群聊"+roomId前14位
                    String chatName = groupChatInternalInfo.getRoomname();
                    if (chatName == null || chatName.trim().isEmpty()) {
                        // 确保roomId长度足够，避免StringIndexOutOfBoundsException
                        int length = Math.min(14, roomId.length());
                        chatName = "群聊" + roomId.substring(0, length);
                    }
                    groupChat.setName(chatName);

                    groupChat.setOwner(groupChatInternalInfo.getCreator());
                    groupChat.setCreateTime(groupChatInternalInfo.getRoom_create_time());
                    groupChat.setNotice(groupChatInternalInfo.getNotice());
                    groupChat.setStatus((byte) 0);
                    groupChat.setErrorLog(null);
                    groupChatList.add(groupChat);
                    //群聊成员
                    List<GroupChatInternalResponse.InternalMember> members = groupChatInternalInfo.getMembers();
                    for (GroupChatInternalResponse.InternalMember member : members){
                        GroupChatMember groupChatMember=new GroupChatMember();
                        groupChatMember.setChatId(roomId);
                        groupChatMember.setUserid(member.getMemberid());
                        groupChatMember.setJoinTime(member.getJointime());
                        groupChatMember.setStatus((byte) 0);
                        groupChatMemberList.add(groupChatMember);
                    }
                }else{
                    //2种情况：1.即不是客户群也不是内部群，2.接口其它报错
                    GroupChat groupChat = new GroupChat();
                    groupChat.setChatId(roomId);
                    groupChat.setErrorLog(groupChatCustomerInfo.getErrmsg() + "/" + groupChatInternalInfo.getErrmsg());
                    errorGroupChatList.add(groupChat);
                }
            }
        }
        //(4)保存数据到数据库
        //处理群聊主表（先失效，再新增/更新）
        if (!groupChatList.isEmpty()) {
            groupChatMapper.markAllGroupChatsInactive();
            groupChatMapper.batchMergeGroupChats(groupChatList);
        }
        //处理群聊成员表（先失效，再新增/更新）
        if (!groupChatMemberList.isEmpty()) {
            groupChatMapper.markAllGroupChatMembersInactive();
            groupChatMapper.batchMergeGroupChatMembers(groupChatMemberList);
        }
        //处理拉取失败的群聊数据
        if (!errorGroupChatList.isEmpty()) {
            groupChatMapper.batchMergeErrorGroupChats(errorGroupChatList);
        }
    }

    /**
     * <获取客户群详情>接口
     * @param accessToken 调用接口凭证
     * @param chatId    客户群ID
     * @return 返回 GroupChatCustomerResponse 对象
     */
    public GroupChatCustomerResponse getGroupChatCustomerInfo(String accessToken,String chatId) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("chat_id", chatId);
        requestBody.put("need_name", 1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        String urlExternal = weChatApiConfig.getUrl().getCustomerGroupchatDetail() + "?access_token=" + accessToken;
        ResponseEntity<GroupChatCustomerResponse> response = restTemplate.postForEntity(urlExternal, entity, GroupChatCustomerResponse.class);
        logger.info("getGroupChatCustomerInfo Response:{}",response);
        return response.getBody();
    }


    /**
     * <获取会话内容存档内部群信息>接口
     * @param accessToken 调用接口凭证
     * @param roomId    企业的内部群id
     * @return 返回 GroupChatInternalResponse 对象
     */
    public GroupChatInternalResponse getGroupChatInternalInfo(String accessToken,String roomId) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("roomid", roomId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        String urlExternal = weChatApiConfig.getUrl().getMsgauditGroupchatDetail() + "?access_token=" + accessToken;
        ResponseEntity<GroupChatInternalResponse> response = restTemplate.postForEntity(urlExternal, entity, GroupChatInternalResponse.class);
        logger.info("getGroupChatInternalInfo Response:{}",response);
        return response.getBody();
    }


}