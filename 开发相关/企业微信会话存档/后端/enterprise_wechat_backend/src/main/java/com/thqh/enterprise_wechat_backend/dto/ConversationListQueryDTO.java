package com.thqh.enterprise_wechat_backend.dto;

import lombok.Data;

/**
 * @ClassName: ChatterListQueryDTO
 * @Description:
 * @Author liubin
 * @Date 2025/4/7 10:04
 * @Version V1.0
 */
@Data
public class ConversationListQueryDTO {
    // 会话发起方（employee 或 customer）
    private String from;
    // 会话发起方的 ID
    private String id;
    // 会话发起方的聊天列表类型（employee、customer、groupchat）
    private String to;
    /**
     * 当前页码，默认为1
     */
    private int page = 1;
    /**
     * 每页记录数，默认为100
     */
    private int limit = 100;

    private String name;

    private Boolean hasConversation;

}