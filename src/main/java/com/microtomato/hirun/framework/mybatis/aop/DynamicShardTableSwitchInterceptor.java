package com.microtomato.hirun.framework.mybatis.aop;

import com.microtomato.hirun.framework.mybatis.annotation.Shard;
import com.microtomato.hirun.framework.mybatis.dynamic.policy.DynamicShardTablePolicy;
import com.microtomato.hirun.framework.mybatis.threadlocal.ShardTableContextHolder;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * 动态分表拦截器
 *
 * @author Steven
 * @date 2020-03-20
 */
@Slf4j
public class DynamicShardTableSwitchInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object proceed;
        String shard = determineShard(invocation);
        if (StringUtils.isNotBlank(shard)) {
            try {
                log.debug("当前表分片：{}", shard);
                ShardTableContextHolder.push(shard);
                proceed = invocation.proceed();
            } finally {
                ShardTableContextHolder.poll();
            }
        } else {
            proceed = invocation.proceed();
        }
        return proceed;
    }

    private String determineShard(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();

        Shard shard;
        if (method.isAnnotationPresent(Shard.class)) {
            shard = method.getAnnotation(Shard.class);
        } else {
            return "";
        }

        String key = shard.value();
        Class<?> policy = shard.policy();
        log.debug("policy: {}", policy);

        if (DynamicShardTablePolicy.class.isAssignableFrom(policy)) {
            DynamicShardTablePolicy dynamicShardTablePolicy = (DynamicShardTablePolicy) SpringContextUtils.getBean(policy);
            return dynamicShardTablePolicy.determineShardTable(invocation, key);
        } else {
            return "";
        }

    }
}
