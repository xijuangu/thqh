package com.org.bddsserver.controller;

import com.org.bddsserver.dto.response.DataCompareResponse;
import com.org.bddsserver.dto.request.DateRequest;
import com.org.bddsserver.dto.response.PeriodDataDetailResponse;
import com.org.bddsserver.exception.Result;
import com.org.bddsserver.service.IndicatorCompareService;
import com.org.bddsserver.service.PeriodDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author PY.Lu
 * @date 2025/10/16
 * @Description
 */
@RestController
@RequestMapping("/api/periodData")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class PeriodDataController {
    @Autowired
    private PeriodDataService periodDataService;

    @Autowired
    private IndicatorCompareService indicatorCompareService;

    /**
     * 查询周期业务数据详情,前端传入日期参数（开始时间、结束时间），后台返回周期业务数据详情
     *
     * @param dateRequest DateRequest
     * @return Result<PeriodDataDetailResponse>
     */

    @PostMapping("/queryPeriodDataDetail")
    public Result<PeriodDataDetailResponse> queryPeriodDataDetail(@RequestBody DateRequest dateRequest) {
        PeriodDataDetailResponse periodDataDetailResponse = periodDataService.getPeriodDataDetailResponse(dateRequest.getStartDate1(), dateRequest.getEndDate1(), dateRequest.getSysUserId());
        log.debug("周期业务数据查询请求参数：{}", dateRequest);
        return Result.success(periodDataDetailResponse);
    }


    /**
     * 查询周期业务指标对比数据详情,前端传入日期参数（周期1开始时间、结束时间；周期2开始时间、结束时间；查询指标），后台返回周期业务指标对比数据详情
     *
     * @param dateRequest DateRequest
     * @return Result<PeriodDataDetailResponse>
     */

    @PostMapping("/queryIndicatorCompareDetail")
    public Result<DataCompareResponse> queryIndicatorCompareDetail(@RequestBody DateRequest dateRequest) {
        log.debug("周期业务比对数据查询请求参数：{}",dateRequest);

        DataCompareResponse dataCompareResponse = indicatorCompareService.getDataCompareResponse(dateRequest);

        return Result.success(dataCompareResponse);

    }


}
