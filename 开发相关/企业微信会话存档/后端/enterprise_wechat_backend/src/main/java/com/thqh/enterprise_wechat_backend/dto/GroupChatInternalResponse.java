package com.thqh.enterprise_wechat_backend.dto;

import lombok.Data;
import java.util.List;

/**
 * @ClassName: GroupChatInternalResponse
 * @Description: 会话存档内部群响应体
 * @Author liubin
 * @Date 2025/3/26 17:21
 * @Version V1.0
 */
@Data
public class GroupChatInternalResponse {
    private int errcode;
    private String errmsg;
    private String roomname;
    private String creator;
    private int room_create_time;
    private String notice;
    private List<InternalMember> members;

    @Data
    public static class InternalMember {
        private String memberid;
        private int jointime;
    }
}
