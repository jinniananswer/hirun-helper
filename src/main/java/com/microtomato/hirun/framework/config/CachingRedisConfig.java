package com.microtomato.hirun.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * Redis 缓存配置
 *
 * @author Steven
 * @date 2019-12-12
 */
@Slf4j
@Configuration
@EnableCaching
@ConditionalOnProperty(prefix = "caching", name = "mode", havingValue = "redis")
public class CachingRedisConfig {

    private static final String REDIS_MODE_STANDALONE = "standalone";
    private static final String REDIS_MODE_SENTINEL = "sentinel";
    private static final String REDIS_MODE_CLUSTER = "cluster";

    /**
     * 工作模式: standalone、sentinel、cluster，默认：standalone
     */
    @Value("${spring.redis.mode:standalone}")
    private String mode;

    /**
     * Redis 服务器地，默认：127.0.0.1
     */
    @Value("${spring.redis.host:127.0.0.1}")
    private String redisHost;

    /**
     * Redis 服务器端口，默认：6379
     */
    @Value("${spring.redis.port:6379}")
    private int redisPort;

    /**
     * 连接超时时间（毫秒）默认是2000ms
     */
    @Value("${spring.redis.timeout:2000}")
    private int redisTimeout;

    /**
     * Redis 服务器连接密码，默认为空
     */
    @Value("${spring.redis.password:}")
    private String redisPassword;

    /**
     * Redis 数据库索引，默认：0
     */
    @Value("${spring.redis.database:0}")
    private int redisDatabase;

    /**
     * 连接池最大连接数（使用负值表示没有限制）
     */
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    /**
     * 连接池中的最大空闲连接
     */
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    /**
     * 连接池最大阻塞等待时间（使用负值表示没有限制）
     */
    @Value("${spring.redis.jedis.pool.max-wait}")
    private int maxWait;

    /**
     * 连接池中的最小空闲连接
     */
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxWaitMillis(maxWait);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setTestOnBorrow(false);
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestWhileIdle(true);
        JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
            .usePooling()
            .poolConfig(poolConfig)
            .and()
            .readTimeout(Duration.ofMillis(redisTimeout))
            .build();


        if (StringUtils.equals(REDIS_MODE_STANDALONE, mode)) {
            // 单点模式
            RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
            redisConfig.setHostName(redisHost);
            redisConfig.setPort(redisPort);
            redisConfig.setPassword(RedisPassword.of(redisPassword));
            redisConfig.setDatabase(redisDatabase);
            return new JedisConnectionFactory(redisConfig, clientConfig);
        } else if (StringUtils.equals(REDIS_MODE_SENTINEL, mode)) {
            // 哨兵模式
            RedisSentinelConfiguration redisConfig = new RedisSentinelConfiguration();
            throw new UnsupportedOperationException("暂不支持该模式: Sentinel");
            //return new JedisConnectionFactory(redisConfig, clientConfig);
        } else if (StringUtils.equals(REDIS_MODE_CLUSTER, mode)) {
            // 集群模式
            RedisClusterConfiguration redisConfig = new RedisClusterConfiguration();
            throw new UnsupportedOperationException("暂不支持该模式: Cluster");
            //return new JedisConnectionFactory(redisConfig, clientConfig);
        } else {
            throw new IllegalArgumentException("无法识别的模式: " + mode);
        }

    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        log.info("CacheManager：RedisCacheManager");
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
            .defaultCacheConfig()
            .entryTtl(Duration.ofHours(1));
        return RedisCacheManager
            .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
            .cacheDefaults(redisCacheConfiguration).build();
    }
}
