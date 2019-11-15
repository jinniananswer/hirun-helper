package com.microtomato.hirun.modules.user.exception;

import com.microtomato.hirun.framework.exception.BaseException;
import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.exception.Error;
import com.microtomato.hirun.framework.util.ArrayUtils;

/**
 * @program: hirun-helper
 * @description: 用户相关的异常信息类
 * @author: jinnian
 * @create: 2019-11-15 14:43
 **/
public class UserException extends BaseException {

    public enum UserExceptionEnum {

        @Error(code = ErrorKind.ALREADY_EXIST, message = "该手机号码【%s】的用户已存在")
        IS_EXISTS
    }

    public UserException(String message, int code) {
        super(message, code);
    }

    public UserException(UserExceptionEnum exception, String... replaceTexts) {
        try {
            String name = exception.name();
            Error error = exception.getClass().getField(name).getAnnotation(Error.class);
            this.setCode(error.code().getCode());

            String message = error.message();
            if (ArrayUtils.isEmpty(replaceTexts)) {
                this.setMessage(error.message());
                return;
            }

            for (String replaceText : replaceTexts) {
                message = message.replaceFirst("%s", replaceText);
            }

            this.setMessage(message);
        } catch (NoSuchFieldException e) {

        }
    }
}
