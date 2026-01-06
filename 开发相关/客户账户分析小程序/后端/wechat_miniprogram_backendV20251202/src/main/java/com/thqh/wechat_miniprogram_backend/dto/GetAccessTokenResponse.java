package com.thqh.wechat_miniprogram_backend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
/**
 * @ClassName: GetAccessTokenResponse
 * @Description:
 * @Author liubin
 * @Date 2025/1/2 14:14
 * @Version V1.0
 */


@Data
public class GetAccessTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("errcode")
    private Integer errcode;

    @JsonProperty("errmsg")
    private String errmsg;
}


