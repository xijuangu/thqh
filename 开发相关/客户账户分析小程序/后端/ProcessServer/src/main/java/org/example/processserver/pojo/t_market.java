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
public class t_market {
    private String market_id;       // Corresponds to market_id (varchar(10))
    private String market_name;     // Corresponds to market_name (varchar(50))
    private String market_fname;    // Corresponds to market_fname (varchar(100))
    private String market_no;       // Corresponds to market_no (char(1))
    private String exchange_id;     // Corresponds to exchange_id (varchar(10))
    private String ds_date;         // Corresponds to ds_date (char(8))
    private String data_source;     // Corresponds to data_source (char(4))
}
