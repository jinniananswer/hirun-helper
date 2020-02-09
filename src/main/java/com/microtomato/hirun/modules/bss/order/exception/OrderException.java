package com.microtomato.hirun.modules.bss.order.exception;

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
public class OrderException extends BaseException {

    public enum OrderExceptionEnum {
        @Error(code = ErrorKind.NOT_FOUND, message = "未找到订单状态转换配置信息")
        STATUS_TRANS_CFG_NOT_FOUND,

        @Error(code = ErrorKind.NOT_FOUND, message = "未找到订单状态配置信息")
        STATUS_CFG_NOT_FOUND
    }

    public OrderException(String message, int code) {
        super(message, code);
    }

    public OrderException(OrderExceptionEnum exception, String... replaceTexts) {
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
