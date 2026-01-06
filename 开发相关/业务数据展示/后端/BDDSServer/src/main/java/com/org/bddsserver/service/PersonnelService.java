package com.org.bddsserver.service;

import com.org.bddsserver.dto.PersonnelInfo;
import com.org.bddsserver.dto.response.PersonnelDeptAll;
import com.org.bddsserver.pojo.department;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/11/7
 * @Description
 */

public interface PersonnelService {
    PersonnelDeptAll getBusinessPersonInfo();

    Integer updateBusinessPersonInfo(PersonnelInfo personnelInfo);

    Integer deleteBusinessPersonInfo(Integer id);

    Integer insertBusinessPersonInfo(PersonnelInfo personnelInfo);

}
