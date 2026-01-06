package org.example.processserver.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

@Data
@Component
public class t_hold_balance {
    private String busi_date;
    private String brokers_id;
    private String client_id;
    private String fund_account_id;
    private String trade_account_id;
    private String branch_id;
    private String security_id;
    private String market_id;
    private Double enable_amount;
    private Double frozen_amount;
    private Double cost;
    private Double market_value;
    private String data_source;
    private String ds_date;
    private Double begin_market_value;
    private Double begin_amount;
    private Double hand_amount;
    private Double open_amount;
    private Double open_money;
    private Double close_amount;
    private Double close_money;
    private Double hold_amount;
    private Double hold_money;
    private Double hold_profit;
    private Double today_profit;
    private Double sett_price;
    private String trade_type;
    private Double hold_profit_bydate;
    private Double hold_profit_bytrade;
    private Double close_profit;
    private Double close_profit_bydate;
    private Double close_profit_bytrade;
    private Double margin;
    private Double market_margin;
    private Double transfee;
    private Double market_transfee;
    private String money_type;
    private String bs_direction;
    private Double settfee;
    private Double market_settfee;
    private Double holdmovefee;
    private Double market_holdmovefee;
    private Double delivfee;
    private Double market_delivfee;
    private String trade_prop;
    private Double opt_marketvalue;
    private String security_code;
    private String product_id;
    private Double done_amt;
    private Double done_sum;
    private Double close_amount_today;
    private Double close_money_today;
    private String client_no;
    private Double optstrike_profit;
    private Integer id;
    private Integer encrypt_lable;
}