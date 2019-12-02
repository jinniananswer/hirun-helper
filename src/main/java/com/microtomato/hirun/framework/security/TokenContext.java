package com.microtomato.hirun.framework.security;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Steven
 * @date 2019-12-03
 */
@Slf4j
public class TokenContext {
    private static final Map<String, String> tokenMap = new ConcurrentHashMap<>();

    public static final String authentication(String token) {
        String username = tokenMap.get(token);
        if (null != username) {
            log.info("{} token is valid!", token);
            return username;
        } else {
            log.info("{} token is invalid!", token);
            return null;
        }
    }

    public static final String associateWithToken(String username) {
        String token = UUID.randomUUID().toString();
        tokenMap.put(token, username);
        return token;
    }
}
