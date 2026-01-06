package com.org.bddsserver.dto;

/**
 * @author PY.Lu
 * @date 2025/10/20
 * @Description
 */

import lombok.Data;

@Data
public class PersonnelInfo {
    //人员基础信息
    private Integer id;
    private String personnel_code;
    private String personnel_name;
    private String department_code;
    private String department_name;

    private String[] clientCodeList;
    private String[] clientIdList;

    // 无参构造方法
    public PersonnelInfo() {
        this.id = 0;
        this.personnel_code = "";
        this.personnel_name = "";
        this.department_code = "";
        this.department_name = "";

        this.clientCodeList = new String[0];
        this.clientIdList = new String[0];
    }


}
