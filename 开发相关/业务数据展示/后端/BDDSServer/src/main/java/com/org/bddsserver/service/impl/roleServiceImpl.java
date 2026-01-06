package com.org.bddsserver.service.impl;

import com.org.bddsserver.dto.DataAuthInfo;
import com.org.bddsserver.dto.PersonnelInfo;
import com.org.bddsserver.dto.request.roleUpdateRequest;
import com.org.bddsserver.dto.roleList;
import com.org.bddsserver.dto.response.roleListResponse;
import com.org.bddsserver.exception.BusinessException;
import com.org.bddsserver.mapper.PersonnelMapper;
import com.org.bddsserver.mapper.RoleMapper;
import com.org.bddsserver.pojo.role;
import com.org.bddsserver.pojo.sysfunctions;
import com.org.bddsserver.service.roleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/11/5
 * @Description
 */
@Service
@Transactional(rollbackFor = {BusinessException.class, SQLException.class, Exception.class})
public class roleServiceImpl implements roleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PersonnelMapper personnelMapper;


    @Override
    public roleListResponse getAllInfo() {
        roleListResponse response = new roleListResponse();
        List<roleList> roleLists = new ArrayList<>();

        // 查询出所有角色
        List<role> roles = roleMapper.getAllRole();
        // 查询出所有可授权的系统功能
        List<sysfunctions> sysfunctionsList = roleMapper.getAllSysfunctions();
        // 查询出所有可授权的可查看数据的人员信息
        List<PersonnelInfo> personnelInfoList = personnelMapper.getAllPersonnelDetail();

        // 遍历角色，获取对应的功能权限、数据访问权限
        for (role role : roles) {
            roleList roleList = new roleList();
            List<sysfunctions> authSysfunctions = new ArrayList<>(); //已授权的功能集合
            List<DataAuthInfo> authPersonnelInfo = new ArrayList<>(); // 已授权可查看数据的人员集合

            authSysfunctions = roleMapper.getSysfunctionsByRoleId(role.getId());

            // 判断功能列表中是否有数据访问权限
            for (sysfunctions sysfunction : authSysfunctions) {
                if (sysfunction.getFunction_code().equals("home")) {
                    authPersonnelInfo = roleMapper.getDataAuthInfoByRoleId(role.getId());
                    break;
                }
            }
            roleList.setRole_id(role.getId());
            roleList.setRole_name(role.getRole_name());
            roleList.setRole_code(role.getRole_code());
            roleList.setPersonnel_list(authPersonnelInfo);
            roleList.setSysfunctions_list(authSysfunctions);

            roleLists.add(roleList);
        }
        response.setRoleList(roleLists);
        response.setSysfunctionList(sysfunctionsList);
        response.setDataViewList(personnelInfoList);
        return response;
    }

    @Override
    public Boolean addNewRole(role role) {
        // 获取现有所有角色信息
        List<role> roles = roleMapper.getAllRole();
        // 判断新角色名称是否已存在
        for (role r : roles) {
            if (r.getRole_name().equals(role.getRole_name())) {
                throw new BusinessException(4005,"角色名称已存在，请重新输入！");
            }
            if (r.getRole_code().equals(role.getRole_code())) {
                throw new BusinessException(4006,"角色编码已存在，请重新输入！");
            }
        }
        Integer result = roleMapper.addNewRole(role.getRole_name(), role.getRole_code());
        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String updateRole(roleUpdateRequest roleUpdateRequest) {
        // 处理role_name的更新
        String role_name = roleUpdateRequest.getRole_name();
        Integer update_roleName_result = roleMapper.updateRoleNameByRoleId(roleUpdateRequest.getRole_id(), role_name);

        // 处理功能权限的更新
        String[] sysfunctions_code = roleUpdateRequest.getRole_func_permis_codes();

        Integer update_roleAuthFunc_result = 0;

        roleMapper.deleteRoleAuthFuncsByRoleId(roleUpdateRequest.getRole_id());
        if (sysfunctions_code.length > 0) {
            update_roleAuthFunc_result = roleMapper.updateRoleAuthFuncsByRoleId(roleUpdateRequest.getRole_id(), sysfunctions_code);
        }


        // 处理数据访问权限的更新
        String[] personnel_code = roleUpdateRequest.getRole_data_permis_person_codes();
        Integer update_roleAuthData_result = 0;
        roleMapper.deleteRoleAuthDataByRoleId(roleUpdateRequest.getRole_id());
        if (personnel_code.length > 0) {
            update_roleAuthData_result = roleMapper.updateRoleAuthDataByRoleId(roleUpdateRequest.getRole_id(), personnel_code);
        }



        String result = "本次更新情况：" + update_roleName_result + "条角色信息被更新，" + update_roleAuthFunc_result + "条功能权限更新，" + update_roleAuthData_result + "条数据权限更新。";

        return result;

    }

    @Override
    public String deleteRole(role role) {
        Integer delete_role_result = roleMapper.deleteRoleByRoleId(role.getId());
        String result =  + delete_role_result + "条角色信息被删除。";
        return result;
    }
}
