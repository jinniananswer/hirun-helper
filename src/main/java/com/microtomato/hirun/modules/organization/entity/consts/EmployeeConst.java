package com.microtomato.hirun.modules.organization.entity.consts;

/**
 * @program: hirun-helper
 * @description: 员工常量对象类
 * @author: jinnian
 * @create: 2019-11-03 00:57
 **/
public class EmployeeConst {

    public static final String CREATE_TYPE_NEW_ENTRY = "1";

    public static final String CREATE_TYPE_REHIRE = "2";

    public static final String CREATE_TYPE_REHELLORING = "2";

    public static final String OPER_TYPE_CREATE = "create";

    public static final String OPER_TYPE_EDIT = "edit";

    public static final String STATUS_NORMAL = "0";

    public static final String STATUS_RETIRE = "1";

    public static final String STATUS_DESTROY = "3";

    public static final String JOB_ROLE_MAIN = "1";

    //全职员工
    public static final String TYPE_FULL_TIME = "1";

    //外聘员工
    public static final String TYPE_OUTSIDE = "2";

    //返聘员工
    public static final String TYPE_REHELLORING = "3";

    //实习员工
    public static final String TYPE_PRACTICE = "4";

    public static final String HISTORY_EVENT_ENTRY = "1";

    public static final String HISTORY_EVENT_REHIRE = "2";

    public static final String HISTORY_EVENT_REHELLORING = "3";

    public static final String HISTORY_EVENT_DESTROY = "4";

    //调岗
    public static final String HISTORY_EVENT_TRANS = "5";

    //借调
    public static final String HISTORY_EVENT_BORROW = "6";

    //离职方式-退休
    public static final String DESTORY_WAY_RETIRE = "5";

    /**
     * 培训协议
     */
    public static final String CONTRACT_TYPE_TRAIN = "4";

    /**
     * 保密
     */
    public static final String CONTRACT_TYPE_SECRET = "5";

    /**
     * 合同时间变更类型
     */
    public static final String CONTRACT_TYPE_POSTPONE = "6";

    /**
     * 合同岗位变更
     */
    public static final String CONTRACT_TYPE_CHANGE_ROLE = "7";

    /**
     * 合同地点变更类型
     */
    public static final String CONTRACT_TYPE_CHANGE_PLACE = "8";

    /**
     * 合同试用期变更
     */
    public static final String CONTRACT_TYPE_CHANGE_PROBLATION = "9";

    /**
     * 合同其他变更类型
     */
    public static final String CONTRACT_TYPE_OTHER = "10";

    /**
     * 合同正常状态
     */
    public static final String CONTRACT_STATUS_NORMAL = "1";

    /**
     * 合同结束状态
     */
    public static final String CONTRACT_STATUS_END = "2";

    /**
     * 合同删除状态
     */
    public static final String CONTRACT_STATUS_DELETE = "3";

    /**
     * 是
     */
    public static final String YES="1";
    /**
     * 否
     */
    public static final String NO="2";

    /**
     * 休假状态
     */
    public static final String EMPLOYEE_HOLIDAY_STATUS="1";

    /**
     * 借调状态
     */
    public static final String EMPLOYEE_BORROW_STATUS="2";

    /**
     * 调出状态
     */
    public static final String EMPLOYEE_TRANS_STATUS="3";

    /**
     * 男
     */
    public static final String EMPLOYEE_SEX_MAN="1";

    /**
     * 女
     */
    public static final String EMPLOYEE_SEX_WOMAN="2";

    /**
     * 员工关键人类型 - 子女
     */
    public static final String KEYMAN_TYPE_CHILD = "1";

    /**
     * 员工关键人类型 - 联系人
     */
    public static final String KEYMAN_TYPE_CONTACT = "2";
}
