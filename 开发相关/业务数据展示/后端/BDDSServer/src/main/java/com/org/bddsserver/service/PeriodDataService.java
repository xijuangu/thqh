package com.org.bddsserver.service;

import com.org.bddsserver.dto.response.PeriodDataDetailResponse;

/**
 * @author PY.Lu
 * @date 2025/10/16
 * @Description
 */

public interface PeriodDataService {
    /**
     * TODO: 获取业务人员基础信息及其周期业务详细数据
     * @Param String startDate 开始日期
     * @Param String endDate 结束日期
     * @Param Integer sysUserId 用戶id
     * @Return List<PeriodDataDetailResponse> 周期业务详细数据
     */
    PeriodDataDetailResponse getPeriodDataDetailResponse(String startDate, String endDate, Integer sysUserId);

}
