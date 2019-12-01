package com.microtomato.hirun.framework.util;

import com.microtomato.hirun.framework.security.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Web上下文工具类
 *
 * @author Steven
 * @date 2019-04-29
 */
@Slf4j
public class WebContextUtils {

    /**
     * 获取 HttpSession
     *
     * @return
     */
    public static final HttpSession getHttpSession() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getSession();
    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    public static final UserContext getUserContext() {

        UserContext userContext = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserContext) {
            userContext = (UserContext) principal;
        } else {
            log.error("principal is not UserContext, " + principal + ", " + principal.getClass());
        }

        return userContext;
    }

}
