package com.thqh.enterprise_wechat_backend.dto;

import lombok.Data;

/**
 * @ClassName: ChatterListQueryDTO
 * @Description:
 * @Author liubin
 * @Date 2025/3/31 10:04
 * @Version V1.0
 */
@Data
public class ChatterListQueryDTO {
    /**
     * 对话类型: "employee"、"customer"、"groupchat"
     */
    private String type;

    /**
     * 当前页码，默认为1
     */
    private int page = 1;

    /**
     * 模糊查询条件，可选
     */
    private String name;

    /**
     * 每页记录数，默认为100
     */
    private int limit = 100;

}
