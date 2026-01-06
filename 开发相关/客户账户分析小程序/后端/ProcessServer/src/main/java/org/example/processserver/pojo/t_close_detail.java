package org.example.processserver.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class t_close_detail {
    private String busi_date;
    private String brokers_id;
    private String market_id;
    private String done_no;
    private String open_done_no;
    private String open_date;
    private Double open_price;
    private Double close_price;
    private Double amount;
    private Double transfee;
    private Double hand_amount;
    private String fund_account_id;
    private String security_id;
    private String trade_account_id;
    private String entrust_type;
    private Double yes_sett_price;
    private Double sett_price;
    private Double close_profit_bydate;
    private Double close_profit_bytrade;
    private Double market_transfee;
    private String client_id;
    private String trade_prop;
    private String money_type;
    private String ds_date;
    private String data_source;
    private String branch_id;
    private String trade_type;
    private String bs_direction;
    private String trade_order;
    private String seat_no;
    private String deliv_date;
    private Double zr_flag;
    private Double dc_flag;
    private String security_code;
    private Double exch_rate_1t;
    private String option_type;
    private String client_name;
    private String offset_type;
    private String client_no;
    private String product_id;
    private Integer id;
    private Integer encrypt_lable;
}