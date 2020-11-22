package com.microtomato.hirun.framework.util;

import com.microtomato.hirun.framework.exception.cases.AlreadyExistException;
import com.microtomato.hirun.framework.exception.cases.NotFoundException;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

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

    private static final String SPLIT = "、";

    /**
     * 线程安全，此对象创建成本很高！
     * 通过 ValidatorFactory 得到了一个 Validator 的实例. Validator 是线程安全的，可以重复使用。
     */
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 基于 JSR303 规则的验证
     * <p>
     * 如果返回 null 则表示没有错误
     *
     * @param obj
     * @return
     */
    public static String jsr303Check(Object obj) {

        if (null == obj) {
            return "入参不能为空！";
        }

        Set<ConstraintViolation<Object>> validResult = validator.validate(obj);
        if (null != validResult && validResult.size() > 0) {
            StringBuilder sb = new StringBuilder();

            for (ConstraintViolation<Object> violation : validResult) {
                if (StringUtils.isNotBlank(violation.getMessage())) {
                    sb.append(violation.getMessage()).append(SPLIT);
                } else {
                    sb.append(violation.getPropertyPath().toString()).append("不合法").append(SPLIT);
                }
            }

            return StringUtils.stripEnd(sb.toString(), SPLIT);

        }
        return null;
    }

}
