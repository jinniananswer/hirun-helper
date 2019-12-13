package com.microtomato.hirun.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 缓存模块配置
 *
 * @author Steven
 * @date 2019-12-12
 */
@Slf4j
@Configuration
@EnableCaching
public class CachingConfig {

    /**
     * 用于开发模式
     */
    @ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "simple")
    static class SimpleCachingConfig {
        @Bean
        public CacheManager cacheManager() {
            log.info("缓存管理器：ConcurrentMapCacheManager");
            return new ConcurrentMapCacheManager();
        }
    }

    /**
     * 用于生产模式
     */
    @ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis")
    static class RedisCachingConfig {
        @Bean
        public CacheManager cacheManager(LettuceConnectionFactory factory) {
            log.info("缓存管理器：RedisCacheManager");

            // 以锁写入的方式创建 RedisCacheWriter 对象
            RedisCacheWriter writer = RedisCacheWriter.lockingRedisCacheWriter(factory);
            RedisSerializationContext.SerializationPair pair = RedisSerializationContext.SerializationPair.fromSerializer(new JdkSerializationRedisSerializer(this.getClass().getClassLoader()));
            RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
            config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
            RedisCacheManager cacheManager = new RedisCacheManager(writer, config);
            return cacheManager;
        }
    }

    @Configuration
    @ConditionalOnProperty(prefix = "spring.cache.customize-keygenerator", name = "enabled", havingValue = "true", matchIfMissing = false)
    class MyKeyGenerator extends CachingConfigurerSupport {

        @Override
        @Bean
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

}
