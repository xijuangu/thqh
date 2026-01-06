package com.thqh.enterprise_wechat_backend.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: Customer
 * @Description:
 * @Author liubin
 * @Date 2025/3/24 15:42
 * @Version V1.0
 */
@Data
public class Customer {
    private int id;
    //基本信息
    // 外部联系人ID
    private String externalUserId;
    // 客户姓名
    private String name;
    // 类型
    private Byte type;
    // 头像
    private String avatar;
    // 企业简称
    private String corpName;
    // 企业全称
    private String corpFullName;
    // 性别
    private Byte gender;
    // 职位
    private String position;
    // unionid
    private String unionid;
    // 以下字段来自跟进信息
    // 跟进的企业成员ID
    private String followUserid;
    // 备注
    private String followRemark;
    // 描述
    private String followDescription;
    // 跟进添加时间（时间戳）
    private Long followCreatetime;
    // 标签ID集合（逗号分隔）
    private String followTagIds;
    // 备注的手机号码集合（逗号分隔）
    private String followRemarkMobiles;
    // 操作员ID
    private String followOperUserid;
    // 添加方式
    private Byte followAddWay;
    // 状态（0：默认，1：关系不存在）
    private Byte status;
    private int createdBy;
    private LocalDateTime createdAt;
    private int updatedBy;
    private LocalDateTime updatedAt;
}
