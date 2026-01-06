package com.thqh.wechat_miniprogram_backend.service;

import com.thqh.wechat_miniprogram_backend.config.WeChatProperties;
import com.thqh.wechat_miniprogram_backend.dto.GetAccessTokenResponse;
import com.thqh.wechat_miniprogram_backend.dto.GetPhoneNumberResponse;
import com.thqh.wechat_miniprogram_backend.dto.GetPhoneNumberRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * @ClassName: WeChatService
 * @Description:
 * @Author liubin
 * @Date 2025/1/2 14:17
 * @Version V1.0
 */


@Service
public class WeChatService {

    private static final Logger logger = LoggerFactory.getLogger(WeChatService.class);

    private final RestTemplate restTemplate;
    private final WeChatProperties weChatProperties;

    @Autowired
    public WeChatService(RestTemplate restTemplate, WeChatProperties weChatProperties) {
        this.restTemplate = restTemplate;
        this.weChatProperties = weChatProperties;
    }

    /**
     * 调用微信接口获取access_token
     *
     * @return GetAccessTokenResponse
     */
    @Cacheable(value = "accessToken")
    public GetAccessTokenResponse getAccessToken() {
        URI uri = UriComponentsBuilder.fromHttpUrl(weChatProperties.getAccessTokenUrl())
                .queryParam("grant_type", "client_credential")
                .queryParam("appid", weChatProperties.getAppid())
                .queryParam("secret", weChatProperties.getSecret())
                .build()
                .toUri();
        logger.info("<获取接口调用凭据>接口地址：{}", weChatProperties.getAccessTokenUrl());
        GetAccessTokenResponse response = restTemplate.getForObject(uri, GetAccessTokenResponse.class);
        logger.info("<获取接口调用凭据>接口response：{}", response);
        return response;
    }

    /**
     * 调用微信接口获取用户手机号
     *
     * @param accessToken access_token
     * @param code        前端传来的code
     * @return GetPhoneNumberResponse
     */
    public GetPhoneNumberResponse getPhoneNumber(String accessToken, String code) {
        URI uri = UriComponentsBuilder.fromHttpUrl(weChatProperties.getPhoneNumberUrl())
                .queryParam("access_token", accessToken)
                .build()
                .toUri();

        GetPhoneNumberRequest request = new GetPhoneNumberRequest();
        request.setCode(code);
        logger.info("<获取手机号>接口地址：{}", weChatProperties.getPhoneNumberUrl());
        GetPhoneNumberResponse response = restTemplate.postForObject(uri, request, GetPhoneNumberResponse.class);
        logger.info("<获取手机号>接口response：{}", response);
        return response;
    }

    /**
     * 调用微信接口获取用户unionid
     *
     * @param code  前端传来的code
     * @return String
     */
    public String getWxOpenid(String code) {
        URI uri = UriComponentsBuilder.fromHttpUrl(weChatProperties.getCode2SessionUrl())
                .queryParam("js_code", code)
                .queryParam("grant_type", "authorization_code")
                .queryParam("appid", weChatProperties.getAppid())
                .queryParam("secret", weChatProperties.getSecret())
                .build()
                .toUri();
        logger.info("<获取openid>接口地址：{}", weChatProperties.getCode2SessionUrl());
        String response = restTemplate.getForObject(uri,String.class);
        logger.info("<获取openid>接口response：{}", response);
        return response;
    }
}
