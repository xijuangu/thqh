package com.thqh.enterprise_wechat_backend.exception;

/**
 * @ClassName: ErrorCode
 * @Description:
 * @Author liubin
 * @Date 2025/3/4 15:15
 * @Version V1.0
 */

public enum ErrorCode {
    SUCCESS(0, ""),
    //微信平台相关错误代码
    INIT_SDK_ERROR(1001, "SDK初始化失败"),
    GET_CHATDATA_ERROR(1002, "获取会话存档数据失败"),
    DECRYPT_CHATDATA_ERROR(1003, "解密会话存档数据失败"),
    //用户相关
    USER_NOT_EXISTS(2001,"用户不存在"),
    USER_IS_DISABLED(2002,"用户已禁用"),
    PASSWORD_ERROR(2003,"密码错误"),
    LOGIN_FAILURE(2003,"登录失败"),
    //消息内容相关
    SDKFILEID_NOT_EXISTS(3001,"sdkfileid不存在"),
    MEDIA_NOT_EXISTS(3002,"附件未从企微下载成功"),
    PRE_MSGID_NOT_EXISTS(3003,"pre_msgid不存在"),
    //服务器相关
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


