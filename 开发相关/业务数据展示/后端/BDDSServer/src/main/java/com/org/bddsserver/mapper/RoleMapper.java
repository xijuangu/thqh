package com.org.bddsserver.mapper;

import com.org.bddsserver.dto.DataAuthInfo;
import com.org.bddsserver.dto.PersonnelInfo;
import com.org.bddsserver.pojo.role;
import com.org.bddsserver.pojo.sysfunctions;
import com.org.bddsserver.pojo.sysuser_role_map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/11/4
 * @Description
 */
@Mapper
public interface RoleMapper {
    List<role> getRolesByUserId(@Param("UserId") Integer UserId);

    List<role> getAllRole();

    Integer addSysUser_Role_Map(@Param("sysuserRoleMaps") List<sysuser_role_map> sysuserRoleMaps);

    List<sysfunctions> getSysfunctionsByRoleId(Integer id);

    List<DataAuthInfo> getDataAuthInfoByRoleId(Integer id);

    List<sysfunctions> getAllSysfunctions();

    Integer addNewRole(@Param("role_name")String role_name, @Param("role_code")String role_code);

    Integer updateRoleNameByRoleId(@Param("rold_id") Integer role_id, @Param("role_name") String role_name);

    Integer updateRoleAuthFuncsByRoleId(@Param("role_id") Integer role_id,@Param("function_codes") String[] sysfunctionsCode);
    Integer deleteRoleAuthFuncsByRoleId(Integer role_id);

    Integer updateRoleAuthDataByRoleId(@Param("role_id") Integer roleId,@Param("personnel_codes") String[] personnelCode);
    Integer deleteRoleAuthDataByRoleId(Integer role_id);

    Integer deleteRoleByRoleId(Integer id);
}
