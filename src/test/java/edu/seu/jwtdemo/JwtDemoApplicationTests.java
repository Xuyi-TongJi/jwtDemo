package edu.seu.jwtdemo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64Codec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Format;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class JwtDemoApplicationTests {

    @Test
    public void testCreateToken() {
        long current = System.currentTimeMillis();
        long exp = current + 60 * 1000;
        JwtBuilder jwtBuilder = Jwts.builder()
                // 声明的标识("jti": "8888")
                .setId("8888")
                // 主体，用户("sub": "Rose")
                .setSubject("Rose")
                // 创建日期("ita": "xxxx")
                .setIssuedAt(new Date())
                // 设置过期时间
                .setExpiration(new Date(exp))
                // 算法和盐(secret)
                .signWith(SignatureAlgorithm.HS256, "xxxx");
        String token = jwtBuilder.compact();
        System.out.println(token);
        System.out.println("--------------");
        String[] split = token.split("\\.");
        // 打印头部信息
        System.out.println(Base64Codec.BASE64.decodeToString(split[0]));
        // 打印荷载信息
        System.out.println(Base64Codec.BASE64.decodeToString(split[1]));
        // 签名信息无法解密，因为盐无法获取
        System.out.println(Base64Codec.BASE64.decodeToString(split[2]));

        System.out.println("--------------");
        // 解析token 过期失效或者盐错误都会直接抛出异常
        Claims claims = Jwts.parser()
                .setSigningKey("xxxx")
                .parseClaimsJws(token)
                .getBody();
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        System.out.println(claims.getIssuedAt());
        System.out.println(claims.getExpiration());
    }

    /**
     * 自定义Token声明
     */
    @Test
    public void testDiyTokenClaims() {
        JwtBuilder jwtBuilder = Jwts.builder()
                // 声明的标识("jti": "8888")
                .setId("8888")
                // 主体，用户("sub": "Rose")
                .setSubject("Rose")
                // 创建日期("ita": "xxxx")
                .setIssuedAt(new Date())
                // 自定义声明，k-v形式或者直接传入map
                .claim("roles", "admin")
                // 算法和盐(secret)
                .signWith(SignatureAlgorithm.HS256, "xxxx");
    }
}
