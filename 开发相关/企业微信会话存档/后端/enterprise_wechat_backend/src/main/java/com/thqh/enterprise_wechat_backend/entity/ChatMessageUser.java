package com.thqh.enterprise_wechat_backend.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @ClassName: ChatMessageUser
 * @Description:
 * @Author liubin
 * @Date 2025/4/2 17:56
 * @Version V1.0
 */
@Data
public class ChatMessageUser {
    // 自增主键
    private Long id;
    private String msgid;
    private String fromUser;
    private String toUser;
    private int createdBy;
    private LocalDateTime createdAt;
    private int updatedBy;
    private LocalDateTime updatedAt;
}
