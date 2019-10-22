package com.microtomato.hirun.framework.threadlocal;

import java.time.LocalDateTime;

/**
 * 请求级上下文
 *
 * @author Steven
 * @date 2019-10-22
 */
public class RequestTimeHolder {

    /**
     * 请求级时间戳，确保请求级上下文的时间一致！
     */
    private final static ThreadLocal<LocalDateTime> requestTimeHolder = new ThreadLocal<>();

    /**
     * 添加请求级时间戳
     *
     * @param localDateTime
     */
    public static void addRequestTime(LocalDateTime localDateTime) {
        requestTimeHolder.set(localDateTime);
    }

    /**
     * 获取请求级时间戳
     *
     * @return
     */
    public static LocalDateTime getRequestTime() {
        return requestTimeHolder.get();
    }

    /**
     * 删除请求级时间戳
     */
    public static void remove() {
        requestTimeHolder.remove();
    }

}
