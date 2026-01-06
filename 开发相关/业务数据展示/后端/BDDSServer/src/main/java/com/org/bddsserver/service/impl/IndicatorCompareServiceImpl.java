package com.org.bddsserver.service.impl;

import com.org.bddsserver.dto.*;
import com.org.bddsserver.dto.request.DateRequest;
import com.org.bddsserver.dto.response.DataCompareResponse;
import com.org.bddsserver.exception.BusinessException;
import com.org.bddsserver.mapper.IndicatorPeriodDataMapper;
import com.org.bddsserver.mapper.PersonnelMapper;
import com.org.bddsserver.service.IndicatorCompareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author PY.Lu
 * @date 2025/10/20
 * @Description
 */
@Service
@Slf4j
public class IndicatorCompareServiceImpl implements IndicatorCompareService {
    @Autowired
    private PersonnelMapper personnelMapper;

    @Autowired
    private IndicatorPeriodDataMapper indicatorPeriodDataMapper;


    @Override
    public DataCompareResponse getDataCompareResponse(DateRequest dateRequest) {
        // 前端查询的指标是：
        String indicator = dateRequest.getIndicator();

        // 前端查询的两个周期的开始时间和结束时间是
        String startDate1 = dateRequest.getStartDate1();
        String startDate2 = dateRequest.getStartDate2();
        String endDate1 = dateRequest.getEndDate1();
        String endDate2 = dateRequest.getEndDate2();

        //需要基于登录用户的数据查看权限拉取对应的业务人员信息
        List<PersonnelInfo> personnelInfoList = personnelMapper.getPersonnelDetailBySysUserId(dateRequest.getSysUserId());

        if (personnelInfoList == null || personnelInfoList.isEmpty()) {
            throw new BusinessException(1001, "没有查询到业务人员信息,请与业务人员客户类关系表维护人员或管理员联系，尽快更新业务人员客户类关系表！");
        }

        // 声明指标项数据比对响应体对象
        DataCompareResponse dataCompareResponse = new DataCompareResponse();

        List<IndicatorPeriodCompareDetail> indicatorPeriodCompareDetailList = new ArrayList<>();

        Map<String, IndicatorPeriodCompareDetail> map = new HashMap<>();
        for (PersonnelInfo personnelInfo : personnelInfoList) {
            log.info("当前循环查询的业务人员信息：{}",personnelInfo);
            if (personnelInfo == null){
                continue;
            }

            IndicatorPeriodCompareDetail indicatorPeriodCompareDetail = new IndicatorPeriodCompareDetail();
            indicatorPeriodCompareDetail.setPersonnelInfo(personnelInfo);
            map.put(personnelInfo.getPersonnel_code(), indicatorPeriodCompareDetail);
        }

        // 初始化数组，长度与列表大小一致
        String[] personnelCodeList = new String[personnelInfoList.size()];
        int index_personnel = 0;

        // 遍历列表，将业务人员编码存入数组
        for (PersonnelInfo personnelInfo : personnelInfoList) {
            if (personnelInfo == null){
                continue;
            }
            personnelCodeList[index_personnel++] = personnelInfo.getPersonnel_code();
        }
        log.info(personnelInfoList.toString());

        // 存储查询结果的对象
        List<IndicatorPeriodDataQueryResult> indicatorPeriodDataQueryResult = new ArrayList<>();

        if (indicator.equals("新增客户数")) {
            indicatorPeriodDataQueryResult = indicatorPeriodDataMapper.queryNewCustomerIndicator(startDate1, endDate1, startDate2, endDate2, personnelCodeList);
        }else if (indicator.equals("总客户数")) {
            indicatorPeriodDataQueryResult = indicatorPeriodDataMapper.queryTotalCustomerIndicator(endDate1, endDate2, personnelCodeList);
        }else if (indicator.equals("期初权益")) {
            indicatorPeriodDataQueryResult = indicatorPeriodDataMapper.queryEquityIndicator(startDate1, startDate2, endDate1, endDate2, personnelCodeList);
        }else if (indicator.equals("期末权益")) {
            indicatorPeriodDataQueryResult = indicatorPeriodDataMapper.queryEndEquityIndicator(endDate1, endDate2, personnelCodeList);
        }else if (indicator.equals("交易日日均权益")) {
            indicatorPeriodDataQueryResult = indicatorPeriodDataMapper.queryEquityDayAvgIndicator(startDate1, endDate1, startDate2, endDate2, personnelCodeList);
        }else if (indicator.equals("入金金额")) {
            indicatorPeriodDataQueryResult = indicatorPeriodDataMapper.queryDepositIndicator(startDate1, endDate1, startDate2, endDate2, personnelCodeList);
        }else if (indicator.equals("出金金额")) {
            indicatorPeriodDataQueryResult = indicatorPeriodDataMapper.queryWithdrawalIndicator(startDate1, endDate1, startDate2, endDate2, personnelCodeList);
        }else if (indicator.equals("净入金")) {
            indicatorPeriodDataQueryResult = indicatorPeriodDataMapper.queryNetDepositIndicator(startDate1, endDate1, startDate2, endDate2, personnelCodeList);
        }else if (indicator.equals("总手续费")) {
            indicatorPeriodDataQueryResult = indicatorPeriodDataMapper.queryTotalFeeIndicator(startDate1, endDate1, startDate2, endDate2, personnelCodeList);
        }else if (indicator.equals("留存手续费")) {
            indicatorPeriodDataQueryResult = indicatorPeriodDataMapper.queryRetentionFeeIndicator(startDate1, endDate1, startDate2, endDate2, personnelCodeList);
        }else if (indicator.equals("总盈亏")) {
            indicatorPeriodDataQueryResult = indicatorPeriodDataMapper.queryTotalProfitLossIndicator(startDate1, endDate1, startDate2, endDate2, personnelCodeList);
        }


        // 遍历查询结果，将结果封装到指标项数据比对响应体对象中
        for (IndicatorPeriodDataQueryResult queryList : indicatorPeriodDataQueryResult) {
            IndicatorPeriodCompareDetail detail = map.get(queryList.getPersonnel_code());
            detail.setData(queryList.getClient_type(), queryList.getPeriod1(), queryList.getPeriod2());
        }

        // 2. 将 Map 中所有的 value（IndicatorPeriodCompareDetail 对象）添加到 List
        indicatorPeriodCompareDetailList.addAll(map.values());

        String period1 = startDate1 + "-" + endDate1;
        String period2 = startDate2 + "-" + endDate2;

        dataCompareResponse.setPeriod1(period1);
        dataCompareResponse.setPeriod2(period2);
        dataCompareResponse.setCompareIndicator(indicator);
        dataCompareResponse.setIndicatorPeriodCompareDetailList(indicatorPeriodCompareDetailList);

        return dataCompareResponse;
    }
}
