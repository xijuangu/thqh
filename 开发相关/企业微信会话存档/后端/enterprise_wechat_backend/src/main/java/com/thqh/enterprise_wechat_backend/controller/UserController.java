package com.thqh.enterprise_wechat_backend.controller;

import com.thqh.enterprise_wechat_backend.dto.ApiResponse;
import com.thqh.enterprise_wechat_backend.dto.LoginRequest;
import com.thqh.enterprise_wechat_backend.exception.ErrorCode;
import com.thqh.enterprise_wechat_backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @ClassName: UserController
 * @Description:
 * @Author liubin
 * @Date 2025/3/19 15:39
 * @Version V1.0
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)//开发阶段允许跨域请求
public class UserController {

    private  final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // 调用服务层获取会话内容存档
            logger.info("登陆请求参数: {}", loginRequest);
            Map<String, Object> result= userService.userLogin(loginRequest);
            int code = (int) result.get("code");
            String msg = (String) result.get("msg");
            if(code == 0){
                result.remove("code");
                result.remove("msg");
                logger.info("登陆请求响应内容: {}", ApiResponse.success(result));
                return ResponseEntity.ok(ApiResponse.success(result));
            }else{
                logger.info("登陆请求响应内容: {}", ApiResponse.failure(code,msg));
                return ResponseEntity.ok(ApiResponse.failure(code,msg));
            }
        } catch (Exception e) {
            // 生产环境中建议记录异常日志，不暴露敏感信息给前端
            logger.error("login Exception:{}",e.getMessage());
            return ResponseEntity.ok(ApiResponse.failure(ErrorCode.LOGIN_FAILURE.getCode(),ErrorCode.LOGIN_FAILURE.getMessage()));
        }
    }
}
