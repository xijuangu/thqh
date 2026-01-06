package com.thqh.enterprise_wechat_backend.mapper;

import com.thqh.enterprise_wechat_backend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author liubin
 */
@Mapper
public interface UserMapper {

    @Select("SELECT id, username, password, status, created_by,created_at, updated_by,updated_at FROM sys_user WHERE username = #{username}")
    User findUserByUsername(@Param("username") String username);
}