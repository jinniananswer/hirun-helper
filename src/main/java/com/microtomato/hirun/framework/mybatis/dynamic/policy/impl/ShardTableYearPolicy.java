package com.microtomato.hirun.framework.mybatis.dynamic.policy.impl;

import com.microtomato.hirun.framework.mybatis.dynamic.policy.DynamicShardTablePolicy;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 按年分片，如: 2020
 *
 * @author Steven
 * @date 2020-03-20
 */
@Component
public class ShardTableYearPolicy extends DynamicShardTablePolicy {

    /**
     * 线程安全的
     */
    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy");

    /**
     * @param key Shard 注解里的内容
     * @return 是否匹配
     */
    @Override
    public boolean matches(String key) {
        return true;
    }

    /**
     * 抽象最终决定哪个分表
     *
     * @param invocation 方法执行信息
     * @param key        Shard 注解里的内容
     * @return 数据源名称
     */
    @Override
    public String doDetermineShardTable(MethodInvocation invocation, String key) {
        return LocalDate.now().format(fmt);
    }
}
