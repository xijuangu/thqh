package org.example.processserver.mapper.db2;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("db2DataSource")
public interface t_hold_detailMapper_O extends CommonMapper_O {
    int getCount_Oracle();

    int Increment_getCount_Oracle();
}
