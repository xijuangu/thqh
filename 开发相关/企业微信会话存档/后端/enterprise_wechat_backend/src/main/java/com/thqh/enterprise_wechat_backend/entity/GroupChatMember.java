package com.thqh.enterprise_wechat_backend.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: GroupChatMember
 * @Description:
 * @Author liubin
 * @Date 2025/3/27 10:45
 * @Version V1.0
 */
@Data
public class GroupChatMember {
    private int id;
    // 关联 group_chat.chat_id
    private String chatId;
    // 群成员ID
    private String userid;
    // 成员类型
    private int type;
    // 外部联系人在微信开放平台的唯一身份标识
    private String unionid;
    // 入群时间（Unix 时间戳）
    private int joinTime;
    // 入群方式
    private int joinScene;
    // 邀请者ID
    private String invitorUserid;
    // 群内昵称
    private String groupNickname;
    // 成员姓名
    private String memberName;
    // 是否为管理员：1-是；0-否
    private Byte isAdmin;
    //  状态（0：默认，1：关系不存在）
    private Byte status;
    private int createdBy;
    private LocalDateTime createdAt;
    private int updatedBy;
    private LocalDateTime updatedAt;
}
