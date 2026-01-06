package com.thqh.wechat_miniprogram_management_backend.dto;

import lombok.Data;

import java.util.Map;
/**
 * @ClassName: UserStatsResponse
 * @Description:
 * @Author liubin
 * @Date 2025/11/4 16:26
 * @Version V1.0
 */
@Data
public class UserStatsResponse {
    // 累计用户数
    private Long totalUsers;
    // 累计疑似流失用户数
    private Long suspectedLostUsers;
    // 上日活跃用户数
    private Long yesterdayActiveUsers;
    // 月新增用户数 yyyy-MM -> 数量
    private Map<String, Long> monthlyNewUsers;
    // 月活跃用户数
    private Map<String, Long> monthlyActiveUsers;
    // 月交易登录活跃用户数
    private Map<String, Long> monthlyTradeLoginUsers;
    // 月交易登录活跃用户占比
    private Map<String, Long> monthlyTradeLoginRate;
}
