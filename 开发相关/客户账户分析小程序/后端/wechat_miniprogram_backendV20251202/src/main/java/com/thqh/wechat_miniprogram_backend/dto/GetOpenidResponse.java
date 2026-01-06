package com.thqh.wechat_miniprogram_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @ClassName: GetOpenidResponse
 * @Description:
 * @Author liubin
 * @Date 2025/1/21 14:15
 * @Version V1.0
 */

@Data
public class GetOpenidResponse {
    @JsonProperty("errcode")
    private Integer errcode;

    @JsonProperty("errmsg")
    private String errmsg;

    @JsonProperty("session_key")
    private String sessionKey;

    @JsonProperty("openid")
    private String openid;


}
