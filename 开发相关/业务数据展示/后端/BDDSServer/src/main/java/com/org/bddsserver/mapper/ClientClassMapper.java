package com.org.bddsserver.mapper;

import com.org.bddsserver.pojo.ClientClassInfo;
import com.org.bddsserver.pojo.personnel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/10/28
 * @Description
 */
@Mapper
public interface ClientClassMapper {
    List<ClientClassInfo> selectAll();

    Integer insertOne(@Param("clientClassInfo") ClientClassInfo clientClassInfo);

    Integer deleteById(@Param("id") Integer id);

    Integer updateById(@Param("clientClassInfo") ClientClassInfo clientClassInfo);


}
