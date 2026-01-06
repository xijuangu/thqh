package com.thqh.enterprise_wechat_backend.service;


import com.thqh.enterprise_wechat_backend.entity.User;
import com.thqh.enterprise_wechat_backend.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public CustomUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // 按手机号加载用户
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户未找到，username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                // 没有权限
                Collections.emptyList()
        );
    }
}
