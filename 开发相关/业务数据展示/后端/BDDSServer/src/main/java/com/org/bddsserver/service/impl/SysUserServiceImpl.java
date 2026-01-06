package com.org.bddsserver.service.impl;

import com.org.bddsserver.dto.SysUserList;
import com.org.bddsserver.dto.request.SysUserRequest;
import com.org.bddsserver.exception.BusinessException;
import com.org.bddsserver.mapper.RoleMapper;
import com.org.bddsserver.mapper.SysUserMapper;
import com.org.bddsserver.pojo.role;
import com.org.bddsserver.pojo.sysUser;
import com.org.bddsserver.pojo.sysfunctions;
import com.org.bddsserver.pojo.sysuser_role_map;
import com.org.bddsserver.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author PY.Lu
 * @date 2025/10/28
 * @Description
 */
@Service
@Slf4j
@Transactional(rollbackFor = {BusinessException.class, SQLException.class, Exception.class})
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Integer validateUser(String username, String password) {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        String last_login_time = now.toString();

        sysUser result = sysUserMapper.validateUser(username, password);
        if (result == null) {
            throw new BusinessException(3002, "用户名或密码错误！");
        }else if (result.getActivate_status().equals("false")){
            throw new BusinessException(3003, "该账号已禁用，请联系管理员！");
        }else{
            sysUserMapper.updateLastLoginTime(result.getId(), last_login_time);
            return result.getId();
        }
    }

    @Override
    public SysUserList getUserDetailInfo(Integer userId) {

        SysUserList user = sysUserMapper.getUserDetailInfo(userId);
        if(user == null){
            throw new BusinessException(3004, "获取用户信息失败，请联系管理员！");
        }

        List<role> roles = roleMapper.getRolesByUserId(user.getId()); // 获取用户的角色列表

        List<String> roleNames = new ArrayList<>();
        Set<String> functionNames_set = new HashSet<>();
        for (role role : roles) {
            roleNames.add(role.getRole_name());
            List<sysfunctions> sysfunctionsList = roleMapper.getSysfunctionsByRoleId(role.getId());
            for (sysfunctions sysfunctions : sysfunctionsList) {
                functionNames_set.add(sysfunctions.getFunction_code());
            }
        }
        List<String> functionNames = new ArrayList<>(functionNames_set);
        user.setRoles(roleNames);
        user.setAuthFunctions(functionNames);
        return user;
    }

    @Override
    public List<SysUserList> getSystemUserList() {
        List<SysUserList> sysUserListResponseList = new ArrayList<>();
        List<sysUser> users = sysUserMapper.getAllUser();
        if (users == null) {
            return null;
        }
        for (sysUser user : users) {
            SysUserList sysUserList = new SysUserList();

            Integer user_id = user.getId();
            List<role> roles = roleMapper.getRolesByUserId(user_id); // 获取用户的角色列表
            List<String> roleNames = new ArrayList<>();
            for (role role : roles) {
                roleNames.add(role.getRole_name());
            }
            sysUserList.setSysUserListResponse(user, roleNames);
            sysUserListResponseList.add(sysUserList);
        }

        return sysUserListResponseList;
    }

    @Override
    public Boolean changeActivateStatusByUserId(SysUserRequest sysUserRequest) {
        Integer result = sysUserMapper.changeActivateStatusByUserId(sysUserRequest.getSysUserId(),sysUserRequest.getActivate_status());
        if (result == 1) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteSysUserByUserId(SysUserRequest sysUserRequest) {
        Integer result = sysUserMapper.deleteSysUserByUserId(sysUserRequest.getSysUserId());
        if (result == 1) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean changePasswordByUserId(SysUserRequest sysUserRequest) {
        Integer result = sysUserMapper.changePasswordByUserId(sysUserRequest.getSysUserId(),sysUserRequest.getSysUser_Password());
        if (result == 1) {
            return true;
        }
        return false;
    }

    @Override

    public Boolean addSysUser(SysUserRequest sysUserRequest) {
        // 获取当前本地日期时间
        LocalDateTime now = LocalDateTime.now();
        // sysuser
        String sysuser_name = sysUserRequest.getSysUser_Name();
        String sysuser_pwd = "123456";
        String create_at = now.toString();
        String update_at = now.toString();
        String sysuser_real_name = sysUserRequest.getSysUser_realName();
        String activate_status = "false";

        log.info("addSysUser, create_at: {}", create_at);


        List<sysuser_role_map> sysuser_role_maps = new ArrayList<>();

        Integer result_add_sysuserInfo = sysUserMapper.addSysUser(sysuser_name, sysuser_pwd, create_at, update_at, sysuser_real_name, activate_status);
        if (result_add_sysuserInfo == 1) {
            Integer new_sysuser_id = sysUserMapper.getnew_sysuser_id_by_name(sysuser_name);

            for(role role : sysUserRequest.getSysUser_Roles()) {

               sysuser_role_map sysuser_role_map = new sysuser_role_map();

               sysuser_role_map.setSysuser_id(new_sysuser_id);
               sysuser_role_map.setRole_id(role.getId());

               sysuser_role_maps.add(sysuser_role_map);
           }
            log.info("addSysUser, sysuser_role_maps: {}", sysuser_role_maps);
            Integer result_add_sysuser_role_map = roleMapper.addSysUser_Role_Map(sysuser_role_maps);
            if (result_add_sysuser_role_map >= 1) {
                return true;
            }else {
                BusinessException businessException = new BusinessException("用户角色关联失败，请联系管理员");
                throw businessException;
            }
        }else {
            BusinessException businessException = new BusinessException("新增用户失败，请联系管理员");
            throw businessException;
        }

    }

    @Override
    public Boolean deleteSysUserBatch(List<Integer> deleteIdList) {
        Integer result = sysUserMapper.deleteSysUserBatch(deleteIdList);
        if (result == deleteIdList.size()) {
            return true;
        }else{
            BusinessException businessException = new BusinessException(1004, "批量删除用户失败，请联系管理员");
            throw businessException;
        }
    }
}
