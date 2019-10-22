package com.microtomato.hirun.framework.filter;

import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 注解 @Order 指定注解的执行顺序，值越小越先执行
 *
 * @author Steven
 * @date 2019-10-22
 */
@Slf4j
@Order(1)
@WebFilter(filterName = "localDateTimeFilter", urlPatterns = "/*", initParams = {
    @WebInitParam(name = "exclusions", value = "/static/,/druid/,/favicon.ico")
})
public class RequestTimeFilter implements Filter {

    public static final String PARAM_NAME_EXCLUSIONS = "exclusions";

    /**
     * 上下文
     */
    private String contextPath;

    /**
     * 被排除的
     */
    private Set<String> excludesPattern;

    @Override
    public void init(FilterConfig filterConfig) {
        contextPath = filterConfig.getServletContext().getContextPath();
        String exclusions = filterConfig.getInitParameter(PARAM_NAME_EXCLUSIONS);
        if (exclusions != null && exclusions.trim().length() != 0) {
            excludesPattern = new HashSet<String>(Arrays.asList(exclusions.split("\\s*,\\s*")));
        }

        log.info("contextPath: " + this.contextPath);
        log.info("excludesPattern: " + this.excludesPattern);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();
        if (isExclusion(requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        RequestTimeHolder.addRequestTime(now);
        log.debug("add localDateTime to localThread: {}, {}", now, requestURI);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    /**
     * 判断路径是否过滤掉
     */
    private boolean isExclusion(String requestURI) {
        if (excludesPattern == null || requestURI == null) {
            return false;
        }

        if (contextPath != null && requestURI.startsWith(contextPath)) {
            requestURI = requestURI.substring(contextPath.length());
            if (!requestURI.startsWith("/")) {
                requestURI = "/" + requestURI;
            }
        }

        for (String pattern : excludesPattern) {
            if (requestURI.startsWith(pattern)) {
                return true;
            }
        }

        return false;
    }
}
