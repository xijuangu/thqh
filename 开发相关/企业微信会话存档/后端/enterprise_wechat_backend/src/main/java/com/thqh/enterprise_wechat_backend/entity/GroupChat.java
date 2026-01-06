package com.thqh.enterprise_wechat_backend.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @ClassName: GroupChat
 * @Description:
 * @Author liubin
 * @Date 2025/3/27 10:45
 * @Version V1.0
 */
@Data
public class GroupChat {
    private int id;
    // chat_id或roomid
    private String chatId;
    // 群聊类型："customer"（客户群）或 "internal"（内部群）
    private String chatType;
    // 群名称
    private String name;
    // 群主或创建者
    private String owner;
    // 创建时间，Unix时间戳
    private int createTime;
    // 群公告
    private String notice;
    // 群管理员列表
    private String adminList;
    // 群成员版本
    private String memberVersion;
    //  状态（0：默认，1：关系不存在）
    private Byte status;
    // 错误日志
    private String errorLog;
    private int createdBy;
    private LocalDateTime createdAt;
    private int updatedBy;
    private LocalDateTime updatedAt;
}
