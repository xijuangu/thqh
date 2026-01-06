package com.thqh.enterprise_wechat_backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;
/**
 * @ClassName: RawChatData
 * @Description:
 * @Author liubin
 * @Date 2025/3/5 17:15
 * @Version V1.0
 */
@Data
public class RawChatData {
    // 自增主键
    private Long id;
    private Long seq;
    private String msgid;
    @JsonProperty("publickey_ver")
    private int publickeyVer;
    @JsonProperty("encrypt_random_key")
    private String encryptRandomKey;
    @JsonProperty("encrypt_chat_msg")
    private String encryptChatMsg;
    private Boolean processFlag;
    private int createdBy;
    private LocalDateTime createdAt;
    private int updatedBy;
    private LocalDateTime updatedAt;
}
