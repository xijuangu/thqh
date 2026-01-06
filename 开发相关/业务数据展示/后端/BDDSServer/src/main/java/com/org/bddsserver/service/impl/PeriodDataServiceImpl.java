package com.org.bddsserver.service.impl;

import com.org.bddsserver.dto.PeriodDataDetail;
import com.org.bddsserver.dto.response.PeriodDataDetailResponse;
import com.org.bddsserver.dto.PeriodDataQueryResult;
import com.org.bddsserver.dto.PersonnelInfo;
import com.org.bddsserver.exception.BusinessException;
import com.org.bddsserver.mapper.PeriodDataMapper;
import com.org.bddsserver.mapper.PersonnelMapper;
import com.org.bddsserver.pojo.ClientClassInfo;
import com.org.bddsserver.service.PeriodDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/10/16
 * @Description
 */
@Service
@Slf4j
public class PeriodDataServiceImpl implements PeriodDataService {
    @Autowired
    private PeriodDataMapper periodDataMapper;

    @Autowired
    private PersonnelMapper personnelMapper;

    private static final String[] client_type = {"0", "1", "3"}; // 客户类型数组：0-个人；1-产业；3-机构

    @Transactional
    @Override
    public PeriodDataDetailResponse getPeriodDataDetailResponse(String startDate, String endDate, Integer sysUserId) {
        PeriodDataDetailResponse periodDataDetailResponse = new PeriodDataDetailResponse();
        List<PeriodDataDetail> periodDataDetailList = new ArrayList<>();


        //需要基于登录用户的数据查看权限拉取对应的业务人员信息
        List<PersonnelInfo> personnelInfoList = personnelMapper.getPersonnelDetailBySysUserId(sysUserId);

        if (personnelInfoList == null || personnelInfoList.isEmpty()) {
            throw new BusinessException(1001, "没有查询到业务人员信息,请与业务人员客户类关系表维护人员或管理员联系，尽快更新业务人员客户类关系表！");
        }

        Integer tradeDateCount = periodDataMapper.queryTradeDayCount(startDate, endDate); // 计算交易日日均权益时用

        int count = 0;
        for (PersonnelInfo personnelInfo : personnelInfoList) {
            log.info("当前循环查询的业务人员信息：{}",personnelInfo);
            if (personnelInfo == null){
                continue;
            }
            PeriodDataDetail periodDataDetail = new PeriodDataDetail();
            periodDataDetail.setPersonnelInfo(personnelInfo);

            Integer[] new_customer_count = {0, 0, 0}; //新增客户
            Integer[] total_customer_count = {0, 0, 0};
            Double[] initial_equity = {0d, 0d, 0d};
            Double[] end_equity = {0d, 0d, 0d};
            Double[] net_deposit = {0d, 0d, 0d};
            PeriodDataQueryResult combineQueryResult;

            //基于业务人员代码拉取其所属客户类列表
            List<ClientClassInfo> client_code_list = personnelMapper.queryClientCodeByPersonnelCode(personnelInfo.getPersonnel_code());

            for (String type : client_type) {

                //基于所属客户类列表拉取相关客户号列表，如果客户类列表为空，代表其名下没有客户，相关指标数据也需要赋予空值
                String[] client_id_list = {};

                if (client_code_list.size() > 0) {
                    client_id_list = personnelMapper.queryClientIdListByClinetCodeList_active(client_code_list, type, endDate); //客户列表（统计周期结束时间前开户且在这个结束时间前未销户的客户）

                    if (client_id_list.length > 0) {
                        total_customer_count[count] = periodDataMapper.queryTotalCustomerCount(client_id_list,endDate); //求总客户
                    }
                    client_id_list = personnelMapper.queryClientIdListByClinetCodeList_all(client_code_list, type); //客户列表（不刨除销户）
                    if (client_id_list.length > 0) {
                        new_customer_count[count] = periodDataMapper.queryNewCustomerCount(client_id_list,startDate,endDate); //求新增客户

                        initial_equity[count] = periodDataMapper.queryInitialEquity(client_id_list,startDate); //求期初权益
                        end_equity[count] = periodDataMapper.queryEndEquity(client_id_list,endDate); //求期末权益
                        combineQueryResult = periodDataMapper.combineQuery(client_id_list,startDate,endDate,tradeDateCount);
                        if (combineQueryResult == null) {
                            combineQueryResult = new PeriodDataQueryResult();
                        }
                        net_deposit[count] = safeDouble(combineQueryResult.getDeposit())-safeDouble(combineQueryResult.getWithdrawal());
                        periodDataDetail.setCombineQueryResult(combineQueryResult,count);
                    }

                }
                count+=1;
            }
            //加入集合
            periodDataDetail.setNew_customer_count(new_customer_count);
            periodDataDetail.setTotal_customer_count(total_customer_count);
            periodDataDetail.setInitial_equity(initial_equity);
            periodDataDetail.setEnd_equity(end_equity);
            periodDataDetail.setNet_deposit(net_deposit);
            periodDataDetailList.add(periodDataDetail);
            count = 0;
        }


        //
        String period = startDate + "-" + endDate;
        periodDataDetailResponse.setPeriod(period);
        periodDataDetailResponse.setPeriodDataDetailList(periodDataDetailList);
        return periodDataDetailResponse;
    }

    // 将null值转换到一个安全值，比如0.0
    private Double safeDouble(Double value) {
        if (value == null) {
            return 0.00; // null时返回0.00
        }
        // 非null时，使用BigDecimal四舍五入保留两位小数
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP); // HALF_UP即四舍五入模式
        return bd.doubleValue();
    }
}
