package com.thqh.enterprise_wechat_backend.service;

import com.thqh.enterprise_wechat_backend.entity.ChatMedia;
import com.thqh.enterprise_wechat_backend.mapper.ChatMediaMapper;
import com.thqh.enterprise_wechat_backend.util.MinioUtil;
import com.thqh.enterprise_wechat_backend.util.WeChatSdkUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName: ChatMediaService
 * @Description:
 * @Author liubin
 * @Date 2025/3/11 10:15
 * @Version V1.0
 */
@Service
public class ChatMediaService {

    private final ChatMediaMapper chatMediaMapper;

    private final MinioUtil minioUtil;

    private static final Logger logger = LoggerFactory.getLogger(ChatMediaService.class);

    public ChatMediaService(ChatMediaMapper chatMediaMapper, MinioUtil minioUtil) {
        this.chatMediaMapper = chatMediaMapper;
        this.minioUtil = minioUtil;
    }

    /**
     * 下载所有未成功上传到minio的多媒体文件
     * @param sdk
     */
    @Transactional
    public void downloadMediaDataAndUpload(long sdk){
        List<ChatMedia> chatMediaList = chatMediaMapper.findByDownloadStatus(0);
        for(ChatMedia chatMedia : chatMediaList){
            try{
                extracted(sdk, chatMedia);
            }catch (Exception e){
                logger.error("执行媒体下载过程失败: {}",e.getMessage());
            }
        }
    }


    /**
     * 下载单个多媒体文件方法
     * @param sdk
     * @param chatMedia
     */
    public void extracted(long sdk, ChatMedia chatMedia) {
        String sdkfileid = chatMedia.getSdkfileid();
        //(1)通过sdkfileid下载多媒体文件保存到本地
        if (sdkfileid == null || sdkfileid.trim().isEmpty()) {
            return ;
        }
        // 用 UUID 防止重名冲突
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String localFilePath = "/tmp/" + uuid + "_" + System.currentTimeMillis();
        // 根据媒体类型添加文件类型后缀
        String mediaType = chatMedia.getMediaType();
        if("image".equals(mediaType)){
            localFilePath = localFilePath + ".jpg";
        }else if("voice".equals(mediaType) || "meeting_voice_call".equals(mediaType) || "voip_doc_share".equals(mediaType)){
            localFilePath = localFilePath + ".amr";
        }else if("video".equals(mediaType)){
            localFilePath = localFilePath + ".mp4";
        }else if("emotion".equals(mediaType)){
            String type= chatMedia.getType();
            if("1".equals(type)){
                localFilePath = localFilePath + ".gif";
            }else if("2".equals(type)){
                localFilePath = localFilePath + ".png";
            }
        }else if("file".equals(mediaType)){
            String  mediaName= chatMedia.getMediaName();
            localFilePath = localFilePath + "_" + mediaName;
        }
        logger.info("localFilePath:{}",localFilePath);
        boolean downloadSuccessFlag = false;
        Map<String, Object> downloadResult = Collections.emptyMap();
        try {
            downloadResult = WeChatSdkUtil.downloadMedia(sdk,sdkfileid,null,null,30, localFilePath);
        } catch (Exception e) {
            chatMedia.setErrorLog(e.getMessage());
            logger.error("下载多媒体文件失败:{}",e.getMessage());
        }
        downloadSuccessFlag = (boolean) downloadResult.get("downloadSuccessFlag");
        if (!downloadSuccessFlag) {
            // 标记下载失败
            chatMedia.setDownloadStatus(2);
            chatMedia.setErrorLog((String) downloadResult.get("msg"));
            chatMediaMapper.updateDownloadInfo(chatMedia);
            return ;
        }

        //(2)若多媒体文件存在md5值这进行校验
        String md5Sum = chatMedia.getMd5Sum();
        boolean md5Pass = true;
        if (md5Sum != null && !md5Sum.isEmpty()) {
            md5Pass = checkFileMD5(localFilePath, md5Sum);
            if (!md5Pass) {
                // MD5校验不通过
                chatMedia.setDownloadStatus(2);
                chatMedia.setErrorLog("MD5校验失败");
                chatMediaMapper.updateDownloadInfo(chatMedia);
//                   new File(localFilePath).delete();
                logger.error("MD5校验失败: sdkfileid={}",sdkfileid);
            }else{
                logger.info("MD5校验成功: sdkfileid={}",sdkfileid);
            }
        }

        //(3)上传minio返回保存地址
        String minioUrl = null;
        try {
            minioUrl = minioUtil.uploadFile(localFilePath);
        } catch (Exception e) {
            logger.error("上传多媒体文件到minio失败:{}",e.getMessage());
            chatMedia.setDownloadStatus(2);
            chatMedia.setErrorLog("上传多媒体文件到minio失败" + e.getMessage());
            chatMediaMapper.updateDownloadInfo(chatMedia);
            return ;
        }

        //(4)成功则更新保存地址及下载状态,若存在md5Pass不通过的情况则保留错误状态和日志
        chatMedia.setDownloadUrl(minioUrl);
        if(md5Pass){
            chatMedia.setDownloadStatus(1);
            chatMedia.setErrorLog(null);
        }
        chatMediaMapper.updateDownloadInfo(chatMedia);

        //(5) 删除本地临时文件
//            File file = new File(localFilePath);
//            if (file.exists()) {
//                file.delete();
//            }
    }


    /**
     * 计算文件MD5并与 expectedMd5 对比 (hex)
     */
    private boolean checkFileMD5(String filePath, String expectedMd5) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            String fileMd5 = DigestUtils.md5Hex(fis);
            return expectedMd5.equalsIgnoreCase(fileMd5);
        } catch (Exception e) {
            logger.error("校验文件Md5时发生错误:{}",e.getMessage());
            return false;
        }
    }

}
