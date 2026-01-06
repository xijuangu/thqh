package com.thqh.enterprise_wechat_backend.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName: WeChatApiConfig
 * @Description:
 * @Author liubin
 * @Date 2025/4/22 17:29
 * @Version V1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat.qyapi")
public class WeChatApiConfig {

    private String corpid;

    private Secret secret;

    private Url url;

    @Data
    public static class Secret {
        @JsonProperty("self-built-application")
        private String selfBuiltApplication;

        @JsonProperty("address-book")
        private String addressBook;

        @JsonProperty("session-archive")
        private String sessionArchive;
    }

    @Data
    public static class Url {
        @JsonProperty("token")
        private String token;

        @JsonProperty("user-list")
        private String userList;

        @JsonProperty("user-detail")
        private String userDetail;

        @JsonProperty("msgaudit-user-list")
        private String msgauditUserList;

        @JsonProperty("customer-detail")
        private String customerDetail;

        @JsonProperty("customer-list")
        private String customerList;

        @JsonProperty("customer-groupchat-detail")
        private String customerGroupchatDetail;

        @JsonProperty("msgaudit-groupchat-detail")
        private String msgauditGroupchatDetail;
    }
}
