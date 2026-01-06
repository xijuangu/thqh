package com.thqh.enterprise_wechat_backend.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @ClassName: RawChatData
 * @Description:
 * @Author liubin
 * @Date 2025/3/ 17:20
 * @Version V1.0
 */
@Data
public class ChatMessage {
    // 自增主键
    private Long id;
    private String msgid;
    private String action;
    private String fromUser;
    private String toList;
    private String roomid;
    private Long msgTime;
    private String msgType;
    private String chatMsg;
    private int createdBy;
    private LocalDateTime createdAt;
    private int updatedBy;
    private LocalDateTime updatedAt;
}
