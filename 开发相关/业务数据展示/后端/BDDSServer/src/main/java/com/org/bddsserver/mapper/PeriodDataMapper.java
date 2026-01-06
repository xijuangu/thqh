package com.org.bddsserver.mapper;

import com.org.bddsserver.dto.PeriodDataQueryResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author PY.Lu
 * @date 2025/10/16
 * @Description
 */
@Mapper
public interface PeriodDataMapper {
    /**
     * TODO:查询某个日期是不是交易日
     * @Param: String date 所查询交易日
     * @Return: Integer trade_flag 是否是交易日：1是交易日；0是非交易日
     */
    Integer queryTradeFlagByDate(@Param("date") String date);

    /**
     * TODO:基于所查询交易日查询上一个最近交易日
     * @Param: String date 所查询交易日
     * @Return: String date 上一个最近交易日
     */
    String queryLastTradeDate(@Param("date") String date);

    /**
     * TODO:查询周期内交易日总数
     * @Param: String start_date 开始日期
     * @Param: String end_date 结束日期
     * @Return: Integer trade_day_count 交易日总数
     */
    Integer queryTradeDayCount(@Param("start_date") String start_date, @Param("end_date") String end_date);

    /**
     * TODO:查询新增客户数
     * @Param: String[] clientIdList 客户号列表
     * @Param: String start_date 开始日期
     * @Param: String end_date 结束日期
     * @Return: Integer new_customer_count 新增客户数
     */
    Integer queryNewCustomerCount(@Param("clientIdList") String[] clientIdList,@Param("start_date") String start_date,@Param("end_date") String end_date);

    /**
     * TODO:查询总客户数
     * @Param: String[] clientIdList 客户号列表
     * @Param: String end_date 结束日期
     * @Return: Integer total_customer_count 总客户数
     */
    Integer queryTotalCustomerCount(@Param("clientIdList") String[] clientIdList,@Param("end_date") String end_date);

    /**
     * TODO:查询交易日日均权益
     * @Param: String[] clientIdList 客户号列表
     * @Param: String start_date 开始日期
     * @Param: String end_date 结束日期
     * @Param: Integer trade_day_count 交易日总数
     * @Return: Double avg_equity_daily 日均权益
     */
    Double queryAvgEquityDaily(@Param("clientIdList") String[] clientIdList,@Param("start_date") String start_date,@Param("end_date") String end_date,@Param("trade_day_count") Integer trade_day_count);

    /**
     * TODO:查询期初权益
     * @Param: String[] clientIdList 客户号列表
     * @Param: String start_date 开始日期
     * @Return: Double initial_equity 期初权益
     */
    Double queryInitialEquity(@Param("clientIdList") String[] clientIdList,@Param("start_date") String start_date);

    /**
     * TODO:查询上日结存/上日权益（上日期末权益）
     * @Param: String[] clientIdList 客户号列表
     * @Param: String start_date 开始日期
     * @Return: Double initial_equity 上日权益
     */
    Double queryPreviousDayEquity(@Param("clientIdList") String[] clientIdList,@Param("start_date") String start_date);

    /**
     * TODO:查询期末权益
     * @Param: String[] clientIdList 客户号列表
     * @Param: String end_date 结束日期
     * @Return: Double end_equity 期末权益
     */
    Double queryEndEquity(@Param("clientIdList") String[] clientIdList,@Param("end_date") String end_date);

    /**
     * TODO:查询入金
     * @Param: String[] clientIdList 客户号列表
     * @Param: String start_date 开始日期
     * @Param: String end_date 结束日期
     * @Return: Double deposit 入金
     */
    Double queryDeposit(@Param("clientIdList") String[] clientIdList,@Param("start_date") String start_date,@Param("end_date") String end_date);

    /**
     * TODO:查询出金
     * @Param: String[] clientIdList 客户号列表
     
     * @Param: String start_date 开始日期
     * @Param: String end_date 结束日期
     * @Return: Double withdrawal 出金
     */
    Double queryWithdrawal(@Param("clientIdList") String[] clientIdList,@Param("start_date") String start_date,@Param("end_date") String end_date);

    /**
     * TODO:查询汇总手续费
     * @Param: String[] clientIdList 客户号列表
     
     * @Param: String start_date 开始日期
     * @Param: String end_date 结束日期
     * @Return: Double commission_total 汇总手续费
     */
    Double queryCommissionTotal(@Param("clientIdList") String[] clientIdList,@Param("start_date") String start_date,@Param("end_date") String end_date);

    /**
     * TODO:查询留存手续费
     * @Param: String[] clientIdList 客户号列表
     
     * @Param: String start_date 开始日期
     * @Param: String end_date 结束日期
     * @Return: Double commission_retention 留存手续费
     */
    Double queryCommissionRetention(@Param("clientIdList") String[] clientIdList,@Param("start_date") String start_date,@Param("end_date") String end_date);

    /**
     * TODO:查询总盈亏
     * @Param: String[] clientIdList 客户号列表
     * @Param: String start_date 开始日期
     * @Param: String end_date 结束日期
     * @Return: Double profit_total 总盈亏
     */
    Double queryProfitTotal(@Param("clientIdList") String personnel_code,@Param("start_date") String start_date,@Param("end_date") String end_date);


    /**
     * TODO:交易日日均权益、入金、出金、汇总手续费、留存手续费、总盈亏合并查询
     * @Param: String[] clientIdList 客户号列表
     * @Param: String start_date 开始日期
     * @Param: String end_date 结束日期
     * @Param: Integer trade_day_count 交易日总数
     * @Return: PeriodDataQueryResult periodDataQueryResult 交易日日均权益、入金、出金、汇总手续费、留存手续费、总盈亏合并查询结果
     */
    PeriodDataQueryResult combineQuery(@Param("clientIdList") String[] clientIdList, @Param("start_date") String start_date, @Param("end_date") String end_date, @Param("trade_day_count") Integer trade_day_count);
}
