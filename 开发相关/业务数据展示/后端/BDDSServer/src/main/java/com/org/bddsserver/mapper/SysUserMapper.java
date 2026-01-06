package com.org.bddsserver.mapper;

import com.org.bddsserver.dto.SysUserList;
import com.org.bddsserver.pojo.sysUser;
import com.org.bddsserver.pojo.sysuser_role_map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/10/28
 * @Description
 */
@Mapper
public interface SysUserMapper {
    sysUser validateUser(@Param("username") String username, @Param("password") String password);

    List<sysUser> getAllUser();

    Integer changeActivateStatusByUserId(@Param("sysUserId") Integer sysUserId, @Param("activateStatus") String activateStatus);

    Integer deleteSysUserByUserId(Integer sysUserId);

    Integer changePasswordByUserId(@Param("sysUserId") Integer sysUserId,@Param("sysUserPassword") String sysUserPassword);



    Integer getnew_sysuser_id_by_name(@Param("sysuserName") String sysuserName);

    Integer addSysUser(@Param("sysuserName") String sysuserName,@Param("sysuserPwd") String sysuserPwd,@Param("createAt") String createAt,@Param("updateAt") String updateAt,@Param("sysuserRealName") String sysuserRealName,@Param("activateStatus") String activateStatus);

    Integer deleteSysUserBatch(List<Integer> deleteIdList);

    void updateLastLoginTime(@Param("id") Integer id,@Param("lastLoginTime") String lastLoginTime);

    SysUserList getUserDetailInfo(Integer userId);
}
