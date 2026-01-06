package org.example.processserver.mapper.db1;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.processserver.pojo.*;

import java.util.List;

@Mapper
@DS("db1DataSource")
public interface t_doneMapper_M extends CommonMapper_M {
    //测试拉取一条加密回写并修改加密标识
    t_done getOne(int id);

    //数据写入data文件 -- 批量拉取数据（全量数据）-暂无
    List<t_done> getAll();

    //数据写入data文件 -- 指定日期区间拉取增量数据-暂无
    List<t_done> getIncrement(@Param("startDate") String startDate, @Param("endDate") String endDate);

    //增量、全量--数据加密处理 -- 清空临时表
    int deleteFrom_TDONE_encrypt_temp();

    //增量、全量--数据加密处理 -- 从原始表获取加密字段的distinct值
    List<encrypt_field> TDONE_encrypt_field();

    //增量、全量--数据加密处理 -- 将不重复的原始字段和对应的加密字段写入临时表
    int insertInto_TDONE_encrypt_temp(@Param("TDONE_B_clientId") String TDONE_B_clientId,
                                      @Param("TDONE_A_clientId") String TDONE_A_clientId,
                                      @Param("TDONE_B_fundaccountId") String TDONE_B_fundaccountId,
                                      @Param("TDONE_A_fundaccountId") String TDONE_A_fundaccountId,
                                      @Param("TDONE_B_tradeaccountId") String TDONE_B_tradeaccountId,
                                      @Param("TDONE_A_tradeaccountId") String TDONE_A_tradeaccountId
    );


    //全量--数据加密处理 -- 获取总初始数据量
    int getCount();

    //全量--数据加密处理 -- 批量覆写
    int overWrite(@Param("id1") int id1, @Param("id2") int id2);

    //增量--数据加密处理 -- 获取增量数据量
    int Increment_getCount();

    //增量--数据加密处理 -- 覆写
    int overWrite_increment();

}
