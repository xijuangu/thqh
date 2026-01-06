package com.thqh.wechat_miniprogram_backend.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

/**
 * @ClassName: JwtUtil
 * @Description:
 * @Author liubin
 * @Date 2025/1/2 16:07
 * @Version V1.0
 */


@Component
public class JwtUtil {

    // 从配置文件中注入密钥和过期时间
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    private Key key;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);


    @PostConstruct
    public void init() {
        // 使用HS512算法生成密钥
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * 生成JWT令牌
     *
     * @param userId      用户ID
     * @return 生成的JWT令牌
     */
    public String generateJwtToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 验证JWT令牌
     *
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Error validateJwtToken():{}",e.getMessage());
        }
        return false;
    }

    /**
     * 从JWT令牌中获取用户ID
     *
     * @param token JWT令牌
     * @return 用户ID
     */
    public Long getUserIdFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    /**
     * 获取过期时间
     *
     * @param
     * @return 过期时间
     */
    public Integer getExpiration() {
        return this.jwtExpirationMs;
    }
}
