package com.thqh.wechat_miniprogram_management_backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author liubin
 */

@Mapper
public interface StatsMapper {

    // ---------------- 用户统计 1~7 ----------------
    Long countTotalUsers();

    Long countSuspectedLostUsers();

    Long countYesterdayActiveUsers();

    List<Map<String, Object>> countMonthlyNewUsers(@Param("year") String year);

    List<Map<String, Object>> countMonthlyActiveUsers(@Param("year") String year);

    List<Map<String, Object>> countMonthlyTradeLoginUsers(@Param("year") String year);

    List<Map<String, Object>> countMonthlyTradeLoginRate(@Param("year") String year);

    // ---------------- 功能使用统计 8~11 ----------------
    List<Map<String, Object>> countFunctionUserCount(@Param("date") String date, @Param("isMonth") boolean isMonth);

    List<Map<String, Object>> countFunctionUseCount(@Param("date") String date, @Param("isMonth") boolean isMonth);

    Long countTradeLoginUsers(@Param("date") String date, @Param("isMonth") boolean isMonth);
}
