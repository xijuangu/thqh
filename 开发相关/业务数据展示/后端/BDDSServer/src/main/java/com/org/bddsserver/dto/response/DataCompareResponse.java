package com.org.bddsserver.dto.response;

import com.org.bddsserver.dto.IndicatorPeriodCompareDetail;
import lombok.Data;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/10/20
 * @Description 周期指标项数据比对响应类
 */
@Data
public class DataCompareResponse {
    private String period1; // 周期1区间
    private String period2; // 周期2区间
    private String compareIndicator; // 比较指标

    private List<IndicatorPeriodCompareDetail> indicatorPeriodCompareDetailList; // 数据对比明细列表
}
