package com.microtomato.hirun.framework.mybatis.annotation;

import com.microtomato.hirun.framework.mybatis.dynamic.policy.impl.ShardTableStaticPolicy;

import java.lang.annotation.*;

/**
 * 表分片
 *
 * @author Steven
 * @date 2020-03-20
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Shard {

    /**
     * @return the table shard you want to switch
     */
    String value() default "";

    /**
     * 分片策略
     *
     * @return
     */
    Class<?> policy() default ShardTableStaticPolicy.class;

}
