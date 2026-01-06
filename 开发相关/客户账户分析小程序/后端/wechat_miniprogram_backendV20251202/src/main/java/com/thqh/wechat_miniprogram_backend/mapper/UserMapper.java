package com.thqh.wechat_miniprogram_backend.mapper;

import com.thqh.wechat_miniprogram_backend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @ClassName: UserMapper
 * @Description:
 * @Author liubin
 * @Date 2024/12/31 17:04
 * @Version V1.0
 */




@Mapper
public interface UserMapper {

    void insertUser(User user);

    List<User> findAllUsers();

    void updateUser(User user);

    /**
     * 根据手机号查找用户
     * @param phoneNumber 手机号
     * @return User
     */
    User findUserByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    /**
     * 根据openid查找用户
     * @param openid 手机号
     * @return User
     */
    User findUserByOpenid(@Param("openid") String openid);

    /**
     * 根据用户ID查找用户
     * @param id 用户ID
     * @return User
     */
    User findUserById(@Param("id") Long id);
}
