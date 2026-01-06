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
public class User {
    private int id;
    private String username;
    private String password;
    // 1: 正常, 0: 禁用
    private int status;
    private int createdBy;
    private LocalDateTime createdAt;
    private int updatedBy;
    private LocalDateTime updatedAt;
}
