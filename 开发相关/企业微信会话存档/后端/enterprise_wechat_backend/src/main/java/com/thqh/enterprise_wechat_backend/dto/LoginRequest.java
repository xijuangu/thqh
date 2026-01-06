package com.thqh.enterprise_wechat_backend.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * @ClassName: LoginRequest
 * @Description:
 * @Author liubin
 * @Date 2025/3/19 15:31
 * @Version V1.0
 */
@Data
public class LoginRequest {
    @NotBlank
    private String username;
    // 前端传入的已 MD5 加密后的密码
    @NotBlank
    private String password;
}
