package com.thqh.enterprise_wechat_backend.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @ClassName: RawChatData
 * @Description:
 * @Author liubin
 * @Date 2025/3/5 17:15
 * @Version V1.0
 */
@Data
public class ChatData {
    // 自增主键
    private Long id;
    private Long seq;
    private String msgid;
    private int publickeyVer;
    private String encryptRandomKey;
    private String encryptChatMsg;
    private int  decryptFlag;
    private int generateMsgFlag;
    private String errorLog;
    private String chatMsg;
    private int createdBy;
    private LocalDateTime createdAt;
    private int updatedBy;
    private LocalDateTime updatedAt;
}
