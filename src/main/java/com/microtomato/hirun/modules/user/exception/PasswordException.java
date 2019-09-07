package com.microtomato.hirun.modules.user.exception;

import com.microtomato.hirun.framework.exception.BaseException;

/**
 * @program: hirun-helper
 * @description: 密码相关的异常
 * @author: jinnian
 * @create: 2019-09-07 15:06
 **/
public class PasswordException extends BaseException {

    public PasswordException(String msg, int code) {
        super(msg, code);
    }
}
