package com.thqh.enterprise_wechat_backend.dto;

import lombok.Data;
import java.util.List;

/**
 * @ClassName: CustomerListResponse
 * @Description:
 * @Author liubin
 * @Date 2025/3/24 10:36
 * @Version V1.0
 */
@Data
public class CustomerListResponse {
    private int errcode;
    private String errmsg;
    private List<String> external_userid;
}