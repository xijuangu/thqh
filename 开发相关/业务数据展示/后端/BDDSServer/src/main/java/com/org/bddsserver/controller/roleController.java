package com.org.bddsserver.controller;

import com.org.bddsserver.dto.request.roleUpdateRequest;
import com.org.bddsserver.dto.response.roleListResponse;
import com.org.bddsserver.exception.BusinessException;
import com.org.bddsserver.exception.Result;
import com.org.bddsserver.pojo.role;
import com.org.bddsserver.service.roleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PY.Lu
 * @date 2025/11/5
 * @Description 角色管理控制层，负责处理系统角色的查询、新增、更新、删除等相关HTTP请求
 */
@RestController
@RequestMapping("/api/role")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class roleController {
    @Autowired
    private roleService roleService;

    /**
     * 查询所有角色完整信息接口
     * 批量查询系统中所有角色的基础信息及关联权限等数据，组装后统一返回
     *
     * @return 标准化响应结果，返回封装所有角色信息的roleListResponse对象
     */
    @RequestMapping("/getallroles")
    public Result<roleListResponse> getAllRoles() {
        roleListResponse roleList = roleService.getAllInfo();
        return Result.success(roleList);
    }

    /**
     * 新增系统角色接口
     * 接收前端传递的角色信息，完成新角色的创建入库操作
     *
     * @param role 请求体，包含新增角色的名称、权限配置等基础业务数据
     * @return 标准化响应结果，新增成功返回空数据的成功响应；失败时抛出业务异常
     */
    @RequestMapping("/addnewrole")
    public Result<T> addNewRole(@RequestBody role role) {
        log.info("新增角色请求参数:{}", role);
        Boolean result = roleService.addNewRole(role);
        if (result) {
            return Result.success();
        } else {
            throw new BusinessException("新增角色失败");
        }
    }

    /**
     * 更新角色信息接口
     * 根据角色更新请求参数，修改指定角色的基础信息及权限配置
     *
     * @param roleUpdateRequest 请求体，包含待更新角色的ID、更新后的角色信息及权限数据
     * @return 标准化响应结果，返回角色更新操作的结果提示信息
     */
    @RequestMapping("/updaterole")
    public Result<T> updateRole(@RequestBody roleUpdateRequest roleUpdateRequest) {
        log.info("更新角色信息请求参数:{}", roleUpdateRequest);
        String result = roleService.updateRole(roleUpdateRequest);
        return Result.success(result);
    }

    /**
     * 删除系统角色接口
     * 根据传入的角色信息，删除指定的系统角色记录（需处理角色关联依赖校验）
     *
     * @param role 请求体，包含待删除角色的唯一标识ID等核心数据
     * @return 标准化响应结果，返回角色删除操作的结果提示信息
     */
    @RequestMapping("/deleterole")
    public Result<T> deleteRole(@RequestBody role role) {
        log.info("删除角色请求参数:{}", role);
        String result = roleService.deleteRole(role);
        return Result.success(result);
    }
}
