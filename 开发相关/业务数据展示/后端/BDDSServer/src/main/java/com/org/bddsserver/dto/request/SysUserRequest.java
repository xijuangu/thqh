package com.org.bddsserver.dto.request;

import com.org.bddsserver.pojo.role;
import lombok.Data;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/11/5
 * @Description  用于接收前端传递的SysUser信息，修改或新增SysUser
 */
@Data
public class SysUserRequest {
    private Integer sysUserId; // 系统用户id

    private String sysUser_Password; // 密码

    private String sysUser_Name; // 系统用户名
    private String sysUser_realName; // 系统用户账号所属人员名称
    private List<role> sysUser_Roles; // 系统用户所属角色列表

    private String activate_status; // 账号激活状态
}
