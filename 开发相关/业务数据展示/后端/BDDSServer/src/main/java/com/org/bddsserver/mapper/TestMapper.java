package com.org.bddsserver.mapper;

import com.org.bddsserver.pojo.TestPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author PY.Lu
 * @date 2025/10/15
 * @Description
 */
@Mapper
public interface TestMapper {
    @Select("SELECT * FROM client_class_info where id = 1 ")
    TestPojo getOneTestPojo_byNotation();

    TestPojo getOneTestPojo_byMapperXML();

}
