package com.microtomato.hirun.modules.finance.exception;

import com.microtomato.hirun.framework.exception.BaseException;
import com.microtomato.hirun.framework.exception.Error;
import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.util.ArrayUtils;

/**
 * @program: hirun-helper
 * @description:
 * @author: jinnian
 * @create: 2020-11-24 22:45
 **/
public class FinanceException extends BaseException {

    public enum FinanceExceptionEnum {
        @Error(code = ErrorKind.BREACH_BUSINESS_RULE, message = "付款金额必须等于付款明细金额之和")
        PAY_MUST_EQUAL_PAYITEM
    }

    public FinanceException(String message, int code) {
        super(message, code);
    }

    public FinanceException(FinanceException.FinanceExceptionEnum exception, String... replaceTexts) {
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
