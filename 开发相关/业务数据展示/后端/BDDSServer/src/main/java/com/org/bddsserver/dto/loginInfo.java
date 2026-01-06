package com.org.bddsserver.dto;

import lombok.Data;

/**
 * @author PY.Lu
 * @date 2025/10/28
 * @Description 登录信息
 */
@Data
public class loginInfo {
    private Integer sysuser_id;
    private String username;
    private String password;
}
