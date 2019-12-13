package com.microtomato.hirun.framework.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author Steven
 * @date 2019-12-12
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.session", name = "store-type", havingValue = "redis", matchIfMissing = false)
public class HttpSessionConfig {

    @EnableRedisHttpSession
    class RedisHttpSessionConfig {

    }

}
