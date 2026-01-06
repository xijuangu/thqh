package com.thqh.wechat_miniprogram_backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName: WeChatProperties
 * @Description:
 * @Author liubin
 * @Date 2025/1/2 14:00
 * @Version V1.0
 */
@Component
@Data
@ConfigurationProperties(prefix = "wechat")
public class WeChatProperties {
    private String appid;
    private String secret;
    private String accessTokenUrl;
    private String phoneNumberUrl;
    private String code2SessionUrl;

}

