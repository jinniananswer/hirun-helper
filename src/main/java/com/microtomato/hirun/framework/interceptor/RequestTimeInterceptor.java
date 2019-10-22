package com.microtomato.hirun.framework.interceptor;

import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器，请求处理完后（从 Controller 返回后），清楚 ThreadLocal 里的 localDateTime，避免内存泄漏。
 *
 * @author Steven
 * @date 2019-10-22
 */
@Slf4j
public class RequestTimeInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        RequestTimeHolder.remove();
        log.debug("remove localDateTime from localThread, " + request.getRequestURI());
    }

}
