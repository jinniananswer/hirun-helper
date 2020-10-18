package com.microtomato.hirun.framework.threadlocal;

import com.microtomato.hirun.framework.security.UserContext;

/**
 * @author Steven
 * @date 2020-10-02
 */
public class UserContextHolder {

    /**
     * 请求级时间戳，确保请求级上下文的时间一致！
     */
    private final static ThreadLocal<UserContext> USER_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 添加请求级时间戳
     *
     * @param userContext
     */
    public static void setUserContext(UserContext userContext) {
        USER_CONTEXT_THREAD_LOCAL.set(userContext);
    }

    /**
     * 获取请求级时间戳
     *
     * @return
     */
    public static UserContext getUserContext() {
        return USER_CONTEXT_THREAD_LOCAL.get();
    }

    /**
     * 删除请求级时间戳
     */
    public static void remove() {
        USER_CONTEXT_THREAD_LOCAL.remove();
    }

}
