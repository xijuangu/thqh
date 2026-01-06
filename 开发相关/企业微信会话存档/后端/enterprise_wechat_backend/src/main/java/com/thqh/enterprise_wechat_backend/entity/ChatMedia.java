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
public class ChatMedia {
    // 自增主键
    private Long id;
    private String msgid;
    private String sdkfileid;
    private String mediaType;
    private String md5Sum;
    private Long mediaSize;
    private Long playLength;
    private String mediaName;
    private Long width;
    private Long height;
    private String type;
    private String downloadUrl;
    private int downloadStatus;
    private String errorLog;
    private int createdBy;
    private LocalDateTime createdAt;
    private int updatedBy;
    private LocalDateTime updatedAt;
}
