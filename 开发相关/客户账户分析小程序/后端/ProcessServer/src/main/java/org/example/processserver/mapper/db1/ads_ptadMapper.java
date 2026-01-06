package org.example.processserver.mapper.db1;

import org.apache.ibatis.annotations.Mapper;
import org.example.processserver.pojo.ads_ptad;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2024/11/4
 * @Description
 */
@Mapper
public interface ads_ptadMapper {
    /**
     *
     * @param ads_ptad_list 接收的CSV文件ADS_ADM_PRODUCT_TRADE_ANALYSIS_DETAIL_1D_EXP.csv中逐行读取的数据对象列表
     * @return 执行结果 —— 成功插入的行数
     */
    int insertList_ads_ptad(List<Object> ads_ptad_list);

    /**
     * 写入数据前清空ads_futures_product_trade_analysis_detail表
     */
    void truncate_ads_ptad();

}
