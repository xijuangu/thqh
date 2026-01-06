package com.org.bddsserver.exception;
import com.org.bddsserver.dto.loginInfo;

import java.io.Serializable;

/**
 * @author PY.Lu
 * @date 2025/10/15
 * @Description：统一 API 响应结果封装
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化标识

    private Integer code; // 响应状态码
    private String message; // 响应消息
    private  T data; // 响应数据

    //构造函数
    public Result() {}

    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = (T) data; //此处如果不写会出现查询成功但是前端接受不到值的情况
    }


    // getter and setter
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 静态成功方法，只返回响应状态码和消息
     * @return
     * @param
     */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 静态成功方法，返回响应状态码、消息和数据
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> success(Integer code, String message) {
        return new Result<>(code, message, null);
    }
    public static <T> Result<T> success(String message) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, null);
    }

    /**
     * 静态失败方法，返回已在自定义响应枚举类中的状态码和消息
     * @return
     * @param
     */
    public static <T> Result<T> error() {
        return new Result<>(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMessage(), null);
    }

    /**
     * 静态失败方法，返回自定义消息或系统消息
     * @return
     * @param message
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(ResultCode.ERROR.getCode(), message, null);
    }


    /**
     * 静态失败方法，返回自定义的状态码和消息
     * @return
     * @param code,message
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }





}
