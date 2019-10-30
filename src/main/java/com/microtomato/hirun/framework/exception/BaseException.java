package com.microtomato.hirun.framework.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础异常
 *
 * @author Steven
 * @date 2019-04-26
 */
@Setter
@Getter
public class BaseException extends RuntimeException {

    /**
     * 异常编码
     */
    private int code;

    /**
     * 异常信息
     */
    private String message;

    public BaseException(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public BaseException() {

    }
}
