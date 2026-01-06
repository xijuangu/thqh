package org.example.processserver.mapper.db2;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("db2DataSource")
public interface t_execute_resultMapper_O extends CommonMapper_O {
    int Increment_getCount_Oracle();

    int getCount_Oracle();
}
