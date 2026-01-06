package com.thqh.wechat_miniprogram_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * @ClassName: TradeAccountLoginRequest
 * @Description:
 * @Author liubin
 * @Date 2025/1/6 17:27
 * @Version V1.0
 */


@Data
public class TradeAccountLoginRequest {
    @NotBlank(message = "交易账号不能为空")
    @JsonProperty("trade_account")
    private String tradeAccount;

    @NotBlank(message = "密码不能为空")
    private String password;

    @JsonProperty("client_ip")
    private String clientIp;

}
