package com.microtomato.hirun.framework.mybatis.aop;

import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import lombok.NonNull;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * 分表增强器，是切入点 Pointcut 和 Advice 的适配器
 *
 * @author Steven
 * @date 2020-03-20
 */
public class DynamicShardTableAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private Advice advice;

    private Pointcut pointcut;

    public DynamicShardTableAnnotationAdvisor(@NonNull DynamicShardTableSwitchInterceptor dynamicShardTableSwitchInterceptor) {
        this.advice = dynamicShardTableSwitchInterceptor;
        this.pointcut = buildPointcut();
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    private Pointcut buildPointcut() {
        Pointcut cpc = new AnnotationMatchingPointcut(DataSource.class, true);
        Pointcut mpc = AnnotationMatchingPointcut.forMethodAnnotation(DataSource.class);
        return new ComposablePointcut(cpc).union(mpc);
    }

}
