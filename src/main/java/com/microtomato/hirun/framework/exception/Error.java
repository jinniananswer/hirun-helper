package com.microtomato.hirun.framework.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: hirun-helper
 * @description: 异常枚举的编码与描述信息注解
 * @author: jinnian
 * @create: 2019-10-26 02:05
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Error {

    ErrorKind code();

    String message();

}
