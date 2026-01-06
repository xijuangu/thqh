package org.example.processserver.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class encrypt_field {
    private int id;
    //加密字段
    private String fund_account_id;//资金账户
    private String trade_account_id;//交易账户
    private String client_id;


}
