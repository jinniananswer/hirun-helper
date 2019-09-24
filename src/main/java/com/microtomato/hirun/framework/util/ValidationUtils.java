package com.microtomato.hirun.framework.util;

import com.microtomato.hirun.framework.exception.cases.AlreadyExistException;
import com.microtomato.hirun.framework.exception.cases.NotFoundException;

import static com.microtomato.hirun.framework.exception.ErrorKind.ALREADY_EXIST;
import static com.microtomato.hirun.framework.exception.ErrorKind.NOT_FOUND;

/**
 * @author Steven
 */
public final class ValidationUtils {

    public static void isNotFound(Boolean b, String entity, String parameter, Object value) {
        if (!b) {
            String msg = entity
                + " is not found! "
                + "{ " + parameter + ":" + value.toString() + " }";
            throw new NotFoundException(msg, NOT_FOUND.getCode());
        }
    }

    /**
     * 判断是否找不到
     *
     * @param object
     * @param entity
     * @param parameter
     * @param value
     */
    public static void isNotFound(Object object, String entity, String parameter, Object value) {
        if (null == object) {
            String msg = entity
                    + " is not found! "
                    + "{ " + parameter + ":" + value.toString() + " }";
            throw new NotFoundException(msg, NOT_FOUND.getCode());
        }
    }

    /**
     * 判断是否找不到
     *
     * @param ret
     * @param entity
     * @param parameter
     * @param value
     */
    public static void isNotFound(int ret, String entity, String parameter, Object value) {
        if (0 == ret) {
            String msg = entity
                    + " is not found! "
                    + "{ " + parameter + ":" + value.toString() + " }";
            throw new NotFoundException(msg, NOT_FOUND.getCode());
        }
    }

    /**
     * 判断是否已经存在
     *
     * @param ret
     * @param entity
     * @param parameter
     * @param value
     */
    public static void isAlreadyExist(int ret, String entity, String parameter, Object value) {
        if (0 == ret) {
            String msg = entity
                    + " already exist! "
                    + "{ " + parameter + ":" + value.toString() + " }";
            throw new AlreadyExistException(msg, ALREADY_EXIST.getCode());
        }
    }

}
