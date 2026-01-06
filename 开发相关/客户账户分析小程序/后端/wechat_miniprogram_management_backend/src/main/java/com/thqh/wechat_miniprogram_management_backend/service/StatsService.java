package com.thqh.wechat_miniprogram_management_backend.service;

import com.thqh.wechat_miniprogram_management_backend.dto.*;
import com.thqh.wechat_miniprogram_management_backend.mapper.StatsMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: StatsService
 * @Description:
 * @Author liubin
 * @Date 2025/11/4 16:32
 * @Version V1.0
 */

@Service
public class StatsService {

    private final StatsMapper statsMapper;

    public StatsService(StatsMapper statsMapper) {
        this.statsMapper = statsMapper;
    }

    // ---------------- 用户统计 1~7 ----------------
    public UserStatsResponse getUserStats(UserStatsRequest request) {
        UserStatsResponse response = new UserStatsResponse();

        // 1️⃣ 累计用户数
        response.setTotalUsers(statsMapper.countTotalUsers());

        // 2️⃣ 累计疑似流失用户数（T-1）
        response.setSuspectedLostUsers(statsMapper.countSuspectedLostUsers());

        // 3️⃣ 上日活跃用户数（T-1）
        response.setYesterdayActiveUsers(statsMapper.countYesterdayActiveUsers());

        // 4️⃣ 月新增用户数
        List<Map<String, Object>> newUsers = statsMapper.countMonthlyNewUsers(request.getYear());
        response.setMonthlyNewUsers(
                newUsers.stream().collect(Collectors.toMap(
                        m -> (String) m.get("month"),
                        m -> ((Number) m.get("count")).longValue()
                ))
        );

        // 5️⃣ 月活跃用户数
        List<Map<String, Object>> activeUsers = statsMapper.countMonthlyActiveUsers(request.getYear());
        response.setMonthlyActiveUsers(
                activeUsers.stream().collect(Collectors.toMap(
                        m -> (String) m.get("month"),
                        m -> ((Number) m.get("count")).longValue()
                ))
        );

        // 6️⃣ 月交易登录活跃用户数
        List<Map<String, Object>> tradeLoginUsers = statsMapper.countMonthlyTradeLoginUsers(request.getYear());
        response.setMonthlyTradeLoginUsers(
                tradeLoginUsers.stream().collect(Collectors.toMap(
                        m -> (String) m.get("month"),
                        m -> ((Number) m.get("count")).longValue()
                ))
        );

        // 7️⃣ 交易登录活跃用户占比
        List<Map<String, Object>> tradeLoginRate = statsMapper.countMonthlyTradeLoginRate(request.getYear());
        response.setMonthlyTradeLoginRate(
                tradeLoginRate.stream().collect(Collectors.toMap(
                        m -> (String) m.get("month"),
                        m -> ((Number) m.get("rate")).longValue()
                ))
        );

        return response;
    }

    // ---------------- 功能使用统计 8~11 ----------------
    public FunctionUsageResponse getFunctionUsage(FunctionUsageRequest request) {
        FunctionUsageResponse response = new FunctionUsageResponse();

        // 根据 date 判断按月或按日查询
        // yyyy-MM
        boolean isMonth = request.getDate().length() == 7;
        // yyyy-MM-dd
        boolean isDay = request.getDate().length() == 10;

        String date = request.getDate();

        // 功能使用人数
        List<Map<String, Object>> userCountList = statsMapper.countFunctionUserCount(date, isMonth);
        response.setFunctionUserCount(
                userCountList.stream().collect(Collectors.toMap(
                        m -> (String) m.get("function_code"),
                        m -> ((Number) m.get("count")).longValue()
                ))
        );

        // 功能使用总次数
        List<Map<String, Object>> useCountList = statsMapper.countFunctionUseCount(date, isMonth);
        response.setFunctionUseCount(
                useCountList.stream().collect(Collectors.toMap(
                        m -> (String) m.get("function_code"),
                        m -> ((Number) m.get("count")).longValue()
                ))
        );

        // 功能平均使用次数
        Map<String, Double> avgUseMap = response.getFunctionUseCount().entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue() * 1.0 / response.getFunctionUserCount().getOrDefault(e.getKey(), 1L)
        ));
        response.setFunctionAvgUse(avgUseMap);

        // 功能使用率
        Long tradeLoginUsers = statsMapper.countTradeLoginUsers(date, isMonth);
        Map<String, Double> usageRateMap = response.getFunctionUserCount().entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> tradeLoginUsers > 0 ? e.getValue() * 100.0 / tradeLoginUsers : 0.0
        ));
        response.setFunctionUsageRate(usageRateMap);

        return response;
    }
}

