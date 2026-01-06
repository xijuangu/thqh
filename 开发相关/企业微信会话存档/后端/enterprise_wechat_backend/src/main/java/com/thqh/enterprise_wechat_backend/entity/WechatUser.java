package com.thqh.enterprise_wechat_backend.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @ClassName: User
 * @Description:
 * @Author liubin
 * @Date 2025/3/19 15:33
 * @Version V1.0
 */
@Data
public class WechatUser {
    private int id;
    private String userid;
    private String name;
    private int status;
    private int sessionArchiveFlag;
    private int createdBy;
    private LocalDateTime createdAt;
    private int updatedBy;
    private LocalDateTime updatedAt;
}
