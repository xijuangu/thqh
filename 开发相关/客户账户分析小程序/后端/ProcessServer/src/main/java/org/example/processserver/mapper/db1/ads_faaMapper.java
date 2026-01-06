package org.example.processserver.mapper.db1;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2024/11/5
 * @Description
 */
@Mapper
public interface ads_faaMapper {
    /**
     *
     * @param ads_faa_list 接收的CSV文件ADS_ADM_ACCOUNT_ANALYSIS_EXP.csv中逐行读取的数据对象列表
     * @return 执行结果 —— 成功插入的行数
     */
    int insertList_ads_faa(List<Object> ads_faa_list);

    /**
     * 写入数据前清空ads_futures_account_analysis表
     */
    void truncate_ads_faa();
}
