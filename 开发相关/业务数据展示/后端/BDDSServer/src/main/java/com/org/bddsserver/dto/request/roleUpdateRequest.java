package com.org.bddsserver.dto.request;

import com.org.bddsserver.dto.roleList;
import lombok.Data;

/**
 * @author PY.Lu
 * @date 2025/11/6
 * @Description 接收前端更新角色信息的请求
 */

@Data
public class roleUpdateRequest {
    // 需要更新的角色信息
    private Integer role_id;
    private String role_name;
    private String role_code;

    // 角色功能权限——功能代码列表
    private String[] role_func_permis_codes;

    //角色数据访问权限-人员代码列表
    private String[] role_data_permis_person_codes;
}
