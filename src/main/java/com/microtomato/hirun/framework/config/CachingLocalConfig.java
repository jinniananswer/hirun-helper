package com.microtomato.hirun.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 本地缓存配置
 *
 * @author Steven
 * @date 2019-05-04
 */
@Slf4j
@Configuration
@EnableCaching
@ConditionalOnProperty(prefix = "caching", name = "mode", havingValue = "local")
public class CachingLocalConfig {

    /**
     * 基于 java.util.concurrent.ConcurrentHashMap 的简易缓存管理器，一般用于开发。
     *
     * @return
     */
    @Bean
    public CacheManager cacheManager() {
        log.info("CacheManager：ConcurrentMapCacheManager");
        return new ConcurrentMapCacheManager();
    }

}
