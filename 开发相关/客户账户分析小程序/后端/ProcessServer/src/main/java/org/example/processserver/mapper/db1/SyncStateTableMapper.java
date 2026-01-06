package org.example.processserver.mapper.db1;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.processserver.pojo.SyncStateTable;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/7/4
 * @Description 项目Mysql数据库中的SyncStateTable表记录了全量/增量同步后的，项目Mysql库和CRM数据库的数据量情况，用于检查数据同步情况
 */
@Mapper
@DS("db1DataSource")
public interface SyncStateTableMapper {
    // 返回SyncStateTable表数据
    List<SyncStateTable> searchAll();

    // 将数据量检查情况写入SyncStateTable表
    int update(@Param("syncstate")int syncstate,@Param("checkTime")String checkTime,@Param("sourceCount")Integer sourceCount,@Param("targetCount")Integer targetCount,@Param("checkType")String checkType,@Param("tableName")String tableName);
}
