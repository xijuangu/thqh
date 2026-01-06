package com.org.bddsserver.service;

import com.org.bddsserver.dto.SysUserList;
import com.org.bddsserver.dto.request.SysUserRequest;
import com.org.bddsserver.pojo.sysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/10/28
 * @Description
 */
public interface SysUserService {
    Integer validateUser(@Param("username") String username,@Param("password") String password);
    SysUserList getUserDetailInfo(@Param("userId") Integer userId);
    List<SysUserList> getSystemUserList();

    Boolean changeActivateStatusByUserId(SysUserRequest sysUserRequest);

    Boolean deleteSysUserByUserId(SysUserRequest sysUserRequest);

    Boolean changePasswordByUserId(SysUserRequest sysUserRequest);

    Boolean addSysUser(SysUserRequest sysUserRequest);

    Boolean deleteSysUserBatch(List<Integer> deleteIdList);
}
