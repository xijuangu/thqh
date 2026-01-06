package com.thqh.wechat_miniprogram_backend.filter;

import com.thqh.wechat_miniprogram_backend.service.CustomUserDetailsService;
import com.thqh.wechat_miniprogram_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
/**
 * @ClassName: JwtAuthorizationFilter
 * @Description:
 * @Author liubin
 * @Date 2025/1/2 16:31
 * @Version V1.0
 */

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtAuthorizationFilter(CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = null;
        Long userId = null;

        // JWT通常在Authorization头中，格式为"Bearer token"
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            if (jwtUtil.validateJwtToken(token)) {
                userId = jwtUtil.getUserIdFromJwtToken(token);
            }
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 加载用户详细信息
            UserDetails userDetails = userDetailsService.loadUserById(userId);
            if (userDetails != null) {
                // 创建认证令牌
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // 设置认证详情
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 将认证信息存储到Security上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}


