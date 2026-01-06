package com.org.bddsserver.dto.response;

import com.org.bddsserver.dto.SysUserList;
import com.org.bddsserver.pojo.role;
import lombok.Data;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/11/5
 * @Description
 */
@Data
public class SysUserListResponse {
    private List<SysUserList> sysUserList;
    private List<role> allRoleList;
}
