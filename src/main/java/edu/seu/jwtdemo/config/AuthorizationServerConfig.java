package edu.seu.jwtdemo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Oauth授权服务器配置类，授权模式
 * @author xuyitjuseu
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final TokenStore tokenStore;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtAccessTokenConverter accessTokenConverter;
    private final TokenEnhancer tokenEnhancer;

    public AuthorizationServerConfig(PasswordEncoder passwordEncoder, @Qualifier("jwtTokenStore") TokenStore tokenStore,
                                     UserDetailsService userDetailsService, AuthenticationManager authenticationManager,
                                     @Qualifier("jwtTokenConverter") JwtAccessTokenConverter accessTokenConverter,
                                     @Qualifier("tokenEnhancer") TokenEnhancer tokenEnhancer) {
        this.passwordEncoder = passwordEncoder;
        this.tokenStore = tokenStore;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.accessTokenConverter = accessTokenConverter;
        this.tokenEnhancer = tokenEnhancer;
    }

    /**
     * 授权服务器的（接受）客户端侧配置
     * @param clients 客户端
     * @throws Exception 所有可能抛出的异常
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                // 配置client-id，假设在内存
                .withClient("admin")
                // 配置client-secret. client-id和secret不是UserDetailsService中用于登录的账户username和密码password
                // ，而是客户端的id和secret
                .secret(passwordEncoder.encode("112233"))
                .accessTokenValiditySeconds(60)
                .refreshTokenValiditySeconds(3600)
                // 授权成功后跳转
                .redirectUris("http://localhost:8081/login")
                .scopes("all")
                .autoApprove(true)
                // 授权码模式为authorization_code，密码模式为password, 刷新令牌为refresh_token
                .authorizedGrantTypes("authorization_code", "password", "refresh_code");
    }

    /**
     * 端点配置
     * @param endpoints 授权服务器端点
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        // 设置token增强链
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(accessTokenConverter);
        delegates.add(tokenEnhancer);
        enhancerChain.setTokenEnhancers(delegates);

        endpoints
                .authenticationManager(authenticationManager)
                // 配置userDetailsService Bean
                .userDetailsService(userDetailsService)
                // 配置jwt存储方式
                .tokenStore(tokenStore)
                // 配置jwt转换器
                .accessTokenConverter(accessTokenConverter)
                // 配置token增强链
                .tokenEnhancer(enhancerChain);
    }

    /**
     * 使用外部客户端进行单点登录（SSO）时，必须重写该方法
     * @param security 配置对象
     * @throws Exception 抛出所有可能抛出的异常
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 获取密钥需要身份认证，使用单点登录时必须配置
        security.tokenKeyAccess("isAuthenticated()");
    }
}