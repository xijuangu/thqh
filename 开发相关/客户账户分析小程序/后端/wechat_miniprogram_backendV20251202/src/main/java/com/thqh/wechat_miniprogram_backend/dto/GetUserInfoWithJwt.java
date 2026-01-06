package com.thqh.wechat_miniprogram_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @ClassName: GetUserInfoWithJwt
 * @Description:
 * @Author liubin
 * @Date 2025/1/2 16:39
 * @Version V1.0
 */


@Data
public class GetUserInfoWithJwt {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("pure_phone_number")
    private String purePhoneNumber;
    @JsonProperty("country_code")
    private String countryCode;
    @JsonProperty("jwt_token")
    private String jwtToken;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("openid")
    private String openid;
    @JsonProperty("JWT_expiration_time")
    private String jwtExpirationTime;
}

