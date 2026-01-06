package org.example.processserver.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author PY.Lu
 * @date 2024/11/8
 * @Description
 */
@Component
@Data
public class t_pub_date {
    String busi_date;
    String market_no;
    String trade_flag;

}
