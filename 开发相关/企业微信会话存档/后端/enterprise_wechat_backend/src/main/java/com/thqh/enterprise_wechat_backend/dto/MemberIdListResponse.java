package com.thqh.enterprise_wechat_backend.dto;

import lombok.Data;
import java.util.List;

/**
 * @ClassName: MemberIdListResponse
 * @Description:
 * @Author liubin
 * @Date 2025/3/20 15:57
 * @Version V1.0
 */
@Data
public class MemberIdListResponse {
    private int errcode;
    private String errmsg;
    private String next_cursor;
    private List<DeptUser> dept_user;
}
