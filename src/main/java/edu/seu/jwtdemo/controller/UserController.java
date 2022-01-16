package edu.seu.jwtdemo.controller;

import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * 测试Controller, 必须有令牌才能访问
 * @author xuyitjuseu
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getCurrentUser")
    public Object getCurrentUser(Authentication authentication) {
        return authentication.getPrincipal();
    }

    /**
     * 解析jwt令牌接口
     * @param request HttpServletRequest 用以获取Authorization请求头
     * @return 解析后的Json对象，restful接口
     */
    @RequestMapping("/decode")
    public Object decode(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        // 获取jwt令牌(bearer token 从第七位开始是令牌)
        String token = authorization.substring(authorization.indexOf("bearer") + 7);
        return Jwts
                .parser()
                // 盐(私钥)
                .setSigningKey("test_key".getBytes(StandardCharsets.UTF_8))
                // 获取jwt令牌的Claims
                .parseClaimsJws(token)
                .getBody();
    }
}
