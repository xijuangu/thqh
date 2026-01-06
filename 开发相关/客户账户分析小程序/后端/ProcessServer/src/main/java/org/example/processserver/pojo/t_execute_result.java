package org.example.processserver.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class t_execute_result {
    private String busi_date; // 交易日=tx_date
    private String client_id; // 客户号=cust_no
    private String client_name; // 客户名称=cust_name
    private Double execute_no; // 行权号=EXECUTE_NO
    private String market_id; // 交易所=EXCH_CODE
    private String seat_no; // 席位号=SEAT_NO
    private String trade_account_id; // 交易账户=TX_NO
    private String security_code; // 合约代码=CONTRACT_CODE
    private String security_id; // 标的=VARI_CODE
    private String deliv_date; // 交割期=DELIV_DATE
    private Double strike_price; // 行权价=STRIKE_PRICE
    private String option_type; // 期权类型=OPTION_TYPE（1305）
    private String bs_direction; // 买卖方向=BS_FLAG
    private String trade_prop; // 投保标记=SH_FLAG
    private String money_type; // 币别=CURRENCY_CODE
    private Double strike_qty; // 行权数量=EXECUTE_QTY
    private Double deliv_price; // 交割价=DELIV_PRICE
    private Double optstrike_profit; // 期权执行盈亏=EXECUTE_PROFIT
    private Double strikefee; // 手续费=EXECUTE_COMMI
    private Double market_strikefee; // 上交手续费=EXCH_EXECUTE_COMMI
    private Double strikefee_rate; // 手续费率=EXECUTE_COMMI_RATE
    private Double strikefee_amt; // 手续费金额=EXECUTE_COMMI_AMT
    private Double market_strikefee_rate; // 交易所手续费率=EXCH_EXECUTE_COMMI_RATE
    private Double market_strikefee_amt; // 交易所手续费金额=EXCH_EXECUTE_COMMI_AMT
    private Double hand_amount; // 合约乘数=HANDS
    private String note; // 备注=NOTE
    private String deliv_type; // 交割类型=DELIV_MOD（1258）
    private String ds_date; // 采集日期
    private String data_source; // 采集源头
    private String fund_account_id; // 资金账户
    private String branch_id; // 分支机构
    private String trade_type; // 期权类型
    private Double strikefrozenmargin; // 投资者冻结保证金
    private Double exchstrikefrozenmargin; // 交易所冻结保证金
    private String strike_type; // 行权类型
    private String client_no; // 统一客户号
    private Double execute_transfee; // 行权手续费
    private Double market_execute_transfee; // 上交行权手续费
    private Double market_performfee; // 上交履约手续费
    private Double performfee; // 履约手续费
    private String product_id; // 产品ID
    private Integer id; // 表主键
    private Integer encrypt_lable; // 备注 是否经过加密处理标识：1是，0否
}