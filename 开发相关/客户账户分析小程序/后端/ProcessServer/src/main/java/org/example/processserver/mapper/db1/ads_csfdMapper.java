package org.example.processserver.mapper.db1;

import org.apache.ibatis.annotations.Mapper;
import org.example.processserver.pojo.ads_cfsd;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2024/11/5
 * @Description
 */
@Mapper
public interface ads_csfdMapper {
    /**
     *
     * @param ads_cfsd_list 接收的CSV文件ADS_ADM_CLIENT_FINANCE_STATUS_DETAIL_1D_EXP.csv中逐行读取的数据对象列表
     * @return 执行结果 —— 成功插入的行数
     */
    int insertList_ads_cfsd(List<Object> ads_cfsd_list);

    /**
     * 写入数据前清空ads_futures_client_finance_status_detail表
     */
    void truncate_ads_cfsd();
}
