package com.microtomato.hirun.framework.exception;

/**
 * 异常编码枚举，业务异常的抽象概括。异常编码范围: 400001 -> 500000 <br/>
 * <p>
 * 比如：<br/>
 * 根据 "员工编码" 找不到员工记录时，抛 NOT_FOUND；新增员工如果已经存在，抛 ALREADY_EXIST。
 *
 * @author Steven
 * @date 2019-04-25
 */
public enum ErrorKind {

    /**
     * MybatisPlus通用异常
     */
    MABATIS_PLIUS_EXCEPTION(400003),

    /**
     * 不存在
     */
    NOT_FOUND(400001),

    /**
     * 已存在
     */
    ALREADY_EXIST(400002),

    /**
     * 不匹配
     */
    MISMATCHING(100001);

    private int code;

    ErrorKind(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
