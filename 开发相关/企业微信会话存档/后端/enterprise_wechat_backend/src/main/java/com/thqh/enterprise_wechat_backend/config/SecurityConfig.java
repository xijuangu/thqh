package com.thqh.enterprise_wechat_backend.config;


import com.thqh.enterprise_wechat_backend.filter.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @ClassName: SecurityConfig
 * @Description:
 * @Author liubin
 * @Date 2025/1/2 15:10
 * @Version V1.0
 */



@Configuration
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    public SecurityConfig(JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authConfig) throws Exception {
        http
                // 禁用 CSRF，因为我们使用的是无状态的REST API
                .csrf().disable()
                // 采用无状态会话管理
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 配置请求授权
                .authorizeRequests()
                // 允许OPTIONS方法请求通过（CORS预检）
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 允许匿名访问获取手机号的接口
                .antMatchers("/api/user/login").permitAll()
                // 允许Swagger UI访问（如果有使用Swagger）
//                .antMatchers(
//                        "/v2/api-docs",
//                        "/v3/api-docs",
//                        "/swagger-resources/**",
//                        "/swagger-ui/**",
//                        "/webjars/**"
//                ).permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
                .and()
                // 添加JWT授权过滤器
                .addFilterBefore(jwtAuthorizationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
