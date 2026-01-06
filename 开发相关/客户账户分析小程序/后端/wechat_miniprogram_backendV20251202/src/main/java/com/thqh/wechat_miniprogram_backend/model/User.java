package com.thqh.wechat_miniprogram_backend.model;

import lombok.Data;

/**
 * @ClassName: User
 * @Description:
 * @Author liubin
 * @Date 2024/12/31 17:03
 * @Version V1.0
 */


@Data
public class User {
    private Long id;
    private String name;
    private String email;
}
