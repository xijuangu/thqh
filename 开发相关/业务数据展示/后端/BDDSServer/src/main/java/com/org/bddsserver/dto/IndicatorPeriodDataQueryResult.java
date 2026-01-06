package com.org.bddsserver.dto;

import lombok.Data;

/**
 * @author PY.Lu
 * @date 2025/10/20
 * @Description 某业务人员某业务类型某指标某时间段的指标数据查询结果
 */
@Data
public class IndicatorPeriodDataQueryResult {
    private String personnel_code;
    String client_type;
    private Double period1;
    private Double period2;
}
