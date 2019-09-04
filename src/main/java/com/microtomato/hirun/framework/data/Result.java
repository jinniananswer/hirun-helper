package com.microtomato.hirun.framework.data;

import lombok.Data;

import java.io.Serializable;

/**
 * 响应格式接口, code 返回 0 表示成功
 *
 * @author Steven
 * @date 2019-04-25
 */
@Data
public class Result implements Serializable {

    /**
     * 数据行数
     */
    private Integer total;

    /**
     * 数据
     */
    private Object rows;

    /**
     * 返回编码 0: 正常, 非0: 异常
     */
    private Integer code;

    /**
     * 异常时返回的错误信息
     */
    private String message;

}
