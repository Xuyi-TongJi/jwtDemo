package edu.seu.jwtdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * Jwt存储配置类
 * @author xuyitjuseu
 */
@Configuration
public class JwtTokenStoreConfig {

    @Bean("jwtTokenStore")
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean("jwtTokenConverter")
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        // 配置jwt使用的秘钥
        jwtAccessTokenConverter.setSigningKey("test_key");
        return jwtAccessTokenConverter;
    }

    /**
     * 配置JwtToken增强器
     * @return JwtToken增强器
     */
    @Bean("tokenEnhancer")
    public TokenEnhancer jwtTokenEnhancer() {
        return new JwtTokenEnhancer();
    }
}
