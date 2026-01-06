package com.thqh.enterprise_wechat_backend.util;

import com.thqh.enterprise_wechat_backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
    private  String jwtSecret;

    @Value("${jwt.expiration}")
    private  int jwtExpiration;

    private  Key key;

    @PostConstruct
    public void init() {
        // 使用HS512算法生成密钥
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    private  final Logger logger = LoggerFactory.getLogger(JwtUtil.class);


    /**
     * 生成JWT令牌
     *
     * @return 生成的JWT令牌
     */
    public  String generateJwtToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration * 1000L);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("username", user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }


    /**
     * 解析 JWT 令牌，返回 Claims 对象，其中包含 token 内的所有声明信息
     * @param token JWT 令牌
     * @return Claims 对象
     */
    public  Claims parseToken(String token) {
        return Jwts.parserBuilder()
                // 设置签名密钥
                .setSigningKey(key)
                // 构建 JwtParser 对象
                .build()
                // 解析 JWT 并返回 Jws 对象，然后获取其中的 Claims
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证JWT令牌
     *
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateJwtToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("JWT 令牌已过期:{}",e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT 令牌不支持:{}",e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("JWT 令牌格式错误:{}",e.getMessage());
        }  catch (IllegalArgumentException e) {
            logger.error("JWT 令牌为空或包含空字符串::{}",e.getMessage());
        } catch (JwtException e) {
            // 记录异常或处理
            logger.error("JwtException:",e);
        }
        return false;
    }

    /**
     * 从 JWT 令牌中提取用户 ID（保存在 subject 中）
     * @param token JWT 令牌
     * @return 用户ID
     */
    public  Integer getUserId(String token) {
        Claims claims = parseToken(token);
        return Integer.valueOf(claims.getSubject());
    }

    /**
     * 从 JWT 令牌中提取用户名（自定义声明 "username"）
     * @param token JWT 令牌
     * @return 用户名
     */
    public  String getUsername(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }

    /**
     * 获取jwt失效时间(秒)
     * @return
     */
    public int getExpire() {
        return jwtExpiration;
    }
}
