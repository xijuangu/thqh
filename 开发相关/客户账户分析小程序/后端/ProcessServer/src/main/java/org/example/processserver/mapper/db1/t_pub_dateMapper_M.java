package org.example.processserver.mapper.db1;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.example.processserver.pojo.t_pub_date;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2024/11/8
 * @Description
 */
@Mapper
@DS("db1DataSource")
public interface t_pub_dateMapper_M extends CommonMapper_M {
    List<t_pub_date> selectAll();

    int getCount();



}
