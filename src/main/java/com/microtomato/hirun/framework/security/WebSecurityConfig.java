package com.microtomato.hirun.framework.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * Web安全配置
 *
 * @author Steven
 * @date 2019-09-08
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired(required = false)
    @Qualifier("sysDataSource")
    private DataSource dataSource;

    @Autowired
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailHandler customAuthenticationFailHandler;

    /**
     * 设置自定义的 userDetailsService, 密码加密器
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    /**
     * Http 资源认证
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.authorizeRequests()
            .antMatchers("/static/**").permitAll()
            .antMatchers("/api/**").permitAll()
            .antMatchers("/login/**").permitAll()
            .antMatchers("/druid/**").permitAll()
            .antMatchers("/api/demo/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login")
            .successHandler(customAuthenticationSuccessHandler)
            .failureHandler(customAuthenticationFailHandler)
            .and()
//            .rememberMe()
//            // 两周之内登陆过不用重新登陆
//            .tokenValiditySeconds(60 * 60 * 24 * 14)
//            .tokenRepository(persistentTokenRepository())
//            .and()
            .httpBasic();

        // 默认情况下 SpringSecurity 通过设置 X-Frame-Options: DENY 防止网页被 Frame，我们这需要警用该功能。
        http.headers().frameOptions().disable();
    }

    @Bean
    public LoginAuthenticationProvider authenticationProvider() {
        LoginAuthenticationProvider loginAuthenticationProvider = new LoginAuthenticationProvider();
        // 打开用户名不存在提示信息
        loginAuthenticationProvider.setHideUserNotFoundExceptions(false);
        loginAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        loginAuthenticationProvider.setPasswordEncoder(customPasswordEncoder);
        return loginAuthenticationProvider;
    }

    /**
     * 持久化 remember-me token
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        tokenRepository.setCreateTableOnStartup(false);
        return tokenRepository;
    }

}