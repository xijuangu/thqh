package com.thqh.enterprise_wechat_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: ApiResponse
 * @Description:
 * @Author liubin
 * @Date 2025/3/4 15:17
 * @Version V1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    // 0表示成功，其他值表示失败
    private int code;
    // 成功时为空，失败时包含错误信息
    private String msg;
    // 其他数据
    private T data;

    // 静态方法用于快速创建响应

    /**
     * 成功响应，data为返回的数据
     *
     * @param data 需要返回的数据
     * @param <T>  数据类型
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, "", data);
    }


    /**
     * 失败响应，包含错误代码和错误信息
     *
     * @param code 错误代码
     * @param msg  错误信息
     * @param <T>  数据类型
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> failure(int code, String msg) {
        return new ApiResponse<>(code, msg, null);
    }
}

