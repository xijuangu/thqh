package org.example.processserver.mapper.db1;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.processserver.pojo.csvName_Type;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2024/11/7
 * @Description
 */
@Mapper
public interface csvName_TypeMapper {
    // 开发测试用
    csvName_Type getCsvName_Type_one(@Param("csvName") String csvName);

    // CSVFileName表记录了控股传回的CSV文件的名称（由期货与控股方协商定义）、类型（s:汇总表；d：明细表）、别名(后端可基于别名关联到CSV名称来控制读哪个CSV文件)
    List<csvName_Type> getCsvName_Type_all();
}
