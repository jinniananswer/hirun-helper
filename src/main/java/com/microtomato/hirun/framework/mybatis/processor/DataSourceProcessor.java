package com.microtomato.hirun.framework.mybatis.processor;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Steven
 * @date 2020-02-28
 */
public abstract class DataSourceProcessor {

    private DataSourceProcessor nextProcessor;

    public void setNextProcessor(DataSourceProcessor dataSourceProcessor) {
        this.nextProcessor = dataSourceProcessor;
    }

    /**
     * 决定数据源
     * <pre>
     *     调用底层 doDetermineDatasource，
     *     如果返回的是null则继续执行下一个，否则直接返回
     * </pre>
     *
     * @param invocation 方法执行信息
     * @param key DataSource 注解里的内容
     * @return 数据源名称
     */
    public String determineDatasource(MethodInvocation invocation, String key) {
        if (matches(key)) {
            String datasource = doDetermineDatasource(invocation, key);
            if (datasource == null && nextProcessor != null) {
                return nextProcessor.determineDatasource(invocation, key);
            }
            return datasource;
        }
        if (nextProcessor != null) {
            return nextProcessor.determineDatasource(invocation, key);
        }
        return null;
    }

    /**
     * 抽象匹配条件 匹配才会走当前执行器否则走下一级执行器
     *
     * @param key DataSource 注解里的内容
     * @return 是否匹配
     */
    public abstract boolean matches(String key);

    /**
     * 抽象最终决定数据源
     *
     * @param invocation 方法执行信息
     * @param key DS注解里的内容
     * @return 数据源名称
     */
    public abstract String doDetermineDatasource(MethodInvocation invocation, String key);
}
