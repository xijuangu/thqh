package com.org.bddsserver.dto;

import com.org.bddsserver.pojo.role;
import com.org.bddsserver.pojo.sysUser;
import lombok.Data;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/11/4
 * @Description 系统用户信息
 */
@Data
public class SysUserList {
    private Integer id;
    private String sysuser_name;
    private String sysuser_real_name;
    private List<String> roles;
    private List<String> authFunctions;
    private String last_login_time;
    private Boolean activate_status;

    public void setSysUserListResponse(sysUser sysuser, List<String> roles) {
        this.id = sysuser.getId();
        this.sysuser_name = sysuser.getSysuser_name();
        this.sysuser_real_name = sysuser.getSysuser_real_name();
        this.roles = roles;
        // 将String类型转换为Boolean类型
        if (sysuser.getActivate_status().equals("true")) {
            this.activate_status = true;
        } else {
            this.activate_status = false;
        }
        this.last_login_time = sysuser.getLast_login_time();



    }
}
