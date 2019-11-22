package com.microtomato.hirun.framework.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @program: hirun-helper
 * @description: Spring 上下文工具类
 * @author: jinnian
 * @create: 2019-10-27 12:29
 **/
@Component
public final class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 获取 Spring 上下文对象
     *
     * @return
     */
    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * 获取 Bean 对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return (T) getContext().getBean(clazz);
    }

    /**
     * 获取 Bean 对象，调用其带参数的构造函数
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz, Object... args) {
        return (T) getContext().getBean(clazz, args);
    }
}
