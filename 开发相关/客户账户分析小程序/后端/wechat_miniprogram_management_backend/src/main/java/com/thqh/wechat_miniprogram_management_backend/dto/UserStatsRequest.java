package com.thqh.wechat_miniprogram_management_backend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: UserStatsRequest
 * @Description:
 * @Author liubin
 * @Date 2025/11/4 16:24
 * @Version V1.0
 */
@Data
public class UserStatsRequest {

    /**
     * 查询年份，例如 "2025"
     * 用于统计月新增、月活跃、月交易登录活跃等按年分组的指标
     */
    @NotBlank
    private String year;
}
