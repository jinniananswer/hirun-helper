package com.microtomato.hirun.modules.bss.salary.exception;

import com.microtomato.hirun.framework.exception.BaseException;
import com.microtomato.hirun.framework.exception.Error;
import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.util.ArrayUtils;

/**
 * @program: hirun-helper
 * @description: 员工工资相关的异常
 * @author: jinnian
 * @create: 2020-05-21 14:58
 **/
public class SalaryException extends BaseException {

    public enum SalaryExceptionEnum {
        @Error(code = ErrorKind.NOT_FOUND, message = "参数【%s】未传入")
        ARGUMENT_NOT_FOUND,

        @Error(code = ErrorKind.NOT_FOUND, message = "未找到该笔订单【%s】的设计费用标准")
        DESIGN_FEE_STANDARD_NOT_FOUND,

        @Error(code = ErrorKind.NOT_FOUND, message = "未找到该笔订单对应阶段【%s】的费用信息")
        ORDER_FEE_NOT_FOUND,

        @Error(code = ErrorKind.NOT_FOUND, message = "【%s】员工的【%s】月工资已发放，不能再更新，请确认工资月份是否正确")
        CANNOT_UPDATE_MONTHLY,

    }

    public SalaryException(String message, int code) {
        super(message, code);
    }

    public SalaryException(SalaryException.SalaryExceptionEnum exception, String... replaceTexts) {
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
