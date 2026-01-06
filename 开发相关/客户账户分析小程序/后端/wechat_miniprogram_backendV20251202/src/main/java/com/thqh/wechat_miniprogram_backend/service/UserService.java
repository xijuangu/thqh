package com.thqh.wechat_miniprogram_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thqh.wechat_miniprogram_backend.dto.*;
import com.thqh.wechat_miniprogram_backend.entity.User;
import com.thqh.wechat_miniprogram_backend.exception.BusinessException;
import com.thqh.wechat_miniprogram_backend.exception.ErrorCode;
import com.thqh.wechat_miniprogram_backend.mapper.UserMapper;
import com.thqh.wechat_miniprogram_backend.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: UserService
 * @Description:
 * @Author liubin
 * @Date 2025/1/2 11:02
 * @Version V1.0
 */


@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    private final UserMapper userMapper;
    private final WeChatService weChatService;
    private final JwtUtil jwtUtil;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public UserService(UserMapper userMapper, WeChatService weChatService, JwtUtil jwtUtil ) {
        this.userMapper = userMapper;
        this.weChatService = weChatService;
        this.jwtUtil = jwtUtil;
    }

    public User findUserById(Long userId){
        return userMapper.findUserById(userId);
    }


    /**
     * 获取用户手机号并生成JWT令牌
     *
     * @param getAuthRequest 包含微信小程序前端传来的type和code
     * @return 包含用户信息和JWT令牌的结果对象
     */
    @Transactional
    public GetUserInfoWithJwt getUserInfoAndGenerateJwt(GetAuthRequest getAuthRequest)  {
        try{
            GetUserInfoWithJwt resultWithJwt = new GetUserInfoWithJwt();
            String type = getAuthRequest.getType();
            if("openid".equals(type)){
                //调用code2Session接口获取用户openid
                String openidResponse =weChatService.getWxOpenid(getAuthRequest.getCode());
                // 创建 ObjectMapper 对象,将 JSON 字符串转换为 JsonNode 对象
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(openidResponse);
                // 判断是否存在errcode
                if(openidResponse.contains("errcode")){
                    String errmsg = jsonNode.get("errmsg").asText();
                    int errcode = jsonNode.get("errcode").asInt();
                    if (errcode != 0) {
                        logger.error("调用<code2Session>接口失败，错误码: {}, 错误信息: {}", errcode, errmsg);
                        throw new BusinessException(ErrorCode.CODE_TO_SESSION_FAILED, ErrorCode.CODE_TO_SESSION_FAILED.getMessage() + ":" + errmsg);
                    }
                }
                String openid = jsonNode.get("openid").asText();
                //String sessionKey = jsonNode.get("session_key").asText();

                //根据openid查找或创建用户
                User user = userMapper.findUserByOpenid(openid);
                if (user == null) {
                    // 如果用户不存在，可以创建一个新用户
                    user = new User();
                    user.setOpenid(openid);
                    user.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
                    // 系统
                    user.setCreatedBy(0L);
                    user.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
                    // 系统
                    user.setUpdatedBy(0L);
                    userMapper.insertUser(user);
                    logger.info("创建新用户：{}", user);
                }

                // 生成JWT令牌，仅使用用户ID
                String jwtToken = jwtUtil.generateJwtToken(user.getId());
                logger.info("生成JWT令牌，用户ID: {}, 令牌: {}", user.getId(), jwtToken);

                //获取JWT过期时间
                LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
                LocalDateTime expireTime = now.plusMinutes(jwtUtil.getExpiration()/60000);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String expireTimeStr = expireTime.format(formatter);
                logger.info("JWT过期时间: {}", expireTimeStr);

                // 封装结果对象，包括用户信息和JWT令牌
                resultWithJwt.setUserId(user.getId());
                resultWithJwt.setOpenid(user.getOpenid());
                resultWithJwt.setJwtToken(jwtToken);
                resultWithJwt.setNickname(user.getNickname());
                resultWithJwt.setJwtExpirationTime(expireTimeStr);

            }else if("phone_number".equals(type)){
                // 1. 获取access_token
                String accessToken = (String) redisTemplate.opsForValue().get("wx:access_token");
                if(accessToken == null || accessToken.isEmpty()){
                    GetAccessTokenResponse accessTokenResponse = weChatService.getAccessToken();
                    if (accessTokenResponse.getErrcode() != null && accessTokenResponse.getErrcode() != 0) {
                        logger.error("调用<获取接口调用凭据>接口获取access_token失败，错误码: {}, 错误信息: {}", accessTokenResponse.getErrcode(), accessTokenResponse.getErrmsg());
                        throw new BusinessException(ErrorCode.ACCESS_TOKEN_FAILED, "获取access_token失败: " + accessTokenResponse.getErrmsg());
                    }
                    accessToken = accessTokenResponse.getAccessToken();
                    redisTemplate.opsForValue().set("wx:access_token", accessToken, 6000, TimeUnit.SECONDS);

                }
                logger.info("获取到的access_token: {}", accessToken);

                // 2. 使用access_token和code获取手机号
                GetPhoneNumberResponse phoneNumberResponse = weChatService.getPhoneNumber(accessToken, getAuthRequest.getCode());
                if (phoneNumberResponse.getErrcode() != null && phoneNumberResponse.getErrcode() != 0) {
                    logger.error("调用<获取手机号>接口获取手机号失败，错误码: {}, 错误信息: {}", phoneNumberResponse.getErrcode(), phoneNumberResponse.getErrmsg());
                    throw new BusinessException(ErrorCode.PHONE_NUMBER_FAILED, "获取手机号失败: " + phoneNumberResponse.getErrmsg());
                }

                // 3. 提取手机号信息
                GetPhoneNumberResponse.PhoneInfo phoneInfo = phoneNumberResponse.getPhoneInfo();
                if (phoneInfo == null) {
                    logger.error("phone_info is null");
                    throw new BusinessException(ErrorCode.PHONE_INFO_NULL, "获取手机号信息失败: phone_info is null");
                }

                String phoneNumber = phoneInfo.getPhoneNumber();
                String purePhoneNumber = phoneInfo.getPurePhoneNumber();
                String countryCode = phoneInfo.getCountryCode();

                logger.info("成功获取用户手机号: {}", phoneNumber);

                // 4. 根据手机号查找或创建用户
                User user = userMapper.findUserByPhoneNumber(phoneNumber);
                if (user == null) {
                    // 如果用户不存在，可以创建一个新用户
                    user = new User();
                    user.setPhoneNumber(phoneNumber);
                    // 或根据其他逻辑设置
                    user.setNickname(phoneNumber.substring(phoneNumber.length() - 4));
                    user.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
                    // 系统
                    user.setCreatedBy(0L);
                    user.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
                    // 系统
                    user.setUpdatedBy(0L);
                    userMapper.insertUser(user);
                    logger.info("创建新用户：{}", user);
                }

                // 5. 生成JWT令牌，仅使用用户ID
                String jwtToken = jwtUtil.generateJwtToken(user.getId());
                logger.info("生成JWT令牌，用户ID: {}, 令牌: {}", user.getId(), jwtToken);

                //获取JWT过期时间
                LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
                LocalDateTime expireTime = now.plusMinutes(jwtUtil.getExpiration()/60000);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String expireTimeStr = expireTime.format(formatter);
                logger.info("JWT过期时间: {}", expireTimeStr);

                // 6. 封装结果对象，包括用户信息和JWT令牌
                resultWithJwt.setUserId(user.getId());
                resultWithJwt.setPhoneNumber(phoneNumber);
                resultWithJwt.setPurePhoneNumber(purePhoneNumber);
                resultWithJwt.setCountryCode(countryCode);
                resultWithJwt.setJwtToken(jwtToken);
                resultWithJwt.setNickname(user.getNickname());
                resultWithJwt.setJwtExpirationTime(expireTimeStr);

            }else{
                logger.error("type value error");
                throw new BusinessException(ErrorCode.TYPE_VALUE_ERROR,ErrorCode.TYPE_VALUE_ERROR.getMessage());
            }
            return resultWithJwt;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过JWT获取到当前用户信息
     * @return
     */
    public User getUserByJwt(){
        // 获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        // 从认证信息中获取用户详细信息
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = Long.valueOf(userDetails.getUsername());

        // 查询用户信息
        return findUserById(userId);
    }
}

