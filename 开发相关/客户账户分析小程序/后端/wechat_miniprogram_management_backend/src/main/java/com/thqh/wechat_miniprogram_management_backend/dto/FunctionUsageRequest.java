package com.thqh.wechat_miniprogram_management_backend.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * @ClassName: FunctionUsageRequest
 * @Description:
 * @Author liubin
 * @Date 2025/11/4 16:25
 * @Version V1.0
 */
@Data
public class FunctionUsageRequest {

    /**
     * 查询时间：
     * - 按月："yyyy-MM"
     * - 按日："yyyy-MM-dd"
     */
    @NotBlank
    private String date;
}
