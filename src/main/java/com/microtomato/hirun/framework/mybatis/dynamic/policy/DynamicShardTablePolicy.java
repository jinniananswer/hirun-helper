package com.microtomato.hirun.framework.mybatis.dynamic.policy;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Steven
 * @date 2020-03-20
 */
public abstract class DynamicShardTablePolicy implements IShardTablePolicy {

    /**
     * 决定分表
     *
     * <pre>
     *     调用底层 doDetermineShardTable，
     * </pre>
     *
     * @param invocation 方法执行信息
     * @param key Shard 注解里的内容
     * @return 数据源名称
     */
    public String determineShardTable(MethodInvocation invocation, String key) {
        if (matches(key)) {
            String datasource = doDetermineShardTable(invocation, key);
            return datasource;
        }

        return null;
    }

    /**
     * 抽象匹配条件
     *
     * @param key Shard 注解里的内容
     * @return 是否匹配
     */
    public abstract boolean matches(String key);

    /**
     * 抽象最终决定哪个分表
     *
     * @param invocation 方法执行信息
     * @param key Shard 注解里的内容
     * @return 数据源名称
     */
    public abstract String doDetermineShardTable(MethodInvocation invocation, String key);

}
