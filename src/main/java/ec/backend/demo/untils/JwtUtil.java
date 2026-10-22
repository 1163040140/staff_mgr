package ec.backend.demo.untils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT 工具类
 * <p>
 * 使用 jjwt 0.11.5，HS256 算法生成与解析 Token。
 * 注意：HS256 要求签名密钥编码后至少 256 bit（32 字节）。
 * 当前密钥 "staff_mgr_secret_key" 仅 20 字节，如运行时抛出 WeakKeyException，
 * 请将密钥改为不少于 32 字节的字符串。
 */
@Component
public class JwtUtil {

    private static final String SECRET_KEY = "staff_mgr_secret_key_java_sys_user";
    private static final long EXPIRATION_TIME = 2 * 60 * 60 * 1000L; // 2 小时（毫秒）

    private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    /**
     * 生成 JWT Token。
     *
     * @param userId   用户 ID
     * @param username 用户名
     * @return 已签名的 JWT 字符串
     */
    public String generateToken(Long userId, String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 JWT Token，返回 Claims。
     * 如果 Token 伪造、被篡改或已过期，会抛出对应异常
     *（如 ExpiredJwtException、SignatureException 等）。
     *
     * @param token JWT 字符串
     * @return Claims 载荷对象
     */
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
