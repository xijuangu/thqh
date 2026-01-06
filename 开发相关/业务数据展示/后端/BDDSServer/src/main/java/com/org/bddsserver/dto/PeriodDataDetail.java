package com.org.bddsserver.dto;

import lombok.Data;

/**
 * @author PY.Lu
 * @date 2025/10/16
 * @Description
 */
@Data
public class PeriodDataDetail {
    // 人员基础信息
    PersonnelInfo personnelInfo;

    //业务统计指标
    private Integer[] new_customer_count; //新增客户数
    private Integer[] total_customer_count; //总客户数
    private Double[] avg_equity_daily; //交易日日均权益
    private Double[] initial_equity; //期初权益
    private Double[] end_equity; //期末权益
    private Double[] deposit; //入金
    private Double[] withdrawal; //出金
    private Double[] net_deposit; //净入金
    private Double[] commission_total; //汇总手续费
    private Double[] commission_retention; //留存手续费
    private Double[] profit_total; //总盈亏

    public PeriodDataDetail() {
        this.new_customer_count = new Integer[]{0, 0, 0};
        this.total_customer_count = new Integer[]{0, 0, 0};
        this.avg_equity_daily = new Double[]{0d, 0d, 0d};
        this.initial_equity = new Double[]{0d, 0d, 0d};
        this.end_equity = new Double[]{0d, 0d, 0d};
        this.deposit = new Double[]{0d, 0d, 0d};
        this.withdrawal = new Double[]{0d, 0d, 0d};
        this.net_deposit = new Double[]{0d, 0d, 0d};
        this.commission_total = new Double[]{0d, 0d, 0d};
        this.commission_retention = new Double[]{0d, 0d, 0d};
        this.profit_total = new Double[]{0d, 0d, 0d};
    }

    public void setCombineQueryResult(PeriodDataQueryResult combineQueryResult, int i) {
        this.avg_equity_daily[i] = combineQueryResult.getAvg_equity_daily();
        this.deposit[i] = combineQueryResult.getDeposit();
        this.withdrawal[i] = combineQueryResult.getWithdrawal();
        this.commission_total[i] = combineQueryResult.getCommission_total();
        this.commission_retention[i] = combineQueryResult.getCommission_retention();
        this.profit_total[i] = combineQueryResult.getProfit_total();
    }

}
