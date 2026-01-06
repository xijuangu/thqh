package com.org.bddsserver.dto;

import lombok.Data;

/**
 * @author PY.Lu
 * @date 2025/10/23
 * @Description
 */
@Data
public class PeriodDataQueryResult {

    private Double avg_equity_daily;
    private Double deposit;
    private Double withdrawal;
    private Double commission_total;
    private Double commission_retention;
    private Double profit_total;

    //无参初始化
    public PeriodDataQueryResult() {
        this.avg_equity_daily = 0.0;
        this.deposit = 0.0;
        this.withdrawal = 0.0;
        this.commission_total = 0.0;
        this.commission_retention = 0.0;
        this.profit_total = 0.0;
    }
}
