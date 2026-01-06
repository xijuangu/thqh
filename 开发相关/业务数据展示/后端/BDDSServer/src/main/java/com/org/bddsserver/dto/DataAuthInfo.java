package com.org.bddsserver.dto;

import lombok.Data;

/**
 * @author PY.Lu
 * @date 2025/11/6
 * @Description 存储某角色已授权可查看数据对应的部门和人员信息
 */
@Data
public class DataAuthInfo {
    private Integer id;
    private Integer role_id;
    private String department_code;
    private String personnel_code;
}
