package com.microtomato.hirun.modules.organization.exception;

import com.microtomato.hirun.framework.exception.BaseException;
import com.microtomato.hirun.framework.exception.Error;
import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.util.ArrayUtils;

/**
 * @program: hirun-helper
 * @description: 员工异常类
 * @author: jinnian
 * @create: 2019-10-26 01:53
 **/
public class EmployeeException extends BaseException {

    public enum EmployeeExceptionEnum {
        @Error(code = ErrorKind.NOT_FOUND, message = "未找到员工档案信息")
        NOT_FOUND,

        @Error(code = ErrorKind.ALREADY_EXIST, message = "该证件号码的在职员工已存在，员工姓名为【%s】,电话为【%s】")
        IS_EXISTS,

        @Error(code = ErrorKind.BREACH_BUSINESS_RULE, message = "该证件号码的人员已被纳入我公司黑名单")
        IS_BLACK,

        @Error(code = ErrorKind.BREACH_BUSINESS_RULE, message = "复职与返聘员工要求在系统中已有员工档案，但目前系统中暂无此身份证的员工档案")
        CREATE_TYPE_ERROR,
    }

    public EmployeeException(String message, int code) {
        super(message, code);
    }

    public EmployeeException(EmployeeExceptionEnum exception, String... replaceTexts) {
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
