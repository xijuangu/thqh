package org.example.processserver.mapper.db1;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2024/11/5
 * @Description
 */
@Mapper
public interface ads_cfsMapper {
    /**
     *
     * @param ads_cfs_list 接收的CSV文件ADS_ADM_CLIENT_FINANCE_STATUS_EXP.csv中逐行读取的数据对象列表
     * @return 执行结果 —— 成功插入的行数
     */
    int insertList_ads_cfs(List<Object> ads_cfs_list);

    /**
     * 写入数据前清空ads_futures_client_finance_status表
     */
    void truncate_ads_cfs();

    //动态传入表名获取指定表行数统计
    int getCount_designatedTable(@Param("tableName") String tableName);

}
