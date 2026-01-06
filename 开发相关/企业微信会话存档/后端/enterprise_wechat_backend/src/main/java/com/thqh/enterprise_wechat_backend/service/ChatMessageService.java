package com.thqh.enterprise_wechat_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.thqh.enterprise_wechat_backend.dto.ChatterListQueryDTO;
import com.thqh.enterprise_wechat_backend.dto.ConversationDetailQueryDTO;
import com.thqh.enterprise_wechat_backend.dto.ConversationListQueryDTO;
import com.thqh.enterprise_wechat_backend.entity.*;
import com.thqh.enterprise_wechat_backend.exception.BusinessException;
import com.thqh.enterprise_wechat_backend.exception.ErrorCode;
import com.thqh.enterprise_wechat_backend.mapper.*;
import com.thqh.enterprise_wechat_backend.util.MinioUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ChatMessageService
 * @Description:
 * @Author liubin
 * @Date 2025/3/11 09:45
 * @Version V1.0
 */
@Service
public class ChatMessageService {

    private final ChatDataMapper chatDataMapper;

    private final ChatMessageMapper chatMessageMapper;

    private final ChatMessageUserMapper chatMessageUserMapper;

    private final ChatMediaMapper chatMediaMapper;

    private final WechatUserMapper wechatUserMapper;

    private final CustomerMapper customerMapper;

    private final GroupChatMapper groupChatMapper;

    private final MinioUtil minioUtil;

    private final Logger logger = LoggerFactory.getLogger(ChatMessageService.class);

    public ChatMessageService(ChatDataMapper chatDataMapper, ChatMessageMapper chatMessageMapper, ChatMessageUserMapper chatMessageUserMapper, ChatMediaMapper chatMediaMapper, WechatUserMapper wechatUserMapper, CustomerMapper customerMapper, GroupChatMapper groupChatMapper, MinioUtil minioUtil) {
        this.chatDataMapper = chatDataMapper;
        this.chatMessageMapper = chatMessageMapper;
        this.chatMessageUserMapper = chatMessageUserMapper;
        this.chatMediaMapper = chatMediaMapper;
        this.wechatUserMapper = wechatUserMapper;
        this.customerMapper = customerMapper;
        this.groupChatMapper = groupChatMapper;
        this.minioUtil = minioUtil;
    }


    public void generateChatMessage(ChatData chatData) throws Exception {
        String chatMsg = chatData.getChatMsg();
        //解析原始JSON并将其拆分保存到 chatMessage, chatMedia
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(chatMsg);
        //(1)=================解析生成chatMessage===============
        // 需特别判断是否是切换企业日志消息
        //{
        //	"msgid": "125289002219525886280",
        //	"action": "switch",
        //	"time": 1554119421840,
        //	"user": "XuJinSheng"
        //}
        //
        ChatMessage chatMessage = new ChatMessage();
        String action = root.path("action").asText();
        chatMessage.setMsgid(root.path("msgid").asText());
        chatMessage.setAction(action);
        if ("switch".equals(action)) {
            chatMessage.setMsgTime(root.path("time").asLong());
            chatMessage.setFromUser(root.path("user").asText());
            chatMessage.setChatMsg(chatMsg);
        } else {
            //{
            //	"msgid": "CAQQluDa4QUY0On2rYSAgAMgzPrShAE=",
            //	"action": "send",
            //	"from": "XuJinSheng",
            //	"tolist": ["icefog"],
            //	"roomid": "",
            //	"msgtime": 1547087894783,
            //	"msgtype": "text",
            //	"text": {
            //		"content": "test"
            //	}
            //}
            chatMessage.setFromUser(root.path("from").asText());
            chatMessage.setToList(root.path("tolist").toString());
            chatMessage.setRoomid(root.path("roomid").asText());
            chatMessage.setMsgTime(root.path("msgtime").asLong());
            chatMessage.setMsgType(root.path("msgtype").asText());
            chatMessage.setChatMsg(chatMsg);
        }
        // 先插入 chat_message
        chatMessageMapper.insert(chatMessage);
        Long chatMessageId = chatMessage.getId();
        // (2) 解析tolist数据生成chat_message_user表
        if(chatMessage.getToList() != null && !chatMessage.getToList().isEmpty()){
            List<ChatMessageUser> chatMessageUserList = new ArrayList<>();
            String[] toUserList = objectMapper.readValue(chatMessage.getToList(), String[].class);
            for (String toUser : toUserList) {
                ChatMessageUser chatMessageUser = new ChatMessageUser();
                chatMessageUser.setMsgid(chatMessage.getMsgid());
                chatMessageUser.setFromUser(chatMessage.getFromUser());
                chatMessageUser.setToUser(toUser);
                chatMessageUserList.add(chatMessageUser);
            }
            if(!chatMessageUserList.isEmpty()){
                chatMessageUserMapper.batchInsertChatMessageUsers(chatMessageUserList);
            }
        }

        // (2) ===============根据 msgtype 判定是否有 sdkfileid,生成chatMedia===============
        String msgType = chatMessage.getMsgType();
        if (msgType != null) {
            if ("mixed".equals(msgType)) {
                // mixed 混合消息
                //{
                //	"msgid": "DAQQluDa4QUY0On4kYSABAMgzPrShAE=",
                //	"action": "send",
                //	"from": "HeMiao",
                //	"tolist": ["HeChangTian", "LiuZeYu"],
                //	"roomid": "wr_tZ2BwAAUwHpYMwy9cIWqnlU3Hzqfg",
                //	"msgtime": 1577414359072,
                //	"msgtype": "mixed",
                //	"mixed": {
                //		"item": [{
                //			"type": "text",
                //			"content": "{\"content\":\"你好[微笑]\\n\"}"
                //		}, {
                //			"type": "image",
                //			"content": "{\"md5sum\":\"368b6c18c82e6441bfd89b343e9d2429\",\"filesize\":13177,\"sdkfileid\":\"CtY...OTEy\"}"
                //		}]
                //	}
                //}
                JsonNode mixedNode = root.path("mixed").path("item");
                if (mixedNode.isArray()) {
                    for (JsonNode item : mixedNode) {
                        // item: {"type":"image","content":"{...}"}
                        String type = item.path("type").asText();
                        String contentStr = item.path("content").asText();
                        // contentStr 仍是 JSON 字符串 => 需再解析
                        JsonNode contentJson = objectMapper.readTree(contentStr);
                        // 如果是 image/file/voice/emotion... 就提取 sdkfileid
                        ChatMedia chatMedia = parseChatMedia(type, contentJson);
                        if(chatMedia != null){
                            chatMedia.setMsgid(chatMessage.getMsgid());
                            chatMediaMapper.insert(chatMedia);
                        }
                    }
                }
            }else if("chatrecord".equals(msgType)){
                /**{
                    "msgid": "17230214807648521300_1741315315376_external",
                    "action": "send",
                    "from": "wmykZGPQAAo5cSSuPhyEC-r1lzXNxcoQ",
                    "tolist": ["LuPeiYing"],
                    "roomid": "",
                    "msgtime": 1741315311368,
                    "msgtype": "chatrecord",
                    "chatrecord": {
                        "title": "斌与41S⁵的聊天记录",
                        "item": [{
                            "type": "ChatRecordText",
                            "msgtime": 1741154556,
                            "content": "{\"content\":\"现在list文件是和gpg平级，看下有没有影响。\"}",
                            "from_chatroom": false
                        }, {
                            "type": "ChatRecordImage",
                            "msgtime": 1741154556,
                            "content": "{\"md5sum\":\"69a7c48c4c570a7e36fa6ab4f0995828\",\"filesize\":23882,\"sdkfileid\":\"CiBjZmYyNzNiYWQ4ZTE3N2FjY2RkNjkwYTNmYWJlN2FiZBI4TkRkZk56ZzRNVE13TURZeE1EQXpOakl3T1Y4MU5UWTRNakU0TWpOZk1UYzBNVE14TlRNeE5RPT0aIGY5YmQ0NjQzOGViMTBhZWQ0MjlhNTQ5ZGQwN2E0YmJj\"}",
                            "from_chatroom": false
                        },
                        ....
                        {
                            "type": "ChatRecordText",
                            "msgtime": 1741156247,
                            "content": "{\"content\":\"okk\"}",
                            "from_chatroom": false
                        }]
                    }
                }*/
                JsonNode chatRecordNode = root.path("chatrecord").path("item");
                for (JsonNode item : chatRecordNode) {
                    // item: {"type":"image","content":"{...}"}
                    String type = item.path("type").asText();
                    String contentStr = item.path("content").asText();
                    // contentStr 仍是 JSON 字符串 => 需再解析
                    JsonNode contentJson = objectMapper.readTree(contentStr);
                    // 如果是 ChatRecordFile/ ChatRecordImage/ ChatRecordVideo... 就提取 sdkfileid
                    ChatMedia chatMedia = parseChatMedia(type, contentJson);
                    if(chatMedia != null){
                        chatMedia.setMsgid(chatMessage.getMsgid());
                        chatMediaMapper.insert(chatMedia);
                    }
                }
            } else {
                ChatMedia chatMedia = parseChatMedia(msgType, root.path(msgType));
                if (chatMedia != null) {
                    chatMedia.setMsgid(chatMessage.getMsgid());
                    chatMediaMapper.insert(chatMedia);
                }
            }
        }
        //(3)生成chatMessage和chatMedia成功后处理chatData标记
        chatData.setGenerateMsgFlag(1);
        chatData.setErrorLog(null);
        chatData.setUpdatedAt(LocalDateTime.now());
        chatDataMapper.updateGenerateMsgFlag(chatData);
    }


    /**
     * 解析多媒体类型消息
     * @param msgType 消息类型
     * @param jsonNode 消息json结构体
     * @return 多媒体对象
     */
    public ChatMedia parseChatMedia(String msgType,JsonNode jsonNode){
        ChatMedia chatMedia = new ChatMedia();
        if("image".equals(msgType) || "ChatRecordImage".equals(msgType)){
            // 单纯 image 消息 =>
            // {
            // "msgid": "CAQQvPnc4QUY0On2rYSAgAMgooLa0Q8=",
            // "action": "send",
            // "from": "XuJinSheng",
            // "tolist": ["icefog"],
            // "roomid": "",
            //"msgtime": 0,
            // "msgtype": "image",
            // "image": {
            //          "md5sum": "50de8e5ae8ffe4f1df7a93841f71993a",
            //          "filesize": 70961,
            //          "sdkfileid": "CtYBM...kz"
            //          }
            // }
            String sdkfileid = jsonNode.path("sdkfileid").asText();
            String md5Sum = jsonNode.path("md5sum").asText();
            Long mediaSize = jsonNode.path("filesize").asLong();
            chatMedia.setSdkfileid(sdkfileid);
            chatMedia.setMd5Sum(md5Sum);
            chatMedia.setMediaSize(mediaSize);
            chatMedia.setMediaType(msgType);
        }else if ("voice".equals(msgType) || "ChatRecordVoice".equals(msgType)) {
            // {
            //  "msgid": "10958372969718811103_1603875609",
            //  "action": "send",
            //  "from": "wmGAgeDQAAdBjb8CK4ieMPRm7Cqm-9VA",
            //  "tolist": ["kenshin"],
            //  "roomid": "",
            //  "msgtime": 1603875609704,
            //  "msgtype": "voice",
            //  "voice": {
            //      "md5sum": "9db09c7fa627c9e53f17736c786a74d5",
            //      "voice_size": 6810,
            //      "play_length": 10,
            //      "sdkfileid": "kc....4NjY="
            //      }
            //}
            String sdkfileid = jsonNode.path("sdkfileid").asText();
            String md5Sum = jsonNode.path("md5sum").asText();
            Long mediaSize = jsonNode.path("voice_size").asLong();
            Long playLength = jsonNode.path("play_length").asLong();
            chatMedia.setSdkfileid(sdkfileid);
            chatMedia.setMd5Sum(md5Sum);
            chatMedia.setMediaSize(mediaSize);
            chatMedia.setPlayLength(playLength);
            chatMedia.setMediaType(msgType);
        }
        else if ("video".equals(msgType) || "ChatRecordVideo".equals(msgType)) {
            // {
            // "msgid": "17955920891003447432_1603875627",
            // "action": "send",
            // "from": "kenshin",
            // "tolist": ["wmGAgeDQAAHuRJbt4ZQI_1cqoQcf41WQ"],
            // "roomid": "",
            // "msgtime": 1603875626823,
            // "msgtype": "video",
            // "video": {
            //      "md5sum":"d06fc80c01d6fbffcca3b229ba41eac6",
            //      "filesize": 15169724,
            //      "play_length": 108,
            //      "sdkfileid": "MzA....NDk="
            //      }
            //}
            String sdkfileid = jsonNode.path("sdkfileid").asText();
            String md5Sum = jsonNode.path("md5sum").asText();
            Long mediaSize = jsonNode.path("filesize").asLong();
            Long playLength = jsonNode.path("play_length").asLong();
            chatMedia.setSdkfileid(sdkfileid);
            chatMedia.setMd5Sum(md5Sum);
            chatMedia.setMediaSize(mediaSize);
            chatMedia.setPlayLength(playLength);
            chatMedia.setMediaType(msgType);
        }
        else if("emotion".equals(msgType) || "ChatRecordEmotion".equals(msgType)) {
            //  {
            //  "msgid": "6623217619416669654_1603875612",
            //  "action": "send",
            //  "from": "icef",
            //  "tolist": ["wmErxtDgAAhteCglUZH2kUt3rq431qmg"],
            //  "roomid": "",
            //  "msgtime": 1603875611148,
            //  "msgtype": "emotion",
            //  "emotion": {
            //      "type": 1,
            //      "width": 290,
            //      "height": 290,
            //      "imagesize": 962604,
            //      "md5sum": "94c2b0bba52cc456cb8221b248096612",
            //      "sdkfileid": "4eE1EMTYx"
            //      }
            // }
            String sdkfileid = jsonNode.path("sdkfileid").asText();
            String md5Sum = jsonNode.path("md5sum").asText();
            Long mediaSize = jsonNode.path("imagesize").asLong();
            Long width = jsonNode.path("width").asLong();
            Long height = jsonNode.path("height").asLong();
            String type = jsonNode.path("type").asText();
            chatMedia.setSdkfileid(sdkfileid);
            chatMedia.setMd5Sum(md5Sum);
            chatMedia.setMediaSize(mediaSize);
            chatMedia.setPlayLength(height);
            chatMedia.setWidth(width);
            chatMedia.setHeight(height);
            chatMedia.setType(type);
            chatMedia.setMediaType(msgType);
        }
        else if ("file".equals(msgType) || "ChatRecordFile".equals(msgType)) {
            // {
            // "msgid": "18039699423706571225_1603875608",
            // "action": "send",
            // "from": "kens",
            // "tolist": ["wmErxtDgAArDlFIhf76O6w4GxU81al8w"],
            // "roomid": "",
            // "msgtime": 1603875608214,
            // "msgtype": "file",
            // "file": {
            //      "md5sum": "18e93fc2ea884df23b3d2d3b8667b9f0",
            //      "filename": "资料.docx",
            //      "fileext": "docx",
            //      "filesize": 18181,
            //      "sdkfileid": "E4OD...NzYx"
            //      }
            // }
            String sdkfileid = jsonNode.path("sdkfileid").asText();
            String md5Sum = jsonNode.path("md5sum").asText();
            Long mediaSize = jsonNode.path("filesize").asLong();
            String mediaName = jsonNode.path("filename").asText();
            String fileext = jsonNode.path("fileext").asText();
            chatMedia.setSdkfileid(sdkfileid);
            chatMedia.setMd5Sum(md5Sum);
            chatMedia.setMediaSize(mediaSize);
            chatMedia.setMediaName(mediaName);
            chatMedia.setType(fileext);
            chatMedia.setMediaType(msgType);
        }
        else if ("meeting_voice_call".equals(msgType) || "ChatRecordMeetingVoiceCall".equals(msgType)) {
            //  {
            //	"msgid": "17952229780246929345_1594197637",
            //	"action": "send",
            //	"from": "wo137MCgAAYW6pIiKKrDe5SlzEhSgwbA",
            //	"tolist": ["wo137MCgAAYW6pIiKKrDe5SlzEhSgwbA"],
            //	"msgtime": 1594197581203,
            //	"msgtype": "meeting_voice_call",
            //	"voiceid": "grb8a4c48a3c094a70982c518d55e40557",
            //	"meeting_voice_call": {
            //		"endtime": 1594197635,
            //		"sdkfileid": "CpsBKjAOQ==",
            //		"demofiledata": [{
            //			"filename": "65eb1cdd3e7a3c1740ecd74220b6c627.docx",
            //			"demooperator": "wo137MCgAAYW6pIiKKrDe5SlzEhSgwbA",
            //			"starttime": 1594197599,
            //			"endtime": 1594197609
            //		}],
            //		"sharescreendata": [{
            //			"share": "wo137MCgAAYW6pIiKKrDe5SlzEhSgwbA",
            //			"starttime": 1594197624,
            //			"endtime": 1594197624
            //		}]
            //	}
            //}
            String sdkfileid = jsonNode.path("sdkfileid").asText();
            chatMedia.setSdkfileid(sdkfileid);
            chatMedia.setMediaType(msgType);
        }
        else if ("voip_doc_share".equals(msgType) || "ChatRecordVoipDocShare".equals(msgType)) {
            // {
            //	"msgid": "16527954622422422847_1594199256",
            //	"action": "send",
            //	"from": "18002520162",
            //	"tolist": ["wo137MCgAAYW6pIiKKrDe5SlzEhSgwbA"],
            //	"msgtime": 1594199235014,
            //	"msgtype": "voip_doc_share",
            //	"voipid": "gr2751c98b19300571f8afb3b74514bd32",
            //	"voip_doc_share": {
            //		"filename": "欢迎使用微盘.pdf.pdf",
            //		"md5sum": "ff893900f24e55e216e617a40e5c4648",
            //		"filesize": 4400654,
            //		"sdkfileid": "Cps.....Q=="
            //	}
            //}
            String sdkfileid = jsonNode.path("sdkfileid").asText();
            String md5Sum = jsonNode.path("md5sum").asText();
            Long mediaSize = jsonNode.path("filesize").asLong();
            String mediaName = jsonNode.path("filename").asText();
            chatMedia.setSdkfileid(sdkfileid);
            chatMedia.setMd5Sum(md5Sum);
            chatMedia.setMediaSize(mediaSize);
            chatMedia.setMediaName(mediaName);
            chatMedia.setMediaType(msgType);
        }else{
            chatMedia = null;
        }
        return chatMedia;
    }


    /**
     * 获取所有不重复的群聊roomId
     * @return
     */
    public List<String> getDistinctRoomIds(){
        return chatMessageMapper.getDistinctRoomIds();
    }

    /**
     * 获取对话人列表
     * @param queryDTO 查询参数
     * @return
     */
    public Map<String, Object> getChatterListByType(ChatterListQueryDTO queryDTO) {
        String type = queryDTO.getType().toLowerCase();
        int page = queryDTO.getPage();
        int limit = queryDTO.getLimit();
        String name = queryDTO.getName();

        // 定义结果映射
        Map<String, Object> result = new HashMap<>();
        result.put("page", page);
        result.put("limit", limit);
        try {
            PageHelper.startPage(page, limit);
            switch (type) {
                case "employee":
                    List<WechatUser> employees = wechatUserMapper.getEmployeesByPage(name);
                    PageInfo<WechatUser> employeePageInfo = new PageInfo<>(employees);
                    result.put("result", employeePageInfo.getList());
                    result.put("total", employeePageInfo.getTotal());
                    break;
                case "customer":
                    List<Customer> customers = customerMapper.getCustomersByPage(name);
                    PageInfo<Customer> customerPageInfo = new PageInfo<>(customers);
                    result.put("result", customerPageInfo.getList());
                    result.put("total", customerPageInfo.getTotal());
                    break;
                case "groupchat":
                    List<GroupChat> groupChats = groupChatMapper.getGroupChatsByPage(name);
                    PageInfo<GroupChat> groupChatPageInfo = new PageInfo<>(groupChats);
                    result.put("result", groupChatPageInfo.getList());
                    result.put("total", groupChatPageInfo.getTotal());
                    break;
                default:
                    result.put("result", null);
                    result.put("error", "Invalid type parameter");
                    break;
            }
        } catch (Exception e) {
            result.put("result", null);
            result.put("error", e.getMessage());
            logger.error("Error getChatterListByType: {}",e.getMessage());
        }
        return result;
    }


    /**
     * 获取指定对象的对话列表
     * 当请求参数中的 from 为 "employee" 时，表示查询某个员工相关的会话联系人。
     * 如果 to 为 "employee"，系统将从 chat_message_user 表中查找所有 from_user 等于该员工 ID 的记录，并将其中的每一个 to_user 与 wechat_user 表进行匹配，只有存在于该表中的员工 ID 才会被识别为有效员工并返回。
     * 如果 to 为 "customer"，同样从 chat_message_user 表中查找该员工发出的所有消息记录，对每一条 to_user 字段进行判断，仅当该 to_user 在 customer 表中存在（即为客户）时，才返回对应的客户数据。
     * 如果 to 为 "groupchat"，则通过查询 group_chat_member 表中是否存在 userid 等于该员工 ID 的记录，从而获取该员工参与的所有群聊 ID，然后从 group_chat 表中查找这些群聊的详细信息并返回。
     * 当请求参数中的 from 为 "customer" 时，表示查询某个客户相关的会话联系人。
     * 如果 to 为 "employee"，系统会在 chat_message_user 表中查找 from_user 为该客户 ID 的所有记录，然后对每条记录中的 to_user 判断是否存在于 wechat_user 表中，若存在则返回对应员工信息。
     * 如果 to 为 "groupchat"，将通过 group_chat_member 表查找 userid 等于该客户 ID 的记录，获取其加入的群聊 ID，并在 group_chat 表中查询并返回这些群聊信息。
     * @param queryDTO 查询参数
     * @return
     */
    public Map<String, Object> getConversationList(ConversationListQueryDTO queryDTO){
        String from = queryDTO.getFrom();
        String id = queryDTO.getId();
        String to = queryDTO.getTo();
        int page = queryDTO.getPage();
        int limit = queryDTO.getLimit();
        boolean hasConversation  = queryDTO.getHasConversation();

        String searchName = queryDTO.getName();

        // 定义结果映射
        Map<String, Object> result = new HashMap<>();
        result.put("page", page);
        result.put("limit", limit);

        PageHelper.startPage(page, limit);

        if ("employee".equals(from)) {
            if ("employee".equals(to)) {
                // 查询员工对话列表
                List<WechatUser> wechatUserList = new ArrayList<>();
                if(hasConversation){
                    wechatUserList =  chatMessageMapper.findEmployeeConversations(id,searchName);
                }else{
                    wechatUserList = chatMessageMapper.findEmployeeConversations_hasConversation(id,searchName);
                }

                PageInfo<WechatUser> wechatUserPageInfo= new PageInfo<>(wechatUserList);
                result.put("data", wechatUserPageInfo.getList());
                result.put("total", wechatUserPageInfo.getTotal());

            } else if ("customer".equals(to)) {
                // 查询客户对话列表
                List<Customer> customerList = new ArrayList<>();
                if(hasConversation){
                    customerList =  chatMessageMapper.findCustomerConversations_hasConversation(id, searchName);
                }else{
                    customerList = chatMessageMapper.findCustomerConversations(id, searchName);
                }

                PageInfo<Customer> customerPageInfo= new PageInfo<>(customerList);
                result.put("data", customerPageInfo.getList());
                result.put("total", customerPageInfo.getTotal());
            } else if ("groupchat".equals(to)) {
                // 查询群聊列表
                List<GroupChat> groupChatList = chatMessageMapper.findGroupChatConversations(id,searchName);
                PageInfo<GroupChat> groupChatPageInfo= new PageInfo<>(groupChatList);
                result.put("data", groupChatPageInfo.getList());
                result.put("total", groupChatPageInfo.getTotal());
            }
        } else if ("customer".equals(from)) {
            if ("employee".equals(to)) {
                // 查询员工对话列表
                List<WechatUser> wechatUserList = chatMessageMapper.findEmployeeConversations(id,searchName);
                PageInfo<WechatUser> wechatUserPageInfo= new PageInfo<>(wechatUserList);
                result.put("data", wechatUserPageInfo.getList());
                result.put("total", wechatUserPageInfo.getTotal());
            } else if ("groupchat".equals(to)) {
                // 查询群聊列表
                List<GroupChat> groupChatList = chatMessageMapper.findGroupChatConversations(id,searchName);
                PageInfo<GroupChat> groupChatPageInfo= new PageInfo<>(groupChatList);
                result.put("data", groupChatPageInfo.getList());
                result.put("total", groupChatPageInfo.getTotal());
            }
        }else{
            result.put("result", null);
            result.put("error", "Invalid type parameter");
            logger.error("Invalid type parameter: {}", queryDTO);
        }
        return result;
    }


    /**
     * 获取会话详情
     * @param queryDTO
     * @return
     */
    public Map<String, Object> getConversationDetail(ConversationDetailQueryDTO queryDTO){
        String type = queryDTO.getType();
        String from = queryDTO.getFrom();
        String to = queryDTO.getTo();
        String searchName = queryDTO.getSearchName();
        int page = queryDTO.getPage();
        int limit = queryDTO.getLimit();
        String filter_msgtype = queryDTO.getFilter_msgtype();

        // 定义结果映射
        Map<String, Object> result = new HashMap<>();
        result.put("page", page);
        result.put("limit", limit);
        List<ChatMessage> messages;

        PageHelper.startPage(page, limit);

        if ("groupchat".equals(type)) {
            messages =  chatMessageMapper.selectByGroupChatId(from,searchName,filter_msgtype);
        }else{
            messages = chatMessageMapper.selectByParticipants(from, to,searchName,filter_msgtype);
        }
        PageInfo<ChatMessage> chatMessagePageInfo= new PageInfo<>(messages);
        result.put("data", chatMessagePageInfo.getList());
        result.put("total", chatMessagePageInfo.getTotal());
        return result;
    }


    /**
     * 通过指定多媒体文件id获取附件
     * @param sdkfileid 多媒体文件id
     * @return
     * @throws Exception
     */
    public byte[] downloadChatMedia(String sdkfileid) {
        try{
            ChatMedia media = chatMediaMapper.selectBySdkfileid(sdkfileid);
            if (media == null) {
                logger.error("sdkfileid not exists:{}", sdkfileid);
                throw new BusinessException(ErrorCode.SDKFILEID_NOT_EXISTS.getCode(),ErrorCode.SDKFILEID_NOT_EXISTS.getMessage());
            }
            if (media.getDownloadStatus() != 1) {
                throw new BusinessException(ErrorCode.MEDIA_NOT_EXISTS.getCode(),ErrorCode.MEDIA_NOT_EXISTS.getMessage());
            }
            String objectPath = media.getDownloadUrl();
            return minioUtil.downloadFile(objectPath);
        } catch (BusinessException e) {
            throw new BusinessException(e.getCode(),e.getMessage());
        }catch (Exception e){
            logger.error("downloadChatMedia error:{}", e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * 通过msgid获取指定某条聊天记录，用于撤回消息查询前一条消息
     * @param pre_msgid 聊天记录的msgid
     * @return
     * @throws Exception
     */
    public ChatMessage getChatMessageByPreMsgid(String pre_msgid) {
        ChatMessage chatMessage = chatMessageMapper.selectByPreMsgid(pre_msgid);
        if (chatMessage == null) {
            logger.error("pre_msgid not exists:{}", pre_msgid);
            throw new BusinessException(ErrorCode.PRE_MSGID_NOT_EXISTS.getCode(),ErrorCode.PRE_MSGID_NOT_EXISTS.getMessage());
        }
        return chatMessage;
    }




    /**
     * 通过userId获取指定某用户的详细信息
     * @param from_userId
     * @return
     * @throws Exception
     */
    public Map<String, Object> getGroupNameAndLable(String from_userId) {
        Map<String, Object> result = new HashMap<>();
        //from_user可能为员工或客户，在员工表中找不到相关信息时，查询客户表
        Customer customer = customerMapper.selectByUserId(from_userId);
        if (customer == null) {
            logger.error("from_userId not exists in customer table:{}", from_userId);
        }else {
            result.put("from_user", customer);
            return result;
        }
        WechatUser wechatUser = wechatUserMapper.selectByUserId(from_userId);
        if (wechatUser == null) {
            logger.error("from_userId not exists in wechat_user table:{}", from_userId);
        }else {
            result.put("from_user", wechatUser);
            return result;
        }
        return null;
    }

    /**
     * 通过chatId获取指定某群聊的人员数量
     * @param chatId
     * @return
     * @throws Exception
     */
    public  int getgroupMenberCount(String chatId) {
        //from_user可能为员工或客户，在员工表中找不到相关信息时，查询客户表
        int count = customerMapper.getgroupMenberCountBychatId(chatId);
        return count;
    }

}