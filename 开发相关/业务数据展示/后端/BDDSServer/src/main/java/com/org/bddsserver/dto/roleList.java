package com.org.bddsserver.dto;

import com.org.bddsserver.pojo.sysfunctions;
import lombok.Data;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/11/5
 * @Description 角色信息
 */
@Data
public class roleList {
    private Integer role_id;
    private String role_name;
    private String role_code;
    private List<sysfunctions> sysfunctions_list;
    private List<DataAuthInfo> personnel_list;
}
