package com.thqh.enterprise_wechat_backend;

import com.thqh.enterprise_wechat_backend.config.WeChatApiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author liubin
 */
@SpringBootApplication
@EnableConfigurationProperties(WeChatApiConfig.class)
public class EnterpriseWechatBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnterpriseWechatBackendApplication.class, args);
    }

}
