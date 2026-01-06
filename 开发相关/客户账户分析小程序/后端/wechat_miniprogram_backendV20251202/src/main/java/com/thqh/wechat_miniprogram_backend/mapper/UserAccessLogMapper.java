package com.thqh.wechat_miniprogram_backend.mapper;


import com.thqh.wechat_miniprogram_backend.entity.UserAccessLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 
 */

@Mapper
public interface UserAccessLogMapper {

    @Insert("INSERT INTO user_access_log (" +
            "user_id, access_time, function_code, client_ip, trade_account, " +
            "access_status, error_message, created_time, created_by, updated_time, updated_by" +
            ") VALUES (" +
            "#{userId}, #{accessTime}, #{functionCode}, #{clientIp}, #{tradeAccount}, " +
            "#{accessStatus}, #{errorMessage}, #{createdTime}, #{createdBy}, #{updatedTime}, #{updatedBy}" +
            ")")
    int insert(UserAccessLog log);
}
