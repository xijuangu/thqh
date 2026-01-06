package com.thqh.enterprise_wechat_backend.util;

import com.tencent.wework.Finance;
import com.thqh.enterprise_wechat_backend.exception.BusinessException;
import com.thqh.enterprise_wechat_backend.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: WeChatSdkUtil
 * @Description:
 * @Author liubin
 * @Date 2025/3/4 15:22
 * @Version V1.0
 */

public class WeChatSdkUtil {

    private static final Logger logger = LoggerFactory.getLogger(WeChatSdkUtil.class);

    // 初始化SDK
    public static long initSdk(String corpid, String secret) {
        long ret = 0;
        long sdk = Finance.NewSdk();
        ret = Finance.Init(sdk, corpid, secret);
        if (ret != 0) {
            Finance.DestroySdk(sdk);
            logger.error("SDK 初始化失败:{}", ret);
            throw new BusinessException(ErrorCode.INIT_SDK_ERROR, "SDK初始化失败:"+ret);
        }
        logger.info("SDK 初始化成功:{}", ret);
        return sdk;
    }

    // 销毁SDK
    public static void destroySdk(long sdk) {
        Finance.DestroySdk(sdk);
        logger.info("SDK 销毁成功:{}",sdk);
    }

    // 获取会话存档数据
    public static String getChatData(long sdk, long seq, int limit,String proxy, String password,int timeout) {
        long slice = Finance.NewSlice();
        long ret = Finance.GetChatData(sdk, seq, limit, proxy, password, timeout, slice);
        if (ret != 0) {
            Finance.FreeSlice(slice);
            logger.error("获取会话存档数据失败:{}", ret);
            throw new BusinessException(ErrorCode.GET_CHATDATA_ERROR, "获取会话存档数据失败:" + ret);
        }
        String content = Finance.GetContentFromSlice(slice);
        logger.info("获取会话存档数据结果:{}", content);
        Finance.FreeSlice(slice);
        return content;
    }

    //解密会话存档内容
    public static String decryptData(long sdk,String encryptKey,String encryptChatMsg){
        //sdk不会要求用户传入rsa私钥，保证用户会话存档数据只有自己能够解密。
        //此处需要用户先用rsa私钥解密encrypt_random_key后，作为encrypt_key参数传入sdk来解密encrypt_chat_msg获取会话存档明文。
        //每次使用DecryptData解密会话存档前需要调用NewSlice获取一个slice，在使用完slice中数据后，还需要调用FreeSlice释放。
        long msg = Finance.NewSlice();
        int ret = Finance.DecryptData(sdk, encryptKey, encryptChatMsg, msg);
        if (ret != 0) {
            Finance.FreeSlice(msg);
            logger.error("解密会话存档数据失败:{}",ret);
            throw new BusinessException(ErrorCode.DECRYPT_CHATDATA_ERROR, "解密会话存档数据失败:" + ret);
        }
        String content= Finance.GetContentFromSlice(msg);
        logger.info("会话存档数据解密内容:{}",content);
        Finance.FreeSlice(msg);
        return content;
    }


    //拉取媒体文件
    public static Map<String, Object> downloadMedia(long sdk,String sdkfileid,String proxy,String passwd,int timeout,String savefile) {
        //媒体文件每次拉取的最大size为512k，因此超过512k的文件需要分片拉取。若该文件未拉取完整，sdk的IsMediaDataFinish接口会返回0，同时通过GetOutIndexBuf接口返回下次拉取需要传入GetMediaData的indexbuf。
        //indexbuf一般格式如右侧所示，”Range:bytes=524288-1048575“，表示这次拉取的是从524288到1048575的分片。单个文件首次拉取填写的indexbuf为空字符串，拉取后续分片时直接填入上次返回的indexbuf即可。
        Map<String, Object> resultMap = new HashMap<>();
        String indexbuf = "";
        long ret = 0;
        while(true){
            //每次使用GetMediaData拉取存档前需要调用NewMediaData获取一个media_data，在使用完media_data中数据后，还需要调用FreeMediaData释放。
            long media_data = Finance.NewMediaData();
            ret = Finance.GetMediaData(sdk, indexbuf, sdkfileid, proxy, passwd, timeout, media_data);
            if(ret!=0){
                logger.error("下载媒体文件失败:{}",ret);
                Finance.FreeMediaData(media_data);
                resultMap.put("downloadSuccessFlag",false);
                resultMap.put("msg","下载媒体文件失败:" + ret);
                return resultMap;
            }
            logger.info("下载媒体文件数据:{} {} {}",Finance.GetIndexLen(media_data),Finance.GetDataLen(media_data), Finance.IsMediaDataFinish(media_data));

            //大于512k的文件会分片拉取，此处需要使用追加写，避免后面的分片覆盖之前的数据。
            try (FileOutputStream outputStream = new FileOutputStream(new File(savefile), true)) {
                outputStream.write(Finance.GetData(media_data));
            } catch (Exception e) {
                logger.error("保存媒体文件失败:{}",e.getMessage());
                Finance.FreeMediaData(media_data);
                resultMap.put("downloadSuccessFlag",false);
                resultMap.put("msg","保存媒体文件失败:" + e.getMessage());
                return resultMap;
            }

            if(Finance.IsMediaDataFinish(media_data) == 1)
            {
                //已经拉取完成最后一个分片
                Finance.FreeMediaData(media_data);
                break;
            }
            else
            {
                //获取下次拉取需要使用的indexbuf
                indexbuf = Finance.GetOutIndexBuf(media_data);
                Finance.FreeMediaData(media_data);
            }
        }
        resultMap.put("downloadSuccessFlag",true);
        return resultMap;
    }

}
