package com.org.bddsserver.dto.response;

import com.org.bddsserver.dto.PersonnelInfo;
import com.org.bddsserver.pojo.department;
import lombok.Data;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/11/7
 * @Description
 */
@Data
public class PersonnelDeptAll {
    private List<PersonnelInfo> personnelInfoList;
    private List<department> departmentList;
}
