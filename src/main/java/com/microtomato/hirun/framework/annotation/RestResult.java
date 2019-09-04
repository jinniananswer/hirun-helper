package com.microtomato.hirun.framework.annotation;

import java.lang.annotation.*;

/**
 * 自动包装 REST 接口返回结果
 *
 * @author Steven
 * @date 2019-04-28
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestResult {

}
