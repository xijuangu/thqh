package com.thqh.wechat_miniprogram_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @ClassName: GetPhoneNumberResponse
 * @Description:
 * @Author liubin
 * @Date 2025/1/2 14:15
 * @Version V1.0
 */

@Data
public class GetPhoneNumberResponse {
    private Integer errcode;
    private String errmsg;

    @JsonProperty("phone_info")
    private PhoneInfo phoneInfo;

    @Data
    public static class PhoneInfo {
        @JsonProperty("phoneNumber")
        private String phoneNumber;

        @JsonProperty("purePhoneNumber")
        private String purePhoneNumber;

        @JsonProperty("countryCode")
        private String countryCode;

        private Watermark watermark;

        @Data
        public static class Watermark {
            private Long timestamp;
            private String appid;
        }
    }
}
