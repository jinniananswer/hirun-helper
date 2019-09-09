package com.microtomato.hirun.framework.config;

import com.microtomato.hirun.framework.security.CustomPasswordEncoder;
import com.microtomato.hirun.framework.security.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Web安全配置
 *
 * @author Steven
 * @date 2019-09-08
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private CustomPasswordEncoder customPasswordEncoder;

	/**
	 * 设置自定义的 userDetailsService, 密码加密器
	 *
	 * @param auth
	 * @throws Exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(customPasswordEncoder);
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
			.antMatchers("/css/**", "/img/**", "/js/**", "/layui/**", "/webfonts/**").permitAll()
			.antMatchers("/login/**", "/api/**").permitAll()
			.anyRequest().authenticated()
			.and()
				.formLogin().loginPage("/login")
			.and()
				.httpBasic();

		// 默认情况下 SpringSecurity 通过设置 X-Frame-Options: DENY 防止网页被 Frame，我们这需要警用该功能。
		http.headers().frameOptions().disable();
	}

}