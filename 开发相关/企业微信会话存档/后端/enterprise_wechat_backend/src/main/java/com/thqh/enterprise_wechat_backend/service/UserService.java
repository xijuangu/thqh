package com.thqh.enterprise_wechat_backend.service;

import com.thqh.enterprise_wechat_backend.dto.LoginRequest;
import com.thqh.enterprise_wechat_backend.util.JwtUtil;
import com.thqh.enterprise_wechat_backend.entity.User;
import com.thqh.enterprise_wechat_backend.exception.ErrorCode;
import com.thqh.enterprise_wechat_backend.mapper.UserMapper;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: UserService
 * @Description:
 * @Author liubin
 * @Date 2025/3/19 15:37
 * @Version V1.0
 */
@Service
public class UserService {

    private final UserMapper userMapper;

    private final JwtUtil jwtUtil;

    public UserService(UserMapper userMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    public User findByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }


    public  Map<String, Object> userLogin(LoginRequest loginRequest){
        // 根据用户名查询用户信息
        User user = findByUsername(loginRequest.getUsername());
        Map<String,Object > result = new HashMap<>();
        if (user == null) {
            result.put("code", ErrorCode.USER_NOT_EXISTS.getCode());
            result.put("msg",ErrorCode.USER_NOT_EXISTS.getMessage());
            return result;
        }
        // 检查用户状态（例如：仅允许 status == 1 的用户登录）
        if (user.getStatus() != 1) {
            result.put("code", ErrorCode.USER_IS_DISABLED.getCode());
            result.put("msg",ErrorCode.USER_IS_DISABLED.getMessage());
            return result;
        }
        // 比较密码（前端传入的已 MD5 加密密码）
        if (!user.getPassword().equals(loginRequest.getPassword().toUpperCase())) {
            result.put("code", ErrorCode.PASSWORD_ERROR.getCode());
            result.put("msg",ErrorCode.PASSWORD_ERROR.getMessage());
            return result;
        }
        // 生成 JWT 并返回给前端
        String token = jwtUtil.generateJwtToken(user);
        result.put("code", 0);
        result.put("msg",null);
        result.put("access_token",token);
        result.put("expire",jwtUtil.getExpire());
        return result;
    }
}