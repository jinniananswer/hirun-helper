package com.microtomato.hirun.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author Steven
 * @date 2020-10-03
 */
public class TimeTest {
    private static Long loginTime = System.currentTimeMillis();

    public static void main(String[] args) {
        System.out.println(getLoginTime());
        setLoginTime(LocalDateTime.now().minusDays(1));
        System.out.println(getLoginTime());
    }

    public static LocalDateTime getLoginTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(loginTime), ZoneId.systemDefault());
    }

    public static void setLoginTime(LocalDateTime timestamp) {
        loginTime = timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
