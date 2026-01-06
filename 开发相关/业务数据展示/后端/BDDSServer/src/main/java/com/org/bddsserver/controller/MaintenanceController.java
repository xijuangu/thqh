package com.org.bddsserver.controller;

import com.org.bddsserver.dto.PersonnelInfo;
import com.org.bddsserver.dto.response.ClientClassPersonnelAll;
import com.org.bddsserver.dto.response.PersonnelDeptAll;
import com.org.bddsserver.exception.Result;
import com.org.bddsserver.pojo.ClientClassInfo;
import com.org.bddsserver.pojo.department;
import com.org.bddsserver.service.ClientClassService;
import com.org.bddsserver.service.DepartmentService;
import com.org.bddsserver.service.PersonnelService;
import com.org.bddsserver.service.uploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/10/27
 * @Description
 */
@RestController
@RequestMapping("/api/clientClassMaintenance")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class MaintenanceController {
    /**
     * 文件上传服务类，负责处理文件持久化相关业务逻辑
     */
    @Autowired
    private uploadFileService uploadFileService;

    /**
     * 客户类服务类，负责处理客户类信息的增删改查业务逻辑
     */
    @Autowired
    private ClientClassService clientClassService;

    /**
     * 人员服务类，负责处理业务人员信息的增删改查业务逻辑
     */
    @Autowired
    private PersonnelService personnelService;

    /**
     * 部门服务类，负责处理部门信息的增删改查业务逻辑
     */
    @Autowired
    private DepartmentService departmentService;

    /**
     * 文件上传接口
     * 接收前端上传的文件数组，取第一个文件进行持久化存储
     *
     * @param files 前端传递的文件数组，参数名：upLoadFileList
     * @return 标准化响应结果，返回上传成功提示
     * @throws IOException 文件处理过程中可能抛出的IO异常
     */
    @PostMapping("/upload")
    public Result<T> upload(@RequestParam("upLoadFileList") MultipartFile[] files) throws IOException {
        log.debug("upload file size: {}", files.length);
        MultipartFile multipartFile = files[0];
        uploadFileService.uploadFilePersistence(multipartFile);
        return Result.success();
    }

    /**
     * 查询所有客户类及关联人员完整信息
     *
     * @return 标准化响应结果，包含客户分类与关联业务人员信息的完整数据（ClientClassPersonnelAll）
     */
    @RequestMapping("/getAllClientClassInfo")
    public Result<ClientClassPersonnelAll> getAllClientClassInfo() {

        ClientClassPersonnelAll clientClassPersonnelAll = clientClassService.getAllClientClassInfo();
        return Result.success(clientClassPersonnelAll);
    }

    /**
     * 新增客户类信息
     *
     * @param clientClassInfo 前端传递的客户类信息请求体，包含待新增的客户类数据
     * @return 标准化响应结果，返回新增成功的记录条数提示
     */
    @PostMapping("/insertClientClassInfo")
    public Result<T> insertClientClassInfo(@RequestBody ClientClassInfo clientClassInfo) {
        log.info("新增客户分类请求参数: {}", clientClassInfo);
        Integer result = clientClassService.addClientClassInfo(clientClassInfo);
        return Result.success(result + "条记录添加成功");
    }

    /**
     * 删除客户分类信息
     *
     * @param clientClassInfo 前端传递的请求体，包含待删除客户类的ID
     * @return 标准化响应结果，返回删除成功的记录条数提示
     */
    @PostMapping("/deleteClientClassInfo")
    public Result<T> deleteClientClassInfo(@RequestBody ClientClassInfo clientClassInfo) {
        Integer result = clientClassService.deleteClientClassInfo(clientClassInfo.getId());
        return Result.success(result + "条记录删除成功");
    }

    /**
     * 更新客户分类信息
     *
     * @param clientClassInfo 前端传递的客户类信息请求体，包含待更新的客户类数据及ID
     * @return 标准化响应结果，返回更新成功的记录条数提示
     */
    @PostMapping("/updateClientClassInfo")
    public Result<T> updateClientClassInfo(@RequestBody ClientClassInfo clientClassInfo) {
        log.info("更新客户分类请求参数: {}", clientClassInfo);
        Integer result = clientClassService.updateClientClassInfo(clientClassInfo);
        return Result.success(result + "条记录更新成功");
    }

    /**
     * 查询所有业务人员及关联部门信息
     *
     * @return 标准化响应结果，包含业务人员与部门的完整关联数据（PersonnelDeptAll）
     */
    @RequestMapping("/getBusinessPersonInfo")
    public Result<PersonnelDeptAll> getBusinessPersonInfo() {
        PersonnelDeptAll personnelDeptAllList = personnelService.getBusinessPersonInfo();
        return Result.success(personnelDeptAllList);
    }

    /**
     * 删除业务人员信息
     *
     * @param personnelInfo 前端传递的请求体，包含待删除业务人员的ID
     * @return 标准化响应结果，返回删除成功的记录条数提示
     */
    @RequestMapping("/deleteBusinessPersonInfo")
    public Result<T> deleteBusinessPersonInfo(@RequestBody PersonnelInfo personnelInfo) {
        log.info("删除业务人员请求参数: {}", personnelInfo);
        Integer result = personnelService.deleteBusinessPersonInfo(personnelInfo.getId());

        return Result.success(result + "条记录删除成功");
    }

    /**
     * 新增业务人员信息
     *
     * @param personnelInfo 前端传递的业务人员信息请求体，包含待新增的业务人员数据
     * @return 标准化响应结果，返回新增成功的记录条数提示
     */
    @RequestMapping("/insertBusinessPersonInfo")
    public Result<T> insertBusinessPersonInfo(@RequestBody PersonnelInfo personnelInfo) {
        log.info("新增业务人员请求参数: {}", personnelInfo);
        Integer result = personnelService.insertBusinessPersonInfo(personnelInfo);
        return Result.success(result + "条记录添加成功");
    }

    /**
     * 更新业务人员信息
     *
     * @param personnelInfo 前端传递的业务人员信息请求体，包含待更新的业务人员数据及ID
     * @return 标准化响应结果，返回更新成功的记录条数提示
     */
    @RequestMapping("/updateBusinessPersonInfo")
    public Result<T> updateBusinessPersonInfo(@RequestBody PersonnelInfo personnelInfo) {
        log.info("业务人员更新请求参数: {}", personnelInfo);
        Integer result = personnelService.updateBusinessPersonInfo(personnelInfo);
        return Result.success(result + "条记录更新成功");
    }

    /**
     * 查询所有部门信息
     *
     * @return 标准化响应结果，包含所有部门的信息列表（List<department>）
     */
    @RequestMapping("/getDeptInfo")
    public Result<List<department>> getDeptInfo() {
        log.info("查询部门信息");
        List<department> deptInfoList = departmentService.getDeptInfo();
        return Result.success(deptInfoList);
    }

    /**
     * 删除部门信息
     *
     * @param deptInfo 前端传递的请求体，包含待删除部门的ID
     * @return 标准化响应结果，返回删除成功的记录条数提示
     */
    @RequestMapping("/deleteDeptInfo")
    public Result<T> deleteDeptInfo(@RequestBody department deptInfo) {
        log.info("删除部门请求参数: {}", deptInfo);
        Integer result = departmentService.deleteDeptInfo(deptInfo.getId());
        return Result.success(result + "条记录删除成功");
    }

    /**
     * 新增部门信息
     *
     * @param deptInfo 前端传递的部门信息请求体，包含待新增的部门数据
     * @return 标准化响应结果，返回新增成功的记录条数提示
     */
    @RequestMapping("/insertDeptInfo")
    public Result<T> insertDeptInfo(@RequestBody department deptInfo) {
        log.info("新增部门请求参数: {}", deptInfo);
        Integer result = departmentService.insertDeptInfo(deptInfo);
        return Result.success(result + "条记录添加成功");
    }

    /**
     * 更新部门信息
     *
     * @param deptInfo 前端传递的部门信息请求体，包含待更新的部门数据及ID
     * @return 标准化响应结果，返回更新成功的记录条数提示
     */
    @RequestMapping("/updateDeptInfo")
    public Result<T> updateDeptInfo(@RequestBody department deptInfo) {
        log.info("更新部门信息请求参数: {}", deptInfo);
        Integer result = departmentService.updateDeptInfo(deptInfo);
        return Result.success(result + "条记录更新成功");
    }




}
