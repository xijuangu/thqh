package org.example.processserver.mapper.db2;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author PY.Lu
 * @date 2025/5/20
 * @Description
 */
@Mapper
@DS("db2DataSource")
public interface t_marketMapper_O extends CommonMapper_O{
    int getCount_Oracle();


}
