package com.microtomato.hirun.framework.annotation;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Repository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 组合 @Mapper 和 @Repository
 * 解决 IDEA 里 通过 @Autowired 动态注入 Mapper 类时告警的问题。
 *
 * IDEA 是非常智能的，它可以理解 Spring 自带的注解以及上下文，然而 @Mapper 这个注解是 Mybatis 自带的，IDEA 理解不了。
 * 而 @Autowired 注解，要求依赖对象（也就是 xxxMapper ）必须存在。而 IDEA 认为这个对象的实例/代理是个 null，所以就友好地给个提示。

 *
 * @author Steven
 * @date 2019-11-12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Mapper
@Repository
public @interface Storage {

    @AliasFor(annotation = Repository.class, attribute = "value")
    String value() default "";
}
