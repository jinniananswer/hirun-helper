package com.microtomato.hirun.framework.mybatis.aspect;

import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.mybatis.threadlocal.DataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据源切面
 *
 * @author Steven
 * @date 2019-12-19
 */
@Component
@Slf4j
@Aspect
@Order(-1)
public class DataSourceAspect {

    /**
     * 业务 Mapper 以及，mybatisplus 核心 Mapper 的切点
     */
    @Pointcut("execution(* com.microtomato.hirun.modules.*.mapper.*Mapper.*(..))||execution(* com.baomidou.mybatisplus.core.mapper.*Mapper.*(..)))")
    public void pointCut() {

    }

    /**
     * 方法上的切点优先于类上的切点
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("pointCut()")
    public Object doBefore(ProceedingJoinPoint pjp) throws Throwable {
        return process(pjp);
    }

    @Pointcut("execution(* com.microtomato.hirun.modules.*.service.*Impl.*Batch*(..))||execution(* com.baomidou.mybatisplus.extension.service.IService.*Batch*(..)))")
    public void pointCutBatch() {

    }

    /**
     * 对 mybatis-plus 批量操作切面
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("pointCutBatch()")
    public Object doBeforeBatch(ProceedingJoinPoint pjp) throws Throwable {
        return process(pjp);
    }

    private Object process(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        DataSource dataSource = AnnotationUtils.findAnnotation(method, DataSource.class);
        if (null == dataSource) {
            Class<?> cls = pjp.getTarget().getClass();
            dataSource = AnnotationUtils.findAnnotation(cls, DataSource.class);
            log.debug("cls: {}, dataSource: {}", cls, dataSource);
        } else {
            log.debug("method: {}, dataSource: {}", method, dataSource);
        }

        String dsName = dataSource.value();
        log.debug("当前数据源：{}", dsName);
        DataSourceContextHolder.setDataSource(dsName);
        Object o = pjp.proceed();
        DataSourceContextHolder.clear();
        return o;
    }

}