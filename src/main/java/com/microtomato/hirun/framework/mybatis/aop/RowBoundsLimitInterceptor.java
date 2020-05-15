package com.microtomato.hirun.framework.mybatis.aop;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 查询结果集数量限制，防止 SQL 漏条件导致服务宕机。
 *
 * @author Steven
 * @date 2020-05-14
 */
@Component
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class RowBoundsLimitInterceptor implements Interceptor {

    @Value("${row.bounds.limit:100000}")
    private int limit;

    private RowBounds rowBounds;

    @PostConstruct
    public void init() {
        rowBounds = new RowBounds(0, limit);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        args[2] = rowBounds;
        return invocation.proceed();
    }

}
