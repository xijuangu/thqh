package com.thqh.wechat_miniprogram_backend.service;

import com.thqh.wechat_miniprogram_backend.entity.User;
import com.thqh.wechat_miniprogram_backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @ClassName: CustomUserDetailsService
 * @Description:
 * @Author liubin
 * @Date 2025/1/2 16:30
 * @Version V1.0
 */



@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Autowired
    public CustomUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // 按手机号加载用户
    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userMapper.findUserByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new UsernameNotFoundException("用户未找到，手机号: " + phoneNumber);
        }
        // 这里没有密码，可以根据实际情况调整
        return new org.springframework.security.core.userdetails.User(
                user.getPhoneNumber(),
                // 没有密码
                "",
                // 没有权限
                Collections.emptyList()
        );
    }

    // 按用户ID加载用户
    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        User user = userMapper.findUserById(userId);
        if (user == null) {
            throw new UsernameNotFoundException("用户未找到，ID: " + userId);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getId().toString(),
                // 没有密码
                "",
                // 没有权限
                Collections.emptyList()
        );
    }
}
