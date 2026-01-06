package com.thqh.wechat_miniprogram_backend.service;

import com.thqh.wechat_miniprogram_backend.config.ReportMapping;
import com.thqh.wechat_miniprogram_backend.config.ReportMappingsConfig;
import com.thqh.wechat_miniprogram_backend.entity.User;
import com.thqh.wechat_miniprogram_backend.exception.BusinessException;
import com.thqh.wechat_miniprogram_backend.exception.ErrorCode;
import com.thqh.wechat_miniprogram_backend.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;


import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: ReportService
 * @Description:
 * @Author liubin
 * @Date 2025/1/8 16:35
 * @Version V1.0
 */

@Service
public class ReportService {

    private final UserService userService;
    private final EncryptionUtil encryptionUtil;
    private final ReportMappingsConfig reportMappingsConfig;

    @Value("${bi.report.baseUrl}")
    private String biReportBaseUrl;

    @Value("${bi.report.domainId}")
    private String domainId;

    @Value("${bi.report.externalUserId}")
    private String externalUserId;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public ReportService(UserService userService, EncryptionUtil encryptionUtil, ReportMappingsConfig reportMappingsConfig) {
        this.userService = userService;
        this.encryptionUtil = encryptionUtil;
        this.reportMappingsConfig = reportMappingsConfig;
    }

    /**
     * 生成BI报表的访问地址
     *
     * @param reportCode 报表代码
     * @return BI报表访问地址
     */
    public String generateReportUrl(String reportCode) {
        User user = userService.getUserByJwt();

        // 1. 查询用户交易账号
        String tradeAccountKey = "user:trade_account:" + user.getId();

        String clientId = (String) redisTemplate.opsForValue().get(tradeAccountKey);
        if (clientId == null || clientId.isEmpty()) {
            throw new BusinessException(ErrorCode.TRADE_ACCOUNT_VERIFICATION_FAILED, "未绑定交易账号");
        }

        // 3. 获取报表路径
        Map<String, ReportMapping> mappings = reportMappingsConfig.getMappings();
        String reportPath = mappings.get(reportCode).getPath();
        if (reportPath == null || reportPath.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_REPORT_CODE, "无效的报表代码");
        }

        // 2. 使用 SM4 加密交易账号
        String encryptedClientId = null;
        try {
            encryptedClientId = encryptionUtil.encryptSM4CBC(clientId);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "加密失败");
        }

        // 3. 获取SSOToken，如果redis不存在则构建 SSOToken 的 JSON 字符串
        String biSsoTokenKey = "bi_ssotoken";
        String biSsoToken = (String) redisTemplate.opsForValue().get(biSsoTokenKey);
        if(biSsoToken == null || biSsoToken.isEmpty()) {
            // 直接获取上海时区的当前时间
            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
            long timestamp = zonedDateTime.toEpochSecond();  // 转为时间戳

            String ssoTokenJson = String.format("{\"domainId\":\"%s\",\"externalUserId\":\"%s\",\"timestamp\":%d}", domainId, externalUserId, timestamp);
            //使用 RSA 私钥加密 SSOToken
            biSsoToken= encryptionUtil.toHexString(encryptionUtil.encryptRSAPrivate(ssoTokenJson));
            System.out.println(domainId);
            System.out.println(externalUserId);
            System.out.println(timestamp);
            System.out.println(biSsoToken);
            //将ssotoken存储到redis
            redisTemplate.opsForValue().set(biSsoTokenKey, biSsoToken,4, TimeUnit.MINUTES);
        }


        // 5. 构建 BI 报表访问地址
        return String.format("%s/%s?%s=%s&provider=%s&ssoToken=%s&pref.HostNavOnly=true",
                biReportBaseUrl, reportPath,mappings.get(reportCode).getParameter_client_id(), encryptedClientId, domainId, biSsoToken);
    }

}

