package com.microtomato.hirun.framework.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Steven
 * @date 2019-12-13
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "production", name = "mode", havingValue = "false", matchIfMissing = true)
public class AssetSessionDevelopmentImpl extends AssetSession {

    private static final Map<String, Set<String>> MENU_URL_MAP = new HashMap<>();

    @Override
    public void setMenuUrls(Set<String> menuUrls) {
        String key = assetKey();
        MENU_URL_MAP.put(key, menuUrls);
    }

    @Override
    public boolean hasMenuUrl(String url) {
        String key = assetKey();
        Set<String> menuUrls = MENU_URL_MAP.get(key);
        return menuUrls.contains(url);
    }

}
