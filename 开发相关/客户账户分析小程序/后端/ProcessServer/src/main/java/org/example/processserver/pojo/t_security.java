package org.example.processserver.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author PY.Lu
 * @date 2025/5/20
 * @Description
 */
@Component
@Data
public class t_security {
    private String security_id;               // Corresponds to security_id
    private String market_id;                 // Corresponds to market_id
    private String security_code;             // Corresponds to security_code
    private String security_name;             // Corresponds to security_name
    private String security_spell;            // Corresponds to security_spell
    private String status;                    // Corresponds to status
    private String security_type;             // Corresponds to security_type
    private String product_id;                // Corresponds to product_id
    private String delivery_date;             // Corresponds to delivery_date
    private String ds_date;                   // Corresponds to ds_date
    private String data_source;               // Corresponds to data_source
    private String hand_amount;               // Corresponds to hand_amount
    private String maxlimitordervolume;       // Corresponds to maxlimitordervolume
    private String trade_type;                // Corresponds to trade_type
    private String max_hold;                  // Corresponds to max_hold
    private String deliv_rule;                // Corresponds to deliv_rule
    private String min_hand;                  // Corresponds to min_hand
    private String max_hand;                  // Corresponds to max_hand
    private String money_type;                // Corresponds to money_type
    private String oper_code;                 // Corresponds to oper_code
    private String oper_date;                 // Corresponds to oper_date
    private String oper_time;                 // Corresponds to oper_time
    private String price_unit;                // Corresponds to price_unit
    private String busi_date;                 // Corresponds to busi_date
    private String if_margin_dis;             // Corresponds to if_margin_dis
    private String strike_price;              // Corresponds to strike_price
    private String option_type;               // Corresponds to option_type
    private String open_date;                 // Corresponds to open_date
    private String ref_price;                 // Corresponds to ref_price
    private String update_batch;              // Corresponds to update_batch
    private String exec_date;                 // Corresponds to exec_date
    private String expirate_date;             // Corresponds to expirate_date
    private String delivery_month;            // Corresponds to delivery_month
    private String delivery_year;             // Corresponds to delivery_year
    private String end_delivery_date;         // Corresponds to end_delivery_date
}
