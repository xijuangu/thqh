package com.org.bddsserver.mapper;

import com.org.bddsserver.dto.IndicatorPeriodDataQueryResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/10/20
 * @Description
 */
@Mapper
public interface IndicatorPeriodDataMapper {
    /**
     * TODO: 获取新增客户周期指标数据对比数据
     * @Param String startDate1 周期1的开始日期
     * @Param String endDate1 周期1的结束日期
     * @Param String startDate2 周期2的开始日期
     * @Param String endDate2 周期2的结束日期
     * @Param String[] personnel_code_list 业务人员编码列表
     * @Return List<PeriodDataDetail> 周期业务详细数据
     */
    List<IndicatorPeriodDataQueryResult> queryNewCustomerIndicator(@Param("startDate1") String startDate1, @Param("endDate1") String endDate1, @Param("startDate2") String startDate2, @Param("endDate2") String endDate2, @Param("personnel_code_list") String[] personnel_code_list);

    /**
     * TODO: 获取总客户周期指标数据对比数据
     * @Param String endDate1 周期1的结束日期
     * @Param String endDate2 周期2的结束日期
     * @Param String[] personnel_code_list 业务人员编码列表
     * @Return List<PeriodDataDetail> 周期业务详细数据
     */
    List<IndicatorPeriodDataQueryResult> queryTotalCustomerIndicator(@Param("endDate1") String endDate1, @Param("endDate2") String endDate2, @Param("personnel_code_list") String[] personnel_code_list);

    /**
     * TODO: 获取期初权益数据对比数据
     * @Param String startDate1 周期1的开始日期
     * @Param String startDate2 周期2的开始日期
     * @Param String endDate1 周期1的结束日期
     * @Param String endDate2 周期2的结束日期
     * @Param String[] personnel_code_list 业务人员编码列表
     * @Return List<PeriodDataDetail> 周期业务详细数据
     */
    List<IndicatorPeriodDataQueryResult> queryEquityIndicator(@Param("startDate1") String startDate1, @Param("startDate2") String startDate2,@Param("endDate1")String endDate1,@Param("endDate2") String endDate2, @Param("personnel_code_list") String[] personnel_code_list);

    /**
     * TODO: 获取期末权益数据对比数据
     * @Param String endDate1 周期1的结束日期
     * @Param String endDate2 周期2的结束日期
     * @Param String[] personnel_code_list 业务人员编码列表
     * @Return List<PeriodDataDetail> 周期业务详细数据
     */
    List<IndicatorPeriodDataQueryResult> queryEndEquityIndicator(@Param("endDate1") String endDate1, @Param("endDate2") String endDate2, @Param("personnel_code_list") String[] personnel_code_list);

    /**
     * TODO: 获取交易日日均权益数据对比数据
     * @Param String startDate1 周期1的开始日期
     * @Param String endDate1 周期1的结束日期
     * @Param String startDate2 周期2的开始日期
     * @Param String endDate2 周期2的结束日期
     * @Param String[] personnel_code_list 业务人员编码列表
     * @Return List<PeriodDataDetail> 周期业务详细数据
     */
    List<IndicatorPeriodDataQueryResult> queryEquityDayAvgIndicator(@Param("startDate1") String startDate1, @Param("endDate1") String endDate1, @Param("startDate2") String startDate2, @Param("endDate2") String endDate2, @Param("personnel_code_list") String[] personnel_code_list);

    /**
     * TODO: 获取入金数据对比数据
     * @Param String startDate1 周期1的开始日期
     * @Param String endDate1 周期1的结束日期
     * @Param String startDate2 周期2的开始日期
     * @Param String endDate2 周期2的结束日期
     * @Param String[] personnel_code_list 业务人员编码列表
     * @Return List<PeriodDataDetail> 周期业务详细数据
     */
    List<IndicatorPeriodDataQueryResult> queryDepositIndicator(@Param("startDate1") String startDate1, @Param("endDate1") String endDate1, @Param("startDate2") String startDate2, @Param("endDate2") String endDate2, @Param("personnel_code_list") String[] personnel_code_list);

    /**
     * TODO: 获取出金数据对比数据
     * @Param String startDate1 周期1的开始日期
     * @Param String endDate1 周期1的结束日期
     * @Param String startDate2 周期2的开始日期
     * @Param String endDate2 周期2的结束日期
     * @Param String[] personnel_code_list 业务人员编码列表
     * @Return List<PeriodDataDetail> 周期业务详细数据
     */
    List<IndicatorPeriodDataQueryResult> queryWithdrawalIndicator(@Param("startDate1") String startDate1, @Param("endDate1") String endDate1, @Param("startDate2") String startDate2, @Param("endDate2") String endDate2, @Param("personnel_code_list") String[] personnel_code_list);

    /**
     * TODO: 获取净入金数据对比数据
     * @Param String startDate1 周期1的开始日期
     * @Param String endDate1 周期1的结束日期
     * @Param String startDate2 周期2的开始日期
     * @Param String endDate2 周期2的结束日期
     * @Param String[] personnel_code_list 业务人员编码列表
     * @Return List<PeriodDataDetail> 周期业务详细数据
     */
    List<IndicatorPeriodDataQueryResult> queryNetDepositIndicator(@Param("startDate1") String startDate1, @Param("endDate1") String endDate1, @Param("startDate2") String startDate2, @Param("endDate2") String endDate2, @Param("personnel_code_list") String[] personnel_code_list);

    /**
     * TODO: 获取汇总手续费数据对比数据
     * @Param String startDate1 周期1的开始日期
     * @Param String endDate1 周期1的结束日期
     * @Param String startDate2 周期2的开始日期
     * @Param String endDate2 周期2的结束日期
     * @Param String[] personnel_code_list 业务人员编码列表
     * @Return List<PeriodDataDetail> 周期业务详细数据
     */
    List<IndicatorPeriodDataQueryResult> queryTotalFeeIndicator(@Param("startDate1") String startDate1, @Param("endDate1") String endDate1, @Param("startDate2") String startDate2, @Param("endDate2") String endDate2, @Param("personnel_code_list") String[] personnel_code_list);

    /**
     * TODO: 获取留存手续费数据对比数据
     * @Param String startDate1 周期1的开始日期
     * @Param String endDate1 周期1的结束日期
     * @Param String startDate2 周期2的开始日期
     * @Param String endDate2 周期2的结束日期
     * @Param String[] personnel_code_list 业务人员编码列表
     * @Return List<PeriodDataDetail> 周期业务详细数据
     */
    List<IndicatorPeriodDataQueryResult> queryRetentionFeeIndicator(@Param("startDate1") String startDate1, @Param("endDate1") String endDate1, @Param("startDate2") String startDate2, @Param("endDate2") String endDate2, @Param("personnel_code_list") String[] personnel_code_list);

    /**
     * TODO: 获取总盈亏数据对比数据
     * @Param String startDate1 周期1的开始日期
     * @Param String endDate1 周期1的结束日期
     * @Param String startDate2 周期2的开始日期
     * @Param String endDate2 周期2的结束日期
     * @Param String[] personnel_code_list 业务人员编码列表
     * @Return List<PeriodDataDetail> 周期业务详细数据
     */
    List<IndicatorPeriodDataQueryResult> queryTotalProfitLossIndicator(@Param("startDate1") String startDate1, @Param("endDate1") String endDate1, @Param("startDate2") String startDate2, @Param("endDate2") String endDate2, @Param("personnel_code_list") String[] personnel_code_list);
}
