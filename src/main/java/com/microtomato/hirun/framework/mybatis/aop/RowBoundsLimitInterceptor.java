package com.microtomato.hirun.framework.mybatis.aop;

import lombok.extern.slf4j.Slf4j;
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
 * 该限制在 DefaultResultSetHandler.shouldProcessMoreRows(ResultContext<?> context, RowBounds rowBounds) 中生效。
 *
 * @author Steven
 * @date 2020-05-14
 */
@Slf4j
@Component
@Intercepts(
    @Signature(
        type = Executor.class,
        method = "query",
        args = {
            MappedStatement.class,
            Object.class,
            RowBounds.class,
            ResultHandler.class
        }
    )
)
public class RowBoundsLimitInterceptor implements Interceptor {

    @Value("${row.bounds.limit:20000}")
    private int limit;

    @PostConstruct
    public void init() {
        log.info("查询结果集条数限制: {} 条", limit);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        args[2] = new RowBounds(0, limit);
        return invocation.proceed();
    }

}
