package com.microtomato.hirun.framework.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @program: hirun-helper
 * @description: spring上下文工具类
 * @author: jinnian
 * @create: 2019-10-27 12:29
 **/
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 获取spring上下文对象
     * @return
     */
    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * 获取bean对象
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        String className = clazz.getSimpleName();
        String firstCharacter = className.charAt(0) + "";
        String beanName = firstCharacter.toLowerCase() + className.substring(1);
        return (T)getContext().getBean(beanName);
    }
}
