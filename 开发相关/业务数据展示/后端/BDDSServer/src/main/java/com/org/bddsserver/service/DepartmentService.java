package com.org.bddsserver.service;

import com.org.bddsserver.pojo.department;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/11/7
 * @Description
 */
public interface DepartmentService {
    List<department> getDeptInfo();

    Integer deleteDeptInfo(Integer id);

    Integer insertDeptInfo(department deptInfo);

    Integer updateDeptInfo(department deptInfo);
}
