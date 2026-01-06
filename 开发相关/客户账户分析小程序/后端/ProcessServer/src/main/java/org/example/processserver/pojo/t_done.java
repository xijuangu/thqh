package org.example.processserver.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class t_done {
    private Integer id;
    private Integer encrypt_lable;

    //加密字段
    private String FUND_ACCOUNT_ID;//资金账户
    private String TRADE_ACCOUNT_ID;//交易账户
    private String CLIENT_ID;


}
