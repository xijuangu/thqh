package com.org.bddsserver.service.impl;

import com.org.bddsserver.exception.BusinessException;
import com.org.bddsserver.mapper.DepartmentMapper;
import com.org.bddsserver.pojo.department;
import com.org.bddsserver.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/11/7
 * @Description
 */
@Service
@Transactional(rollbackFor = {BusinessException.class, SQLException.class, Exception.class})
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<department> getDeptInfo() {
        return departmentMapper.getDepartmentList();
    }

    @Override
    public Integer deleteDeptInfo(Integer id) {
        Integer result = departmentMapper.deleteDepartment(id);
        if (result == 0) {
            throw new BusinessException("删除失败");
        }
        return result;
    }

    @Override
    public Integer insertDeptInfo(department deptInfo) {
        List<department> deptList = departmentMapper.getDepartmentList();
        for (department dept : deptList) {
            if (dept.getDepartment_name().equals(deptInfo.getDepartment_name()))
                throw new BusinessException("无法添加已存在的部门名称");
            else if (dept.getDepartment_code().equals(deptInfo.getDepartment_code())) {
                throw new BusinessException("无法添加已存在的部门代码");
            }
        }
        return departmentMapper.insertDepartment(deptInfo);
    }

    @Override
    public Integer updateDeptInfo(department deptInfo) {

        List<department> deptList = departmentMapper.getDepartmentList();
        for (department dept : deptList) {
            if (dept.getId() == deptInfo.getId()) {

            } else {
                if (dept.getDepartment_name().equals(deptInfo.getDepartment_name()) && !dept.getDepartment_code().equals(deptInfo.getDepartment_code()))
                    throw new BusinessException("无法更新为已存在的部门名称");
                else if (!dept.getDepartment_name().equals(deptInfo.getDepartment_name()) && dept.getDepartment_code().equals(deptInfo.getDepartment_code())) {
                    throw new BusinessException("无法更新为已存在的部门代码");
                }
            }

        }

        return departmentMapper.updateDepartment(deptInfo);
    }

}
