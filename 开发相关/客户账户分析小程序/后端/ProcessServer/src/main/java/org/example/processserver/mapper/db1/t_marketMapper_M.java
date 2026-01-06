package org.example.processserver.mapper.db1;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author PY.Lu
 * @date 2025/5/21
 * @Description
 */
@Mapper
@DS("db1DataSource")
public interface t_marketMapper_M extends CommonMapper_M {
    int getCount();

}
