package com.microtomato.hirun.framework.interceptor;

import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Web请求性能拦截器
 *
 * 请求处理完后（从 Controller 返回后），清除 ThreadLocal 里的 localDateTime，避免内存泄漏。
 *
 * @author Steven
 * @date 2019-10-22
 */
@Slf4j
public class WebPerformanceInterceptor extends HandlerInterceptorAdapter {

    private static final ZoneId SHANGHAI_ZONE_ID = ZoneId.of("Asia/Shanghai");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LocalDateTime now = LocalDateTime.now();
        RequestTimeHolder.addRequestTime(now);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LocalDateTime startTime = RequestTimeHolder.getRequestTime();
        // 计算请求耗时
        long start = startTime.atZone(SHANGHAI_ZONE_ID).toInstant().toEpochMilli();
        long cost = System.currentTimeMillis() - start;
        log.info("cost: {} ms, uri: {}", cost, request.getRequestURI());
        RequestTimeHolder.remove();
    }

}
