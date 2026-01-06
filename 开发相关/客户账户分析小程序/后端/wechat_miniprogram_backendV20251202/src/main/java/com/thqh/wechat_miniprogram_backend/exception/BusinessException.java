package com.thqh.wechat_miniprogram_backend.exception;

import lombok.Getter;

/**
 * @ClassName: BusinessException
 * @Description:
 * @Author liubin
 * @Date 2025/1/3 10:52
 * @Version V1.0
 */


@Getter
public class BusinessException extends RuntimeException {
    private final int code;
    private final String message;

    public BusinessException(ErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.code = errorCode.getCode();
        this.message = detailMessage;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
