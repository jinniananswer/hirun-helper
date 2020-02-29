package com.microtomato.hirun.framework.mybatis.threadlocal;

import org.springframework.util.StringUtils;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 线程上下文，保存当前数据源名称
 *
 * @author Steven
 * @date 2019-12-19
 */
public class DataSourceContextHolder {

    /**
     * 为什么要用栈来存储？
     * <pre>
     * 为了支持嵌套切换，如 A、B、C 三个 service 都是不同的数据源
     * 其中 A 的某个业务要调 B 的方法，B 的方法需要调用 C 的方法。一级一级调用切换，形成了链。
     * 传统的只设置当前线程的方式不能满足此业务需求，必须模拟栈，后进先出。
     * </pre>
     */
    @SuppressWarnings("unchecked")
    private static final ThreadLocal<Deque<String>> LOOKUP_KEY_HOLDER = new ThreadLocal() {

        @Override
        protected Object initialValue() {
            return new ArrayDeque();
        }

    };

    private DataSourceContextHolder() {
    }

    /**
     * 获得当前线程数据源
     *
     * @return 数据源名称
     */
    public static String peek() {
        return LOOKUP_KEY_HOLDER.get().peek();
    }

    /**
     * 设置当前线程数据源
     * <p>
     * 如非必要不要手动调用，调用后确保最终清除
     * </p>
     *
     * @param dataSource 数据源名称
     */
    public static void push(String dataSource) {
        LOOKUP_KEY_HOLDER.get().push(StringUtils.isEmpty(dataSource) ? "" : dataSource);
    }

    /**
     * 清空当前线程数据源
     * <p>
     * 如果当前线程是连续切换数据源 只会移除掉当前线程的数据源名称
     * </p>
     */
    public static void poll() {
        Deque<String> deque = LOOKUP_KEY_HOLDER.get();
        deque.poll();
        if (deque.isEmpty()) {
            LOOKUP_KEY_HOLDER.remove();
        }
    }

    /**
     * 强制清空本地线程
     * <p>
     * 防止内存泄漏，如手动调用了push可调用此方法确保清除
     * </p>
     */
    public static void clear() {
        LOOKUP_KEY_HOLDER.remove();
    }
}