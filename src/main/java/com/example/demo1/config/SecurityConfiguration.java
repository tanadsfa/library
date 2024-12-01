package com.example.demo1.config;


import com.example.demo1.filter.TokenFilter;
import com.example.demo1.handler.AuthenticationExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;



@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private AuthenticationExceptionHandler authenticationExceptionHandler;
    @Autowired
    private TokenFilter tokenFilter;
    @Autowired
    private DemoConfiguration.Security security;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        List<String> permitAllPaths = security.getPermitAllPath();
        // 配置不需要认证的请求(这里所有的路径可以写在配置文件上修改时就不用改代码)
        if (!CollectionUtils.isEmpty(permitAllPaths)) {
            permitAllPaths.forEach(path -> {
                try {
                    http.authorizeHttpRequests().requestMatchers(path).permitAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        // 关闭csrf因为不使用session
        http.cors().and().csrf().disable()
                // 不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                // 除了上面那些请求都需要认证
                .anyRequest().authenticated()
                .and()
                // 配置异常处理
                // 如果是认证过程中出现的异常会被封装成AuthenticationException然后调用AuthenticationEntryPoint对象的方法去进行异常处理。
                // 如果是授权过程中出现的异常会被封装成AccessDeniedException然后调用AccessDeniedHandler对象的方法去进行异常处理。
                .exceptionHandling()
                .authenticationEntryPoint(authenticationExceptionHandler);
        // 配置token拦截过滤器
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * 身份认证管理器，调用authenticate()方法完成认证
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173"); // 允许前端地址
        configuration.addAllowedMethod("*");  // 允许所有方法
        configuration.addAllowedHeader("*");  // 允许所有请求头
        configuration.setAllowCredentials(true);  // 允许携带 cookies
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // 配置所有路径
        return source;
    }
}
