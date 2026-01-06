package com.thqh.wechat_miniprogram_backend.controller;

import com.thqh.wechat_miniprogram_backend.dto.*;
import com.thqh.wechat_miniprogram_backend.entity.User;
import com.thqh.wechat_miniprogram_backend.service.TradeAccountService;
import com.thqh.wechat_miniprogram_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import com.thqh.wechat_miniprogram_backend.annotation.AccessLog;

/**
 * @ClassName: UserController
 * @Description:
 * @Author liubin
 * @Date 2025/1/2 11:03
 * @Version V1.0
 */


@RestController
@RequestMapping("/api-wx/users")
public class UserController {


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final TradeAccountService tradeAccountService;


    @Autowired
    public UserController(UserService userService, TradeAccountService tradeAccountService) {
        this.userService = userService;
        this.tradeAccountService = tradeAccountService;
    }


    /**
     * POST /api/users/auth
     * 请求体:
     * {
     * "code": "CODE_FROM_FRONTEND"
     * }
     * 响应体:
     * {
     * "phoneNumber": "用户绑定的手机号",
     * "purePhoneNumber": "没有区号的手机号",
     * "countryCode": "区号"
     * }
     */
    @AccessLog(functionCode = "LOGIN")
    @PostMapping("/auth")
    public ResponseEntity<?> userAuth(@Valid @RequestBody GetAuthRequest request) {
        logger.info("接收到用户认证的请求,code: {}", request);
        GetUserInfoWithJwt result = userService.getUserInfoAndGenerateJwt(request);
        logger.info("用户认证请求结果：{}", ApiResponse.success(result));
        return ResponseEntity.ok(ApiResponse.success(result));
    }


    /**
     * GET /api/users/userinfo
     * 响应体:
     * {
     * "userId": 1,
     * "phoneNumber": "13800138000",
     * "nickname": "张三"
     * }
     */
    @AccessLog(functionCode = "QUERY_USERINFO")
    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserProfile() {
        // 查询用户信息
        User user = userService.getUserByJwt();
        // 封装用户信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("user_id", user.getId());
        userInfo.put("phone_number", user.getPhoneNumber());
        userInfo.put("nickname", user.getNickname());
        userInfo.put("openid", user.getOpenid());
        logger.info("获取用户信息请求结果：{}", ApiResponse.success(userInfo));
        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }


    /**
     * POST /api/users/tradeaccount/login/
     * 请求体:
     * {
     * "trade_account": "客户交易账号",
     * "password": "密码"
     * }
     * 成功响应体:
     * {
     * "code": 0,
     * "msg": "",
     * "data": "客户交易账号"
     * }
     * <p>
     * 失败响应体示例:
     * {
     * "code": 1003,
     * "msg": "交易账号验证失败",
     * "data": null
     * }
     */
    @AccessLog(functionCode = "LOGIN_TRADEACCOUNT")
    @PostMapping("/tradeaccount/login")
    public ResponseEntity<?> tradeAccountLogin(@Validated @RequestBody TradeAccountLoginRequest request, HttpServletRequest httpServletRequest) {
        String remoteAddr= httpServletRequest.getHeader("x-forwarded-for");
        logger.info("IP地址: {}", remoteAddr);
        // 如果是IPv6回环地址，则手动转换为IPv4回环地址
        if ("0:0:0:0:0:0:0:1".equals(remoteAddr)) {
            remoteAddr = "127.0.0.1";
        }
        logger.info("接收到交易账号登录请求，交易账号: {}", request.getTradeAccount());
        request.setClientIp(remoteAddr);
        TradeAccountStatusResponse tradeAccountStatusResponse = tradeAccountService.login(request);
        logger.info("客户交易账号登录请求结果：{}", ApiResponse.success(tradeAccountStatusResponse));
        return ResponseEntity.ok(ApiResponse.success(tradeAccountStatusResponse));
    }


    /**
     * GET /api/users/tradeaccount
     * 响应体:
     * {
     * "code": 0,
     * "msg": "",
     * "data": ｛
     * "trade_account":"",
     * "expiration_time":""
     * ｝
     * }
     */
    @AccessLog(functionCode = "QUERY_TRADEACCOUNT")
    @GetMapping("/tradeaccount")
    public ResponseEntity<?> getTradeAccountStatus() {
        TradeAccountStatusResponse tradeAccountStatusResponse = tradeAccountService.getTradeAccountStatus();
        logger.info("获取客户交易账号状态请求结果：{}", ApiResponse.success(tradeAccountStatusResponse));
        return ResponseEntity.ok(ApiResponse.success(tradeAccountStatusResponse));
    }


    @AccessLog(functionCode = "LOGOUT_TRADEACCOUNT")
    @DeleteMapping("/tradeaccount")
    public ResponseEntity<?> logoutTradeAccount() {
        TradeAccountStatusResponse tradeAccountStatusResponse = tradeAccountService.logoutTradeAccount();
        logger.info("注销客户交易账号状态请求结果：{}", ApiResponse.success(tradeAccountStatusResponse));
        return ResponseEntity.ok(ApiResponse.success(tradeAccountStatusResponse));
    }


}

