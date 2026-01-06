package com.thqh.wechat_miniprogram_backend.annotation;

import java.lang.annotation.*;

/**
 * @author 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLog{
    /**
     * 功能代码，可留空动态从请求参数取
     */
    String functionCode() default ""; //
    /**
     * 是否启用日志记录，默认启用
     */
    boolean enabled() default true;
}

