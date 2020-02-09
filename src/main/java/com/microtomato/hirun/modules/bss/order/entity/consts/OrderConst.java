package com.microtomato.hirun.modules.bss.order.entity.consts;

/**
 * @program: hirun-helper
 * @description: 订单常量类
 * @author: jinnian
 * @create: 2020-02-07 00:24
 **/
public class OrderConst {

    /**
     * 订单咨询状态
     */
    public static String ORDER_STATUS_ASKING = "2";

    /**
     * 创建订单日志内容
     */
    public static String OPER_LOG_CONTENT_CREATE = "创建客户订单";

    /**
     * 创建订单日志内容
     */
    public static String OPER_LOG_CONTENT_STATUS_CHANGE = "订单状态变化";

    /**
     * 订单状态往下一状态流转操作码
     */
    public static String OPER_NEXT_STEP = "NEXT";

    /**
     * 订单状态审核不通过
     */
    public static String OPER_AUDIT_NO = "AUDIT_NO";
}
