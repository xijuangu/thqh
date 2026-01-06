package com.org.bddsserver.exception;

/**
 * @author PY.Lu
 * @date 2025/10/15
 * @Description
 */
public class BusinessException extends RuntimeException {
    private final Integer code;

    // 仅传递错误消息，错误码使用默认值
    // 错误码使用预定义的“通用错误”编码
    // 适用场景：需要抛出业务异常，但不需关注具体错误码
    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.BUSINESS_ERROR.getCode();
    }

    // 传递错误消息和错误码
    // 适用场景：需要抛出业务异常，完全自定义错误码和错误描述的场景
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    // 通过预定义的 ResultCode 枚举类，获取错误码和错误描述，并抛出业务异常
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    // 适用场景：使用预定义状态码但需要定义更详细的错误描述的场景
    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
