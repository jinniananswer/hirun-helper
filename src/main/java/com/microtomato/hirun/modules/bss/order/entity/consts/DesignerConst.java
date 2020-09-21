package com.microtomato.hirun.modules.bss.order.entity.consts;

/**
 * @author ：xiaocl
 * @date ：Created in 2020/6/26 23:27
 * @description：设计师域常量
 * @modified By：
 * @version: 1$
 */
public class DesignerConst {
    /**
     * 助理设计师操作标识，助理设计师参与全房图
     */
    public static final String OPER_DRAW_CONSTRUCT = "draw_construct";

    /**
     * 助理设计师操作标识，助理设计师参与平面图
     */
    public static final String OPER_DRAW_PLAN = "draw_plane";

    /**
     * 助理设计师操作标识，助理设计师参与量房
     */
    public static final String OPER_MEASURE = "measure";

    /**
     * 水电设计师操作标识，参与水电
     */
    public static final String OPER_WATER_ELEC_DESIGN = "water_elec_design";

    /**
     * 全程都参与
     */
    public static final String OPER_WALL_IN_DESIGN = "all_in_design";

    /**
     * 客户文员
     * */
    public static final Long ROLE_CODE_CUSTOMER_ClERK = 34L;

    /**
     * 审图员
     * */
    public static final Long ROLE_CODE_DRAWING_REVIEWER = 44L;

    /**
     * 审图员
     * */
    public static final Long ROLE_CODE_ASSISTANT_DESIGNER = 41L;

}
