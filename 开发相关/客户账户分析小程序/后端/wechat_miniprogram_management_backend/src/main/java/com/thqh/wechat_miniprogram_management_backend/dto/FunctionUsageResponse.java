package com.thqh.wechat_miniprogram_management_backend.dto;

import lombok.Data;

import java.util.Map;
/**
 * @ClassName: FunctionUsageResponse
 * @Description:
 * @Author liubin
 * @Date 2025/11/4 16:30
 * @Version V1.0
 */

@Data
public class FunctionUsageResponse {
    // 功能使用人数
    private Map<String, Long> functionUserCount;
    // 功能使用总次数
    private Map<String, Long> functionUseCount;
    // 功能平均使用次数
    private Map<String, Double> functionAvgUse;
    // 功能使用率
    private Map<String, Double> functionUsageRate;
}
