package com.microtomato.hirun.framework.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Steven
 * @date 2019-12-13
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "production", name = "mode", havingValue = "true", matchIfMissing = false)
public class AssetSessionProductionImpl extends AssetSession {

    @Autowired
    protected RedisTemplate redisTemplate;

    @Override
    public void setMenuUrls(Set<String> menuUrls) {
        String key = assetKey();
        SetOperations setOperations = redisTemplate.opsForSet();
        menuUrls.forEach(s -> setOperations.add(key, s));
    }

    @Override
    public boolean hasMenuUrl(String url) {
        String key = assetKey();
        return redisTemplate.opsForSet().isMember(key, url);

    }
}
