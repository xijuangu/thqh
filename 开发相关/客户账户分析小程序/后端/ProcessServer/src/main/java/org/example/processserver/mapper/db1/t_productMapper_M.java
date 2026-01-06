package org.example.processserver.mapper.db1;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.example.processserver.pojo.t_product;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2024/11/8
 * @Description
 */
@Mapper
@DS("db1DataSource")
public interface t_productMapper_M extends CommonMapper_M {
    List<t_product> selectAll();

    int getCount();




}
