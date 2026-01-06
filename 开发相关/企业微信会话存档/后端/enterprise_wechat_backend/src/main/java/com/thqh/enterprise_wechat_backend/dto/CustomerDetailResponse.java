package com.thqh.enterprise_wechat_backend.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: CustomerDetailResponse
 * @Description:
 * @Author liubin
 * @Date 2025/3/24 10:37
 * @Version V1.0
 */
@Data
public class CustomerDetailResponse {
    private int errcode;
    private String errmsg;
    private List<CustomerDetail> external_contact_list;
    private String next_cursor;
}



