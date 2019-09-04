package com.microtomato.hirun.framework.exception.cases;

import com.microtomato.hirun.framework.exception.BaseException;

/**
 * @author Steven
 */
public class NotFoundException extends BaseException {
    public NotFoundException(String message, int code) {
        super(message, code);
    }
}
