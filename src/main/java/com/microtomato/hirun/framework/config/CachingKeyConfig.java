package com.microtomato.hirun.framework.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;

/**
 * 自定义 Caching Key 的生成策略
 * 【暂不启用】
 *
 * @author Steven
 * @date 2019-12-12
 */
//@Configuration
public class CachingKeyConfig extends CachingConfigurerSupport {

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, objects) -> {
            StringBuilder sb = new StringBuilder(200);
            String clsName = target.getClass().getName();
            String[] split = StringUtils.split(clsName, '.');
            for (int i = 0, end = split.length - 1; i < end; i++) {
                sb.append(split[i].charAt(0)).append('.');
            }
            sb.append(split[split.length - 1]);
            sb.append("::");
//            sb.append(method.getName());
//            sb.append(":");
            for (Object obj : objects) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

}
