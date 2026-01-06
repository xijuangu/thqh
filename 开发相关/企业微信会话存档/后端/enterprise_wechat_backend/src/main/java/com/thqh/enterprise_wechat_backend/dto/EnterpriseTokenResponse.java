package com.thqh.enterprise_wechat_backend.dto;

import lombok.Data;

/**
 * @ClassName: EnterpriseTokenResponse
 * @Description:
 * @Author liubin
 * @Date 2025/3/20 15:47
 * @Version V1.0
 */
@Data
public class EnterpriseTokenResponse {
    private int errcode;
    private String errmsg;
    private String access_token;
    private int expires_in;
}
