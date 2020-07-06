package com.microtomato.hirun.modules.bss.config.entity.consts;

/**
 * @program: hirun-helper
 * @description: 费用相关常量类
 * @author: jinnian
 * @create: 2020-03-04 21:47
 **/
public class FeeConst {

    /**
     * 设计费用
     */
    public static final String FEE_TYPE_DESIGN = "1";

    /**
     * 工程费用
     */
    public static final String FEE_TYPE_PROJECT = "2";

    /**
     * 首期款
     */
    public static final int FEE_PERIOD_FIRST = 1;

    /**
     * 二期款
     */
    public static final int FEE_PERIOD_SECOND = 2;

    /**
     * 结算款
     */
    public static final int FEE_PERIOD_END = 3;

    /**
     * 正向收费 增加费用
     */
    public static final Integer FEE_DIRECTION_PLUS = new Integer(0);

    /**
     * 反向收费 扣减费用
     */
    public static final Integer FEE_DIRECTION_MINUS = new Integer(1);
}
