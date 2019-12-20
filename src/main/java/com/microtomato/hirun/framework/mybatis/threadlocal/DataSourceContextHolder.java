package com.microtomato.hirun.framework.mybatis.threadlocal;

/**
 * 线程上下文，保存当前数据源名称
 *
 * @author Steven
 * @date 2019-12-19
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> currDsNameHolder = new InheritableThreadLocal<>();

    /**
     * 设置数据源
     *
     * @param dsName
     */
    public static void setDataSource(String dsName) {
        currDsNameHolder.set(dsName);
    }

    /**
     * 取得当前 dsName
     *
     * @return
     */
    public static String getDataSource() {
        return currDsNameHolder.get();
    }

    /**
     * 清除上下文数据
     */
    public static void clear() {
        currDsNameHolder.remove();
    }
}