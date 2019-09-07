package com.microtomato.hirun.framework.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置
 *
 * @author Steven
 * @date 2019-05-04
 */
@Configuration
@EnableCaching
public class CachingConfig {
	
	@Bean
	public CacheManager cacheManager() {
		// 基于 java.util.concurrent.ConcurrentHashMap 的简易缓存管理器。
		return new ConcurrentMapCacheManager();
	}

}
