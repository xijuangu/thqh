package org.example.processserver.mapper.db1;

/**
 * @author PY.Lu
 * @date 2025/5/21
 * @Description 通用获取Mysql库表全量/增量数据量方法
 */
public interface CommonMapper_M {
    // 获取表全量数据量
    int getCount();

    // 获取表增量数据量
    int Increment_getCount();
}
