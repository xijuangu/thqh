package com.thqh.enterprise_wechat_backend.dto;

import lombok.Data;
import java.util.List;

/**
 * @ClassName: PermitUserListResponse
 * @Description:
 * @Author liubin
 * @Date 2025/3/21 14:58
 * @Version V1.0
 */
@Data
public class PermitUserListResponse {
    private int errcode;
    private String errmsg;
    private List<String> ids;
}
