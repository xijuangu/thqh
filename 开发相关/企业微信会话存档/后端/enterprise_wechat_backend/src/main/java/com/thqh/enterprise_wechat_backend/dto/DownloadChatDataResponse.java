package com.thqh.enterprise_wechat_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thqh.enterprise_wechat_backend.entity.RawChatData;
import lombok.Data;
import java.util.List;

/**
 * @ClassName: DownloadChatDataRequest
 * @Description:
 * @Author liubin
 * @Date 2025/3/4 15:50
 * @Version V1.0
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // 忽略未知字段，避免解析失败
public class DownloadChatDataResponse {
    private int errcode;
    private String errmsg;

    @JsonProperty("chatdata")
    private List<RawChatData> rawChatData;
}