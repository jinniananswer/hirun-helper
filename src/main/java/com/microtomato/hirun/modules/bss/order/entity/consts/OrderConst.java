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
    public static final String ORDER_STATUS_ASKING = "2";

    /**
     * 创建订单日志内容
     */
    public static final String OPER_LOG_CONTENT_CREATE = "创建客户订单";

    /**
     * 创建订单日志内容
     */
    public static final String OPER_LOG_CONTENT_STATUS_CHANGE = "订单状态变化";

    /**
     * 订单操作往下一状态流转操作码
     */
    public static final String OPER_NEXT_STEP = "NEXT";

    /**
     * 订单操作审核不通过
     */
    public static final String OPER_AUDIT_NO = "AUDIT_NO";

    /**
     * 订单操作延迟
     */
    public static final String OPER_DELAY = "DELAY";

    /**
     * 订单操作跑单
     */
    public static final String OPER_RUN = "RUN";

    /**
     * 日志类型-创建订单
     */
    public static final String LOG_TYPE_CREATE = "1";

    /**
     * 日志类型-订单转换
     */
    public static final String LOG_TYPE_STATUS_TRANS = "2";

    /**
     * 订单类型-预订单
     */
    public static final String ORDER_TYPE_PRE = "P";

    /**
     * 订单类型-家装订单
     */
    public static final String ORDER_TYPE_HOME = "H";

    /**
     * 订单类型-木制品订单
     */
    public static final String ORDER_TYPE_WOOD = "W";

    /**
     * 审核状态-未审核
     */
    public static final String AUDIT_STATUS_INIT = "0";

    /**
     * 审核状态-审核通过
     */
    public static final String AUDIT_STATUS_YES = "1";

    /**
     * 审核状态-审核不通过
     */
    public static final String AUDIT_STATUS_NO = "2";
}
