package com.microtomato.hirun.framework.filter;

import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author Steven
 * @date 2019-10-22
 */
@Slf4j
@Order(1)
@WebFilter(filterName = "localDateTimeFilter", urlPatterns = "/*")
public class RequestTimeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        LocalDateTime now = LocalDateTime.now();
        RequestTimeHolder.addRequestTime(now);
        log.debug("add localDateTime to localThread: {}, {}", now, request.getServletPath());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
