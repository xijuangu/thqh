package com.thqh.enterprise_wechat_backend.dto;

import lombok.Data;

/**
 * @ClassName: CustomerDetail
 * @Description:
 * @Author liubin
 * @Date 2025/3/24 15:21
 * @Version V1.0
 */
@Data
public class CustomerDetail {
    private CustomerExternalContact external_contact;
    private CustomerFollowInfo follow_info;
}
