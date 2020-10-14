package com.microtomato.hirun.framework.config;

import com.microtomato.hirun.framework.interceptor.JwtInterceptor;
import com.microtomato.hirun.framework.interceptor.WebPerformanceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WEB 通用配置
 *
 * @author Steven
 * @date 2019-04-29
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 请求级时间一致性拦截器
        registry.addInterceptor(new WebPerformanceInterceptor()).addPathPatterns("/**").excludePathPatterns("/static/**", "/druid/**", "/favicon.ico");
        registry.addInterceptor(jwtInterceptor)
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/system/auth/login");
    }

}
