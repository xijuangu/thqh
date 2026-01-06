package org.example.processserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author PY.Lu
 * @date 2025/8/12
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }
    public static <T> ApiResponse<T> fail(int code, T data) {
        return new ApiResponse<>(code, "fail", data);
    }
}
