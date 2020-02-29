package com.microtomato.hirun.framework.mybatis.processor.impl;

import com.microtomato.hirun.framework.mybatis.processor.DataSourceProcessor;
import com.microtomato.hirun.framework.util.PackageUtils;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Steven
 * @date 2020-02-29
 */
@Slf4j
public class DataSourceHeaderProcessor extends DataSourceProcessor {

    /**
     * header prefix
     */
    private static final String HEADER_PREFIX = "#header";

    public DataSourceHeaderProcessor() {
        log.info("构造动态数据源处理器: {}", PackageUtils.compactPackage(this.getClass()));
    }

    @Override
    public boolean matches(String key) {
        return key.startsWith(HEADER_PREFIX);
    }

    @Override
    public String doDetermineDatasource(MethodInvocation invocation, String key) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getHeader(key.substring(8));
    }

}
