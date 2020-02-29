package com.microtomato.hirun.framework.mybatis.aop;

import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.mybatis.processor.DataSourceProcessor;
import com.microtomato.hirun.framework.mybatis.threadlocal.DataSourceContextHolder;
import com.microtomato.hirun.framework.mybatis.toolkit.DynamicDataSourceClassResolver;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * 动态数据源切换拦截器
 *
 * @author Steven
 * @date 2020-02-28
 */
@Slf4j
public class DynamicDataSourceSwitchInterceptor implements MethodInterceptor {

    /**
     * 表明是 SpringEL 表达式
     */
    private static final String DYNAMIC_PREFIX = "#";
    private static final DynamicDataSourceClassResolver RESOLVER = new DynamicDataSourceClassResolver();

    @Setter
    private DataSourceProcessor dataSourceProcessor;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            String dsName = determineDatasource(invocation);

            log.debug("当前数据源：{}", dsName);
            DataSourceContextHolder.push(dsName);
            return invocation.proceed();
        } finally {
            DataSourceContextHolder.poll();
        }
    }

    private String determineDatasource(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();

        DataSource dataSource;
        if (method.isAnnotationPresent(DataSource.class)) {
            dataSource = method.getAnnotation(DataSource.class);
        } else {
            Class<?> targetClass = RESOLVER.targetClass(invocation);
            dataSource = AnnotationUtils.findAnnotation(targetClass, DataSource.class);
        }

        String key = dataSource.value();
        return (!key.isEmpty() && key.startsWith(DYNAMIC_PREFIX)) ? dataSourceProcessor.determineDatasource(invocation, key) : key;
    }

}