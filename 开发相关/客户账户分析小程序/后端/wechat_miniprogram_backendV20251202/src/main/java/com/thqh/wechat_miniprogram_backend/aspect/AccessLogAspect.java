package com.thqh.wechat_miniprogram_backend.aspect;

import com.thqh.wechat_miniprogram_backend.annotation.AccessLog;
import com.thqh.wechat_miniprogram_backend.entity.UserAccessLog;
import com.thqh.wechat_miniprogram_backend.service.UserAccessLogService;
import com.thqh.wechat_miniprogram_backend.util.JwtUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @ClassName: AccessLogAspect
 * @Description:
 * @Author
 * @Date 2025/10/23 16:00
 * @Version V1.0
 */
@Aspect
@Component
public class AccessLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(AccessLogAspect.class);
    private final HttpServletRequest request;
    private final UserAccessLogService accessLogService;
    private final JwtUtil jwtUtil;


    public AccessLogAspect(HttpServletRequest request, UserAccessLogService accessLogService, JwtUtil jwtUtil) {
        this.request = request;
        this.accessLogService = accessLogService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 切点：拦截 controller 包下所有方法（排除 /api-wx/stats）
     */
    @Pointcut("execution(* com.thqh.wechat_miniprogram_backend.controller..*(..))")
    public void controllerMethods() {}



    /**
     * 环绕通知：记录访问日志，无论方法正常或异常
     */
    @Around("controllerMethods()")
    public Object aroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        Throwable ex = null;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            ex = e;
            throw e;
        } finally {
            try {
                logAccess(joinPoint, result, ex);
            } catch (Exception logEx) {
                logger.error("访问日志记录异常: {}", logEx.getMessage(), logEx);
            }
        }
    }

    /**
     * 日志记录逻辑
     */
    private void logAccess(ProceedingJoinPoint joinPoint, Object result, Throwable ex) throws Exception {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        AccessLog annotation = method.getAnnotation(AccessLog.class);

        // 如果注解存在且 enabled = false，则不记录日志
        if (annotation != null && !annotation.enabled()) {
            return;
        }

        String functionCode = null;
        String tradeAccount = null;

        // 1️⃣ 优先使用注解
        if (annotation != null && !annotation.functionCode().isEmpty()) {
            functionCode = annotation.functionCode();
        }

        Object[] args = joinPoint.getArgs();

        // 2️⃣ 从请求体对象中提取 reportCode
        if (functionCode == null) {
            for (Object arg : args) {
                if (arg != null) {
                    try {
                        Method getReportCode = arg.getClass().getMethod("getReportCode");
                        Object val = getReportCode.invoke(arg);
                        if (val != null) {
                            functionCode = val.toString();
                            break;
                        }
                    } catch (NoSuchMethodException ignore) {
                    }
                }
            }
        }

        // 3️⃣ 获取交易账号
        for (Object arg : args) {
            if (arg != null) {
                try {
                    Method getTradeAccount = arg.getClass().getMethod("getTradeAccount");
                    Object val = getTradeAccount.invoke(arg);
                    if (val != null) {
                        tradeAccount = val.toString();
                        break;
                    }
                } catch (NoSuchMethodException ignore) {
                }
            }
        }

        // 4️⃣ fallback functionCode
        if (functionCode == null) {
            functionCode = request.getRequestURI();
        }

        // 5️⃣ 从 JWT Token 获取 userId
        Long userId = getCurrentUserId(request);
        // ✅ 特殊处理 LOGIN 功能（认证后才有 userId）
        if ("LOGIN".equalsIgnoreCase(functionCode) && (userId == null || userId == 0L)) {
            userId = extractUserIdFromResult(result);
        }
        String clientIp = getClientIp(request);

        LocalDateTime now = LocalDateTime.now();
        // 登录失败时不记录成功日志
        boolean isSuccess = (ex == null);

        // 6️⃣ 构建日志对象
        UserAccessLog logEntity = new UserAccessLog();
        logEntity.setUserId(userId);
        logEntity.setAccessTime(now);
        logEntity.setFunctionCode(functionCode);
        logEntity.setClientIp(clientIp);
        logEntity.setTradeAccount(tradeAccount);
        logEntity.setCreatedTime(now);
        logEntity.setCreatedBy(userId);
        logEntity.setUpdatedTime(now);
        logEntity.setUpdatedBy(userId);

        if (isSuccess) {
            logEntity.setAccessStatus(1);
            logEntity.setErrorMessage(null);
            logger.info("[访问日志][成功] userId={}, func={}, ip={}, tradeAccount={}",
                    userId, functionCode, clientIp, tradeAccount);
        } else {
            logEntity.setAccessStatus(0);
            String errMsg = ex.getMessage();
            if (errMsg != null && errMsg.length() > 950) {
                // 防止太长写爆数据库
                errMsg = errMsg.substring(0, 950) + "...";
            }
            logEntity.setErrorMessage(errMsg);
            logger.error("[访问日志][失败] userId={}, func={}, ip={}, tradeAccount={}, error={}",
                    userId, functionCode, clientIp, tradeAccount, errMsg);
        }

        accessLogService.save(logEntity);
    }

    /**
     * 从 JWT / Header 获取用户ID
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                return jwtUtil.getUserIdFromJwtToken(token);
            }
        } catch (Exception e) {
            logger.error("JWT解析失败: {}", e.getMessage());
        }
        return 0L;
    }
    /**
     * 从返回结果中提取 userId（用于 LOGIN 接口）
     */
    private Long extractUserIdFromResult(Object result) {
        if (result == null) return 0L;
        try {
            // 如果是 ResponseEntity<ApiResponse<GetUserInfoWithJwt>>
            if (result instanceof ResponseEntity) {
                Object body = ((ResponseEntity<?>) result).getBody();
                if (body != null && hasField(body, "data")) {
                    Object data = getFieldValue(body, "data");
                    if (data != null && hasField(data, "userId")) {
                        Object uid = getFieldValue(data, "userId");
                        if (uid != null) return Long.parseLong(uid.toString());
                    }
                }
            }
            // 如果直接是 GetUserInfoWithJwt
            if (hasField(result, "userId")) {
                Object uid = getFieldValue(result, "userId");
                if (uid != null) return Long.parseLong(uid.toString());
            }
        } catch (Exception e) {
            logger.warn("提取 userId 失败: {}", e.getMessage());
        }
        return 0L;
    }
    private boolean hasField(Object obj, String fieldName) {
        try {
            obj.getClass().getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
    private Object getFieldValue(Object obj, String fieldName) throws IllegalAccessException, NoSuchFieldException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    /**
     * 获取客户端 IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0];
        }
        String remoteAddr = request.getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equals(remoteAddr)) {
            remoteAddr = "127.0.0.1";
        }
        return remoteAddr;
    }
}

