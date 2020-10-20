package com.microtomato.hirun.framework.interceptor;

import com.microtomato.hirun.framework.jwt.JwtConstants;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.threadlocal.UserContextHolder;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.system.service.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Steven
 * @date 2020-10-02
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private IAuthService authService;

    /**
     * Intercept the execution of a handler. Called after HandlerMapping determined
     * an appropriate handler object, but before HandlerAdapter invokes the handler.
     * <p>DispatcherServlet processes a handler in an execution chain, consisting
     * of any number of interceptors, with the handler itself at the end.
     * With this method, each interceptor can decide to abort the execution chain,
     * typically sending an HTTP error or writing a custom response.
     * <p><strong>Note:</strong> special considerations apply for asynchronous
     * request processing. For more details see
     * {@link AsyncHandlerInterceptor}.
     * <p>The default implementation returns {@code true}.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return {@code true} if the execution chain should proceed with the
     * next interceptor or the handler itself. Else, DispatcherServlet assumes
     * that this interceptor has already dealt with the response itself.
     * @throws Exception in case of errors
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String jsonWebToken = request.getHeader(JwtConstants.HEAD_AUTHORIZATION);
        if (null != jsonWebToken) {
            jsonWebToken = jsonWebToken.replace(JwtConstants.HEAD_AUTHORIZATION_BEARER, "");
            authService.setUserContext(jsonWebToken);
            return true;
        }

        UserContext userContext = WebContextUtils.getUserContext();
        if (null != userContext) {
            log.debug("session 认证通过!");
            return true;
        }

        String requestURI = request.getRequestURI();
        log.info("requestURI: {}", requestURI);

        if (null != requestURI && requestURI.indexOf("api/system/session/authentication") >= 0) {
            log.info("老系统协同，不做登录校验。");
            return true;
        }

        // 重定向到登录页面
        response.sendRedirect("/hirun");
        return false;

    }

    /**
     * Callback after completion of request processing, that is, after rendering
     * the view. Will be called on any outcome of handler execution, thus allows
     * for proper resource cleanup.
     * <p>Note: Will only be called if this interceptor's {@code preHandle}
     * method has successfully completed and returned {@code true}!
     * <p>As with the {@code postHandle} method, the method will be invoked on each
     * interceptor in the chain in reverse order, so the first interceptor will be
     * the last to be invoked.
     * <p><strong>Note:</strong> special considerations apply for asynchronous
     * request processing. For more details see
     * {@link AsyncHandlerInterceptor}.
     * <p>The default implementation is empty.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  handler (or {@link HandlerMethod}) that started asynchronous
     *                 execution, for type and/or instance examination
     * @param ex       any exception thrown on handler execution, if any; this does not
     *                 include exceptions that have been handled through an exception resolver
     * @throws Exception in case of errors
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextHolder.remove();
    }

}
