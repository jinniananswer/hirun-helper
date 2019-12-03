package com.microtomato.hirun.framework.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Steven
 * @date 2019-12-03
 */
@Slf4j
public class TokenContext {

    /**
     * 新鲜的 Token
     */
    private static final Map<String, String> FRESH_TOKEN_MAP = new ConcurrentHashMap<>(1000);

    /**
     * 历史的 Token
     */
    private static final Map<String, String> HISTO_TOKEN_MAP = new ConcurrentHashMap<>(1000);

    public static final char TOKEN_SPLIT_CHAR = '^';
    public static final String DATE_FORMAT_PATTERN = "yyyyMMddHHmmss";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);

    static {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
                log.info("oneHourAgo: " + oneHourAgo);
                clean(oneHourAgo, FRESH_TOKEN_MAP);
                clean(oneHourAgo, HISTO_TOKEN_MAP);

            }
        }, 1000 * 10);
    }

    /**
     * 定时清理过期的 token, 防止内存溢出
     *
     * @param oneHourAgo
     * @param map
     */
    private static final void clean(LocalDateTime oneHourAgo, Map<String, String> map) {
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String token = iterator.next();
            String[] split = StringUtils.split(token, TOKEN_SPLIT_CHAR);
            if (2 == split.length) {
                LocalDateTime dateTime = LocalDateTime.parse(split[1], DATE_TIME_FORMATTER);
                if (dateTime.isBefore(oneHourAgo)) {
                    log.info("清理超时的 token: " + token);
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 对 token 进行验证
     *
     * @param token
     * @return 验证成功
     */
    public static final String authentication(String token) {

        String username = FRESH_TOKEN_MAP.get(token);
        if (null != username) {
            String now = DateFormatUtils.format(System.currentTimeMillis(), DATE_FORMAT_PATTERN);
            HISTO_TOKEN_MAP.put(token, username + TOKEN_SPLIT_CHAR + now);
            FRESH_TOKEN_MAP.remove(token);
            log.info("{} token is valid!", token);
            return username;
        } else {
            String value = HISTO_TOKEN_MAP.get(token);
            if (null != value) {
                return value;
            }
            log.info("{} token is invalid!", token);
            return null;
        }
    }

    /**
     * token 关联登录帐号
     *
     * @param username
     * @return
     */
    public static final String associateWithToken(String username) {
        String now = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss");
        String token = UUID.randomUUID().toString() + TOKEN_SPLIT_CHAR + now;
        FRESH_TOKEN_MAP.put(token, username);
        return token;
    }

}
