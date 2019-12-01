package com.microtomato.hirun.framework.listener;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话上下文
 *
 * @author Steven
 * @date 2019-12-01
 */
public class SessionContext {

    private static final Map<String, HttpSession> map = new ConcurrentHashMap();

    public static void addSession(HttpSession session) {
        if (null != session) {
            map.put(session.getId(), session);
        }
    }

    public static void deleteSession(HttpSession session) {
        if (null != session) {
            map.remove(session.getId());
        }
    }

    public static HttpSession getSession(String sessionId) {
        if (null == sessionId) {
            return null;
        }
        return map.get(sessionId);
    }
}
