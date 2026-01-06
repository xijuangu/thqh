package org.example.processserver.mapper.db1;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.example.processserver.pojo.CSVloaction;

/**
 * @author PY.Lu
 * @date 2024/10/31
 * @Description 项目Mysql数据库中的Location表记录了CSV文件上传（期货->控股）目录、CSV文件接收（控股->期货）目录
 */
@Mapper
@DS("db1DataSource")
public interface LocationMapper {
    // 从Location表中获取路径信息
    CSVloaction getLocation();
}
