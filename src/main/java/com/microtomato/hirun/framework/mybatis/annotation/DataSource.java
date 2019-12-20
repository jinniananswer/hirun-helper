package com.microtomato.hirun.framework.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 数据源注解，用于动态切换数据源，能被用于类或方法上。
 *
 * @author Steven
 * @date 2019-12-19
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String value();
    //DataSourceKeyEnum value();
}