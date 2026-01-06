package com.thqh.enterprise_wechat_backend.exception;

import com.thqh.enterprise_wechat_backend.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.NoSuchElementException;

/**
 * @ClassName: GlobalExceptionHandler
 * @Description:
 * @Author liubin
 * @Date 2025/3/4 15:15
 * @Version V1.0
 */


@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理所有未捕获的异常
     *
     * @param ex 异常对象
     * @return ApiResponse对象
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex) {
        logger.error("发生异常: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ApiResponse.failure(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理特定的异常，例如 NoSuchElementException
     *
     * @param ex 异常对象
     * @return ApiResponse对象
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoSuchElementException(NoSuchElementException ex) {
        logger.error("资源未找到: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.failure(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage()), HttpStatus.NOT_FOUND);
    }

    // 其他特定异常的处理方法...

    /**
     * 处理自定义业务异常
     *
     * @param ex 自定义业务异常
     * @return ApiResponse对象
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException ex) {
        logger.error("业务异常: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.failure(ex.getCode(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
