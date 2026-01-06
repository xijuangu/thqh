package com.thqh.wechat_miniprogram_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @ClassName: TradeAccountLoginRequest
 * @Description:
 * @Author liubin
 * @Date 2025/1/6 17:27
 * @Version V1.0
 */


@Data
public class TradeAccountStatusResponse {
    @JsonProperty("trade_account")
    private String tradeAccount;

    @JsonProperty("expiration_time")
    private String expirationTime;

}
