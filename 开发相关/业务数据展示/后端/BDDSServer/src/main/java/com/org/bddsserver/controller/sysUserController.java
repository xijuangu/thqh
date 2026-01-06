package com.org.bddsserver.controller;

import com.org.bddsserver.dto.SysUserList;
import com.org.bddsserver.dto.response.SysUserListResponse;
import com.org.bddsserver.dto.request.SysUserRequest;
import com.org.bddsserver.dto.loginInfo;
import com.org.bddsserver.exception.Result;
import com.org.bddsserver.mapper.RoleMapper;
import com.org.bddsserver.pojo.role;
import com.org.bddsserver.pojo.sysUser;
import com.org.bddsserver.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/10/28
 * @Description 系统用户管理控制层，负责处理系统用户的登录验证、信息查询、状态修改、增删改、密码重置等相关HTTP请求
 */
@RestController
@RequestMapping("/api/sysUser")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class sysUserController {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 系统用户登录验证接口
     * 接收前端传递的登录账号密码，校验用户合法性并返回验证结果
     *
     * @param logininfo 登录信息请求体，包含用户名（username）和密码（password）
     * @return 标准化响应结果，返回登录验证结果标识
     */
    @RequestMapping("/login")
    public Result<Integer> login(@RequestBody loginInfo logininfo) {
        String username = logininfo.getUsername();
        String password = logininfo.getPassword();
        Integer result = sysUserService.validateUser(username, password);

        return Result.success(result);
    }

    /**
     * 查询系统用户详情信息接口
     * 根据用户ID查询指定系统用户的完整详情数据，包括用户角色信息，角色的功能权限、数据的查询权限信息
     *
     * @param logininfo 请求体，包含待查询的系统用户ID（sysuser_id）
     * @return 标准化响应结果，返回封装用户详情的SysUserList对象
     */
    @RequestMapping("/getsysuserinfodetail")
    public Result<SysUserList> getsysuserinfodetail(@RequestBody loginInfo logininfo) {
        SysUserList result = sysUserService.getUserDetailInfo(logininfo.getSysuser_id());
        return Result.success(result);

    }

    /**
     * 查询系统用户列表及所有角色信息接口
     * 查询所有系统用户数据，并关联查询全量角色信息，组装后统一返回
     *
     * @return 标准化响应结果，返回包含系统用户列表和全量角色列表的SysUserListResponse对象
     */
    @RequestMapping("/getsysuserlist")
    public Result<SysUserListResponse> getsysuserlist() {
        List<SysUserList> result = sysUserService.getSystemUserList();
        List<role> allRoleList = roleMapper.getAllRole();

        SysUserListResponse sysUserListResponse = new SysUserListResponse();
        sysUserListResponse.setSysUserList(result);
        sysUserListResponse.setAllRoleList(allRoleList);
        return Result.success(sysUserListResponse);
    }

    /**
     * 修改系统用户激活状态接口
     * 根据用户请求参数更新指定系统用户的激活/禁用状态
     *
     * @param sysUserRequest 请求体，包含待修改状态的用户ID及目标激活状态
     * @return 标准化响应结果，返回状态修改成功/失败的提示信息，失败时携带错误码3003
     */
    @RequestMapping("/activatestatuschange")
    public Result<T> activatestatuschange(@RequestBody SysUserRequest sysUserRequest) {
        Boolean result = sysUserService.changeActivateStatusByUserId(sysUserRequest);
        if (result) {
            return Result.success("激活状态修改成功");
        } else {
            return Result.success(3003, "激活状态修改失败,请联系管理员");
        }
    }

    /**
     * 删除单个系统用户接口
     * 根据用户ID删除指定的系统用户记录
     *
     * @param sysUserRequest 请求体，包含待删除的用户ID
     * @return 标准化响应结果，返回用户删除成功/失败的提示信息，失败时携带错误码3003
     */
    @RequestMapping("/deletesysuser")
    public Result<T> deletesysuser(@RequestBody SysUserRequest sysUserRequest) {
        Boolean result = sysUserService.deleteSysUserByUserId(sysUserRequest);
        if (result) {
            return Result.success("删除用户成功");
        } else {
            return Result.success(3003, "删除用户失败,请联系管理员");
        }
    }

    /**
     * 系统用户密码修改接口
     * 根据用户请求参数更新指定系统用户的登录密码
     *
     * @param sysUserRequest 请求体，包含用户ID、新密码
     * @return 标准化响应结果，返回密码修改成功/失败的提示信息，失败时携带错误码3003
     */
    @RequestMapping("/changepassword")
    public Result<T> changepassword(@RequestBody SysUserRequest sysUserRequest) {
        Boolean result = sysUserService.changePasswordByUserId(sysUserRequest);
        if (result) {
            return Result.success("密码修改成功");
        } else {
            return Result.success(3003, "密码修改失败,请联系管理员");
        }
    }

    /**
     * 新增系统用户接口
     * 新增系统用户信息，并关联绑定用户对应的角色权限
     *
     * @param sysUserRequest 请求体，包含新增用户的基础信息及关联角色列表（sysUser_Roles）
     * @return 标准化响应结果，返回用户添加成功/失败的提示信息，失败时携带错误码3003
     */
    @RequestMapping("/addsysuser")
    public Result<T> addsysuser(@RequestBody SysUserRequest sysUserRequest) {
        log.info(sysUserRequest.toString());
        Boolean result = sysUserService.addSysUser(sysUserRequest);
        log.info("addSysUser, sysUser_Roles: {}", sysUserRequest.getSysUser_Roles());
        if (result) {
            return Result.success("添加用户成功");
        }

        return Result.success(3003, "添加用户失败,请联系管理员");
    }

    /**
     * 批量删除系统用户接口
     * 根据传入的用户ID列表，批量删除多个系统用户记录
     *
     * @param deleteIdList 请求体，包含待批量删除的系统用户ID集合
     * @return 标准化响应结果，返回批量删除成功/失败的提示信息，失败时携带错误码3003
     */
    @RequestMapping("/deletesysuserbatch")
    public Result<T> deletesysuserbatch(@RequestBody List<Integer> deleteIdList) {
        Boolean result = sysUserService.deleteSysUserBatch(deleteIdList);
        if (result) {
            return Result.success("批量删除用户成功");
        }
        return Result.success(3003, "批量删除用户失败,请联系管理员");
    }

}
