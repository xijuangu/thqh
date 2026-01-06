package com.org.bddsserver.service.impl;

import com.org.bddsserver.dto.PersonnelInfo;
import com.org.bddsserver.dto.response.PersonnelDeptAll;
import com.org.bddsserver.exception.BusinessException;
import com.org.bddsserver.mapper.PersonnelMapper;
import com.org.bddsserver.mapper.DepartmentMapper;
import com.org.bddsserver.pojo.department;
import com.org.bddsserver.service.PersonnelService;
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
public class PersonnelServiceImpl implements PersonnelService {
    @Autowired
    private PersonnelMapper personnelMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public PersonnelDeptAll getBusinessPersonInfo() {
        PersonnelDeptAll personnelDeptAll = new PersonnelDeptAll();
        List<PersonnelInfo> personnelInfoList = personnelMapper.getBusinessPersonInfo();
        personnelDeptAll.setPersonnelInfoList(personnelInfoList);
        List<department> departmentList = departmentMapper.getDepartmentList();
        personnelDeptAll.setDepartmentList(departmentList);
        return personnelDeptAll;
    }


    @Override
    public Integer updateBusinessPersonInfo(PersonnelInfo personnelInfo) {
        PersonnelInfo personnelName_dupilcateCheckResult = personnelMapper.personnelName_dupilcateCheck(personnelInfo);
        PersonnelInfo personnelCode_dupilcateCheckResult = personnelMapper.personnelCode_dupilcateCheck(personnelInfo);
        if (personnelName_dupilcateCheckResult != null) {
            if (!personnelName_dupilcateCheckResult.getId().equals(personnelInfo.getId())) {
                if (personnelName_dupilcateCheckResult.getPersonnel_name().equals(personnelInfo.getPersonnel_name())) {
                    throw new BusinessException("业务人员名称已存在，请重新输入");
                }
            }
        }
        if (personnelCode_dupilcateCheckResult != null) {
            if (!personnelCode_dupilcateCheckResult.getId().equals(personnelInfo.getId())) {
                if (personnelCode_dupilcateCheckResult.getPersonnel_code().equals(personnelInfo.getPersonnel_code())) {
                    throw new BusinessException("业务人员代码已存在，请重新输入");
                }
            }
        }
        Integer result = personnelMapper.updateBusinessPersonInfo(personnelInfo);
        if (result == 0) {
            throw new BusinessException("更新失败");
        }
        return result;
    }

    @Override
    public Integer deleteBusinessPersonInfo(Integer id) {
        Integer result = personnelMapper.deleteBusinessPersonInfo(id);
        if (result == 0) {
            throw new BusinessException("一条记录删除失败");
        }
        return result;
    }

    @Override
    public Integer insertBusinessPersonInfo(PersonnelInfo personnelInfo) {
        PersonnelInfo personnelName_dupilcateCheckResult = personnelMapper.personnelName_dupilcateCheck(personnelInfo);
        PersonnelInfo personnelCode_dupilcateCheckResult = personnelMapper.personnelCode_dupilcateCheck(personnelInfo);

        if (personnelName_dupilcateCheckResult != null) {
            if (personnelName_dupilcateCheckResult.getPersonnel_name().equals(personnelInfo.getPersonnel_name())) {
                throw new BusinessException("业务人员名称已存在，请重新输入");
            }
        }

        if (personnelCode_dupilcateCheckResult != null) {
            if (personnelCode_dupilcateCheckResult.getPersonnel_code().equals(personnelInfo.getPersonnel_code())) {
                throw new BusinessException("业务人员代码已存在，请重新输入");
            }
        }

        Integer result = personnelMapper.insertBusinessPersonInfo(personnelInfo);
        if (result == 0) {
            throw new BusinessException("新增失败");
        }
        return result;
    }
}
