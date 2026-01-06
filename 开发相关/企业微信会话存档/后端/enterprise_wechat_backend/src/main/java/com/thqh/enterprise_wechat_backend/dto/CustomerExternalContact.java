package com.thqh.enterprise_wechat_backend.dto;

import lombok.Data;

/**
 * @ClassName: CustomerExternalContact
 * @Description:
 * @Author liubin
 * @Date 2025/3/24 15:28
 * @Version V1.0
 */
@Data
public class CustomerExternalContact {
    private String external_userid;
    private String name;
    private int type;
    private String avatar;
    private String corp_name;
    private String corp_full_name;
    private int gender;
    private String position;
    private String unionid;
}
