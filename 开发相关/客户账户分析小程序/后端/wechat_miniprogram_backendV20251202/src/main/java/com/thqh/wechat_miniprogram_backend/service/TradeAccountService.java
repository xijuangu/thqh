package com.thqh.wechat_miniprogram_backend.service;

import com.thqh.wechat_miniprogram_backend.dto.TradeAccountLoginRequest;
import com.thqh.wechat_miniprogram_backend.dto.TradeAccountStatusResponse;
import com.thqh.wechat_miniprogram_backend.entity.User;
import com.thqh.wechat_miniprogram_backend.exception.BusinessException;
import com.thqh.wechat_miniprogram_backend.exception.ErrorCode;
import com.thqh.wechat_miniprogram_backend.util.EncryptionUtil;
import com.thqh.wechat_miniprogram_backend.util.RsaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.nio.charset.Charset;
import java.io.*;
import java.net.Socket;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName: TradeAccountService
 * @Description:
 * @Author liubin
 * @Date 2025/1/6 17:28
 * @Version V1.0
 */


@Service
public class TradeAccountService {

    @Value("${tradeaccount.socket.ip}")
    private String socketIp;

    @Value("${tradeaccount.socket.port}")
    private int socketPort;

    @Value("${tradeaccount.socket.orgcode}")
    private String orgCode;

    @Value("${tradeaccount.socket.appid}")
    private String appId;

    @Value("${tradeaccount.rsa.publicKey}")
    private String publicKey;
    @Value("${tradeaccount.rsa.privateKey}")
    private String privateKey;
    private static final int MSG_MAXLEN = 600;

    private final EncryptionUtil encryptionUtil;

    private final UserService userService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(TradeAccountService.class);


    @Autowired
    public TradeAccountService(EncryptionUtil encryptionUtil, UserService userService) {
        this.encryptionUtil = encryptionUtil;
        this.userService = userService;
    }

    /**
     * 处理交易账号登录验证
     *
     * @param request
     * @return 交易账号（登录成功）
     */
    public TradeAccountStatusResponse login(TradeAccountLoginRequest request) {
        //绑定用户id与客户交易账号
        User user = userService.getUserByJwt();
        if (user == null) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无效的认证信息");
        }
        String decryptStr = "";
        try {
            decryptStr = RsaUtil.decrypt(request.getPassword(),privateKey);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.TRADE_ACCOUNT_DECRYPT_FAILED, "请求处理失败");
        }
        request.setPassword(decryptStr);

        // 用于记录 R|||6011 的成功状态
        boolean loginSuccess = false;
        TradeAccountStatusResponse tradeAccountStatusResponse = new TradeAccountStatusResponse();
        try (Socket socket = new Socket()) {
            // 设置Socket连接超时为5秒
            socket.connect(new java.net.InetSocketAddress(socketIp, socketPort), 5000);
            // 设置Socket读取超时为5秒
            socket.setSoTimeout(5000);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("GB2312")));
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("GB2312")))) {

                // 1. <查询 APPID 相关信息> R|||6295|(2)||(4)|(5)|(6)
                //（1）功能号：6295（2）营业部代码（3）委托方式（4）客户号（5）客户密码（6）终端或中继标识（APPID，必填）
                String appIdReqMsg = String.format("R|||6295|%s||%s|%s|%s", orgCode, request.getTradeAccount(), request.getPassword(), appId);
                logger.info("发送消息: {}", String.format("R|||6295|%s||%s|%s", orgCode, request.getTradeAccount(), appId));
                String appIdRespMsg = sendAndReceive(writer, reader, appIdReqMsg);
                String[] appIdInfo = appIdRespMsg.split("\\|");
                if (!"Y".equals(appIdInfo[3])) {
                    logger.error("query appid info failure: {}", appIdRespMsg);
                    throw new BusinessException(ErrorCode.TRADE_ACCOUNT_VERIFICATION_FAILED, "交易账号验证失败");
                }
                // 2. 解析初始响应，提取 gatewayTime 和 timestamp
                String gatewayTime = null;
                String encryptGatewayTime = null;
                String timestamp = null;
                // 确保索引存在
                if (appIdInfo.length >= 11) {
                    gatewayTime = appIdInfo[10];
                }
                if (appIdInfo.length >= 16) {
                    timestamp = appIdInfo[15];
                }

                if (gatewayTime != null) {
                    logger.info("Gateway computer time: {}", gatewayTime);
                    encryptGatewayTime = encryptionUtil.encryptAes256Cbc(gatewayTime);
                    logger.info("Encrypted Gateway computer time: {}", encryptGatewayTime);
                } else {
                    logger.error("gatewayTime is null: {}", appIdRespMsg);
                    throw new BusinessException(ErrorCode.INITIAL_RESPONSE_MISSING_GATEWAY_TIME, "初始响应缺少 gatewayTime");
                }

                if (timestamp == null) {
                    logger.error("timestamp is null: {}", appIdRespMsg);
                    throw new BusinessException(ErrorCode.INITIAL_RESPONSE_MISSING_TIMESTAMP, "初始响应缺少 timestamp");
                } else {
                    logger.info("Timestamp sequence number: {}", timestamp);
                }

                // 3. <看穿式认证> R|||6296|AAA||BBB1|CCC1|DDD1|{timestamp}|{encryptGatewayTime}
                //（1）功能号：6296（2）营业部代码（3）委托方式（4）客户号（5）客户密码（6）终端或中继标识（APPID，必填）（7）时间戳序号（6295 返回的随机数明文的时间戳序号 (字段 13)，必填）（8）随机数密文（通过授权码对 6295 返回的随机数明文 (字段 8)加密后的密文，base64
                //格式，必填）
                String authReqMsg = String.format("R|||6296|%s||%s|%s|%s|%s|%s", orgCode, request.getTradeAccount(), request.getPassword(), appId, timestamp, encryptGatewayTime);
                logger.info("发送消息: {}", String.format("R|||6296|%s||%s|%s|%s|%s", orgCode, request.getTradeAccount(), appId, timestamp, encryptGatewayTime));
                String authRespMsg = sendAndReceive(writer, reader, authReqMsg);
                String[] authInfo = authRespMsg.split("\\|");
                if (!"Y".equals(authInfo[3])) {
                    logger.error("<看穿式认证>: {}", authRespMsg);
                    throw new BusinessException(ErrorCode.TRADE_ACCOUNT_VERIFICATION_FAILED, "交易账号验证失败");
                }

                // 4. <客户登陆> R|||6011|AAA||BBB1|CCC1|180.165.191.125|1|1|
                //（1）功能号 6011（2）营业部代码（3）委托方式（4）客户号（5）客户密码（6）登录 IP 地址（填客户端 IP 地址）
                //（7）是否接收成交推送[1：推送，空：不退送]（仅用于 B2C 推送）（8）是否接收信息推送[1：推送，空：不退送]（仅用于 B2C 推送）（9）登录类型 [空 = 普通登录；3 = 服务器认证]
                String loginMsg = String.format("R|||6011|%s||%s|%s|%s|||", orgCode, request.getTradeAccount(), request.getPassword(), request.getClientIp());
                logger.info("发送消息: {}",String.format("R|||6011|%s||%s|%s|||", orgCode, request.getTradeAccount(), request.getClientIp()));
                String loginRespMsg = sendAndReceive(writer, reader, loginMsg);

                // 5. 检查 R|||6011 响应是否成功
                String[] loginTokens = loginRespMsg.split("\\|");
                if ("Y".equals(loginTokens[3])) {
                    logger.info("交易账号登录成功");
                    loginSuccess = true;
                } else {
                    logger.error("交易账号登录失败: {}", loginRespMsg);
                    throw new BusinessException(ErrorCode.TRADE_ACCOUNT_VERIFICATION_FAILED, "交易账号验证失败");
                }

                // 6. <终端信息采集上报> R|||6297|AAA||BBB1|CCC1|180.165.191.125|1|1|
                //（1）功能号 6297（2）营业部代码（3）委托方式（4）客户号（5）客户密码（6）终端标识（APPID）（7）中继标识（RelayAPPID）（8）中继异常标识
                //（9）终端公网 IP（10）终端公网端口（11）终端登入时间（12）客户登录的会话 ID（13）终端采集信息密文（由采集 API 提供）
                String terminalReqMsg = String.format("R|||6297|%s||%s|%s|%s|||%s|%s|%s||%s", orgCode, request.getTradeAccount(), request.getPassword(), appId,socketIp,socketPort,gatewayTime,encryptGatewayTime);
                logger.info("发送消息: {}",String.format("R|||6297|%s||%s|%s|||%s|%s|%s||%s", orgCode, request.getTradeAccount(), appId,socketIp,socketPort,gatewayTime,encryptGatewayTime));
                String terminalRespMsg = sendAndReceive(writer, reader, terminalReqMsg);
                String[] terminalTokens = terminalRespMsg.split("\\|");
                if ("Y".equals(terminalTokens[3]) & "0".equals(terminalTokens[4])) {
                    logger.info("<终端信息采集上报>成功");
                } else {
                    logger.error("<终端信息采集上报失败>: {}", terminalRespMsg);
                }
                // 7. <客户登出>发送 R|||6061|||BBB1|CCC1|
                //（1）功能号 6061（2）营业部代码（3）委托方式（4）客户号（5）客户密码（6）在线时间[秒]（7）会话 ID（8）客户类型（9）管理帐号
                String logoutReqMsg = String.format("R|||6061|%s||%s|%s|", orgCode, request.getTradeAccount(), request.getPassword());
                logger.info("发送消息: {}",String.format("R|||6061|%s||%s||", orgCode, request.getTradeAccount()));
                String logoutRespMsg = sendAndReceive(writer, reader, logoutReqMsg);
                String[] logoutTokens = logoutRespMsg.split("\\|");
                if ("Y".equals(logoutTokens[3])) {
                    logger.info("<客户登出>成功");
                } else {
                    logger.error("<客户登出>失败: {}", logoutRespMsg);
                }
                // 8. 根据 R|||6011 的结果决定是否返回成功
                if (loginSuccess) {
                    //绑定用户id与客户交易账号
                    String tradeAccountKey = "user:trade_account:" + user.getId();
                    String expirationTimeKey = "user:trade_account_expiration_time:" + user.getId();
                    // 获取当前时间+3小时
                    LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
                    LocalDateTime futureTime = now.plusHours(3);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedTime = futureTime.format(formatter);
                    redisTemplate.opsForValue().set(tradeAccountKey, request.getTradeAccount(), 3, TimeUnit.HOURS);
                    redisTemplate.opsForValue().set(expirationTimeKey, formattedTime, 3, TimeUnit.HOURS);
                    tradeAccountStatusResponse.setTradeAccount(request.getTradeAccount());
                    tradeAccountStatusResponse.setExpirationTime(formattedTime);
                } else {
                    throw new BusinessException(ErrorCode.TRADE_ACCOUNT_VERIFICATION_FAILED, "交易账号验证失败");
                }
            }
        } catch (IOException e) {
            logger.error("Socket通信异常: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SOCKET_COMMUNICATION_ERROR, "Socket通信异常: " + e.getMessage());
        }
        return tradeAccountStatusResponse;
    }

    /**
     * 发送消息并接收响应
     *
     * @param writer    BufferedWriter
     * @param reader    BufferedReader
     * @param clientMsg 发送的消息
     * @return 服务器响应
     */
    private String sendAndReceive(BufferedWriter writer, BufferedReader reader, String clientMsg) {
        try {
            // 发送消息
            writer.write(clientMsg);
            writer.newLine();
            writer.flush();
//            logger.info("发送消息: {}", clientMsg);

            // 接收响应
            char[] buffer = new char[MSG_MAXLEN];
            int read = reader.read(buffer, 0, MSG_MAXLEN);
            if (read == -1) {
                logger.error("接收消息失败");
                throw new BusinessException(ErrorCode.SERVER_CLOSED_CONNECTION, "服务器关闭连接");
            }
            String serverMsg = new String(buffer, 0, read).trim();
            logger.info("接收响应: {}", serverMsg);
            return serverMsg;
        } catch (IOException e) {
            logger.error("发送或接收消息失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SEND_OR_RECEIVE_MESSAGE_FAILED, "发送或接收消息失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户绑定的交易账号及失效时间
     *
     * @return
     */
    public TradeAccountStatusResponse getTradeAccountStatus() {
        //获取用户id
        User user = userService.getUserByJwt();
        //查询redis该用户id对应的客户交易账号及失效时间
        String tradeAccountKey = "user:trade_account:" + user.getId();
        String tradeAccount = (String) redisTemplate.opsForValue().get(tradeAccountKey);
        String expirationTimeKey = "user:trade_account_expiration_time:" + user.getId();
        String expirationTime = (String) redisTemplate.opsForValue().get(expirationTimeKey);
        logger.info("获取用户交易账号及失效时间：" + tradeAccountKey+":{},"+ expirationTimeKey +":{}",tradeAccount,expirationTime);
        //返回结果
        TradeAccountStatusResponse tradeAccountStatusResponse = new TradeAccountStatusResponse();
        tradeAccountStatusResponse.setTradeAccount(tradeAccount);
        tradeAccountStatusResponse.setExpirationTime(expirationTime);
        return tradeAccountStatusResponse;
    }


    /**
     * 解绑交易账号
     *
     * @return
     */
    public TradeAccountStatusResponse logoutTradeAccount() {
        //获取用户id
        User user = userService.getUserByJwt();
        //查询redis该用户id对应的客户交易账号及失效时间
        String tradeAccountKey = "user:trade_account:" + user.getId();
        String tradeAccount = (String) redisTemplate.opsForValue().get(tradeAccountKey);
        String expirationTimeKey = "user:trade_account_expiration_time:" + user.getId();
        String expirationTime = (String) redisTemplate.opsForValue().get(expirationTimeKey);
        logger.info("删除用户交易账号及失效时间：" + tradeAccountKey+":{},"+ expirationTimeKey +":{}",tradeAccount,expirationTime);
        if(tradeAccount!=null){
            redisTemplate.delete(tradeAccountKey);
        }
        if(expirationTime!=null){
            redisTemplate.delete(expirationTimeKey);
        }
        //返回结果
        return new TradeAccountStatusResponse();
    }

}
