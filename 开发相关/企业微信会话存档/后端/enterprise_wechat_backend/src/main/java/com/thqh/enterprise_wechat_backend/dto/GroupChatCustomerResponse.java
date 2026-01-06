package com.thqh.enterprise_wechat_backend.dto;

import lombok.Data;
import java.util.List;

/**
 * @ClassName: GroupChatCustomerResponse
 * @Description: 客户群接口响应体
 * @Author liubin
 * @Date 2025/3/26 17:19
 * @Version V1.0
 */
@Data
public class GroupChatCustomerResponse {
    private int errcode;
    private String errmsg;
    private GroupChatDetail group_chat;

    @Data
    public static class GroupChatDetail {
        private String chat_id;
        private String name;
        private String owner;
        private int create_time;
        private String notice;
        private List<Member> member_list;
        private List<Admin> admin_list;
        private String member_version;

    }

    @Data
    public static class Member {
        private String userid;
        private int type;
        private int join_time;
        private int join_scene;
        private Invitor invitor;
        private String group_nickname;
        private String name;
        private String unionid;

    }

    @Data
    public static class Invitor {
        private String userid;

    }

    @Data
    public static class Admin {
        private String userid;
    }
}