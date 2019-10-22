package com.microtomato.hirun.framework.config;

import com.microtomato.hirun.framework.interceptor.RequestTimeInterceptor;
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

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 请求级时间一致性拦截器
        registry.addInterceptor(new RequestTimeInterceptor())
            .addPathPatterns("/**");
            // .excludePathPatterns("/login", "/css/**", "/img/**", "/js/**", "/layui/**", "/webfonts/**");
    }

}
