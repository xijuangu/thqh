package com.org.bddsserver.dto.request;

import lombok.Data;

/**
 * @author PY.Lu
 * @date 2025/10/16
 * @Description 前端home页面请求参数
 */
@Data
public class DateRequest {
    //系统登录用户id
    Integer sysUserId;
    //周期1
    String startDate1;
    String endDate1;

    //周期2
    String startDate2;
    String endDate2;

    //业务指标
    String indicator;
}
