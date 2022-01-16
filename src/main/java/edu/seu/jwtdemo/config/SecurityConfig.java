package edu.seu.jwtdemo.config;

import edu.seu.jwtdemo.handler.MyAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security配置类
 * @author xuyitjuseu
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyAccessDeniedHandler myAccessDeniedHandler;

    public SecurityConfig(MyAccessDeniedHandler myAccessDeniedHandler) {
        this.myAccessDeniedHandler = myAccessDeniedHandler;
    }

    /**
     * 密码加密器
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder getPw() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 配置httpSecurity，SpringSecurityConfig的核心方法
     *
     * @param http httpSecurity类
     * @throws Exception 异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 表单提交
        http.formLogin()
                // 当发现/login时，认为是登录，必须和表单提交的地址相同，去执行UserDetailsServiceImpl
                .loginProcessingUrl("/login")
                // 自定义登录页面
                .loginPage("/toLogin")
                /*// 登录成功，失败时跳转的页面,必须是Post请求,直接跳转为get请求，因此需要通过Controller实现重定向
                .successForwardUrl("/toMain")
                .failureForwardUrl("/toError");*/
                // successForwardUrl和successHandler不能共存，后者常用于前后端分离站外跳转
                /*.successHandler(new MyAuthenticationSuccessHandler("/toMain"))
                .failureHandler(new MyAuthenticationFailureHandler("/toLogin"))*/;
        // 授权认证
        http.authorizeRequests()
                // /toLogin不需要被认证
                .antMatchers("/login.html", "/toLogin").permitAll()
                .antMatchers("/oauth/**").permitAll()
                // 放行所有静态资源
                .antMatchers("/js/**", "/css/**", "/images/**").permitAll()
                // 放行所有png
                .antMatchers("/**/*.png").permitAll()
                .antMatchers("/main.html").hasAuthority("admin")
/*                .antMatchers("/main1.html").hasAnyAuthority("admin", "normal")
                .antMatchers("/main2.html").hasAnyRole("a", "b")
                .antMatchers("/main.html").hasIpAddress("127.0.0.1")*/
                // 所有请求都必须被认证
                .anyRequest().authenticated();

        // 关闭csrf(跨站请求访问)保护
        http.csrf().disable();

        // 异常处理， 自定义403(权限不足)处理方式
        http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);

        // 退出登录,也可以自定义logoutSuccessHandler,一般使用默认
        http.logout()
                .logoutUrl("/logout")
                // 退出登录跳转页面
                .logoutSuccessUrl("/toLogin");
    }
}
