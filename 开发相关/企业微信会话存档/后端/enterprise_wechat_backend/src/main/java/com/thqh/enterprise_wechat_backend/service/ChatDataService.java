package com.thqh.enterprise_wechat_backend.service;

import com.thqh.enterprise_wechat_backend.entity.ChatData;
import com.thqh.enterprise_wechat_backend.entity.RawChatData;
import com.thqh.enterprise_wechat_backend.mapper.ChatDataMapper;
import com.thqh.enterprise_wechat_backend.util.RSAUtil;
import com.thqh.enterprise_wechat_backend.util.WeChatSdkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @ClassName: ChatDataService
 * @Description:
 * @Author liubin
 * @Date 2025/3/5 17:20
 * @Version V1.0
 */
@Service
public class ChatDataService {

    private final ChatDataMapper chatDataMapper;

    private final ChatMessageService chatMessageService ;

    private final Logger logger = LoggerFactory.getLogger(ChatDataService.class);

    public ChatDataService(ChatDataMapper chatDataMapper, ChatMessageService chatMessageService) {
        this.chatDataMapper = chatDataMapper;
        this.chatMessageService = chatMessageService;
    }


    // 批量保存聊天消息
    public void saveRawChatDatas(List<RawChatData> rawChatDatas) {
        chatDataMapper.batchInsert(rawChatDatas);
    }

    /**
     * 从消息会话下载表取数据存入到消息记录业务表
     */
    @Transactional
    public void insertRawChatDataIntoChatData(){
        chatDataMapper.insertRawChatDataIntoChatData();
    }


    /**
     * 解密所有加密的chatdata数据
     */
    @Transactional
    public void decryptChatData(long sdk) throws Exception {
        List<ChatData> chatDataList = chatDataMapper.selectByDecryptFlag(0);
        PrivateKey privateKey = RSAUtil.readPrivateKeyFromFile("rsa_private_key.pem");
        for (ChatData chatData : chatDataList) {
            try{
                String encryptRandomKey = chatData.getEncryptRandomKey();
                String encryptKey = RSAUtil.decrypt(encryptRandomKey, privateKey);
                String chatMsg = WeChatSdkUtil.decryptData(sdk, encryptKey, chatData.getEncryptChatMsg());
                chatData.setDecryptFlag(1);
                chatData.setChatMsg(chatMsg);
                chatData.setUpdatedAt(LocalDateTime.now());
                chatDataMapper.updateChatData(chatData);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }



    /**
     * 获取数据库表chat_data中最大的seq值
     * @return
     */
    public long getMaxSeqFromChatData(){
        long seq =  chatDataMapper.getMaxSeqFromChatData();
        return seq;
    }


    /**
     * 从chatData生成会话消息chatMessage
     */
    @Transactional
    public void generateChatMessage(){
        //获取所有未生成ChatMessage的chatData
         List<ChatData> chatDataList = chatDataMapper.selectNoGeneratedMsg();
        for (ChatData chatData : chatDataList) {
            try {
                chatMessageService.generateChatMessage(chatData);
            } catch (Exception e) {
                logger.error(e.getMessage());
                chatData.setErrorLog(e.getMessage());
                chatData.setGenerateMsgFlag(2);
                chatData.setUpdatedAt(LocalDateTime.now());
                chatDataMapper.updateGenerateMsgFlag(chatData);
            }
        }
    }

}
