package com.logdemo.lightserver.Mapper;

import com.logdemo.lightserver.pojo.data;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author PY.Lu
 * @date 2025/3/6
 * @Description
 */
@Mapper
public interface dataMapper {
    //
    int save(data a);

    void truncate_data();


    int count();

}
