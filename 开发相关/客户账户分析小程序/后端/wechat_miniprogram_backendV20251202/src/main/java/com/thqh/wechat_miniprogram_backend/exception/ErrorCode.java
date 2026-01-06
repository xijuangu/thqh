package com.thqh.wechat_miniprogram_backend.exception;

/**
 * @ClassName: ErrorCode
 * @Description:
 * @Author liubin
 * @Date 2025/1/3 10:54
 * @Version V1.0
 */

public enum ErrorCode {
    SUCCESS(0, ""),
    //微信平台相关错误代码
    ACCESS_TOKEN_FAILED(1001, "获取access_token失败"),
    PHONE_NUMBER_FAILED(1002, "获取手机号失败"),
    PHONE_INFO_NULL(1003, "获取手机号信息失败"),
    CODE_TO_SESSION_FAILED(1004, "登录凭证校验失败"),
    TYPE_VALUE_ERROR(1005, "值类型错误"),

    // 新增的交易账号相关错误代码
    TRADE_ACCOUNT_DECRYPT_FAILED(2003, "请求处理失败"),
    TRADE_ACCOUNT_VERIFICATION_FAILED(2004, "交易账号验证失败"),
    SOCKET_COMMUNICATION_ERROR(2005, "Socket通信异常"),
    SERVER_CLOSED_CONNECTION(2006, "服务器关闭连接"),
    SEND_OR_RECEIVE_MESSAGE_FAILED(2007, "发送或接收消息失败"),
    INITIAL_RESPONSE_MISSING_GATEWAY_TIME(2008, "初始响应缺少 gatewayTime"),
    INITIAL_RESPONSE_MISSING_TIMESTAMP(2009, "初始响应缺少 timestamp"),
    //报表相关错误
    INVALID_REPORT_CODE(1101, "无效的报表代码"),

    UNAUTHORIZED(401, "未认证的请求"),
    FORBIDDEN(403, "无效的认证信息"),
    NOT_FOUND(404, "资源未找到"),
    INTERNAL_SERVER_ERROR(500, "内部服务器错误");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}


