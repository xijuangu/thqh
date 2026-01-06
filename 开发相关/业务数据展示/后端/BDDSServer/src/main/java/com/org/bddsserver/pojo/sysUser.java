package com.org.bddsserver.pojo;

import lombok.Data;

/**
 * @author PY.Lu
 * @date 2025/10/28
 * @Description
 */
@Data
public class sysUser {
    private Integer id;
    String sysuser_name;
    String sysuser_pwd;
    String create_at;
    String create_by;
    String update_at;
    String update_by;
    String last_login_time;
    String sysuser_real_name;
    String activate_status;
}
