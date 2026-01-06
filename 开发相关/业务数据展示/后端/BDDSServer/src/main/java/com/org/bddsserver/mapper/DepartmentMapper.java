package com.org.bddsserver.mapper;

import com.org.bddsserver.pojo.department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/11/7
 * @Description
 */
@Mapper
public interface DepartmentMapper {
    List<department> getDepartmentList();

    Integer deleteDepartment(Integer id);

    Integer insertDepartment(@Param("deptInfo") department deptInfo);

    Integer updateDepartment(@Param("deptInfo") department deptInfo);
}
