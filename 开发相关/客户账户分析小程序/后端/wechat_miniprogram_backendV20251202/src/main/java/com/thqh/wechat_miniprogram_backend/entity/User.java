package com.thqh.wechat_miniprogram_backend.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * @ClassName: User
 * @Description:
 * @Author liubin
 * @Date 2025/1/2 10:56
 * @Version V1.0
 */


@Data
public class User implements Serializable {
    // 推荐添加 serialVersionUID
    private static final long serialVersionUID = 1L;

    private Long id;
    private String phoneNumber;
    private String nickname;
    private String openid;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
}
