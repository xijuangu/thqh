package com.thqh.wechat_miniprogram_backend.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @ClassName: UserAccessLog
 * @Description:
 * @Author 
 * @Date 2025/10/23 15:50
 * @Version V1.0
 */

@Data
public class UserAccessLog {
    private Long id;
    private Long userId;
    private LocalDateTime accessTime;
    private String functionCode;
    private String clientIp;
    private String tradeAccount;
    private Integer accessStatus;
    private String errorMessage;
    private LocalDateTime createdTime;
    private Long createdBy;
    private LocalDateTime updatedTime;
    private Long updatedBy;
}
