package com.thqh.enterprise_wechat_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @ClassName: LoginRequest
 * @Description:
 * @Author liubin
 * @Date 2025/3/19 15:31
 * @Version V1.0
 */
@Data
public class LoginResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expire")
    private int expire;
}
