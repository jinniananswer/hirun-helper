package com.microtomato.hirun.modules.college.knowhow.consts;

/**
 * @author huanghua
 * @date 2020-08-16
 */
public class KnowhowConsts {

    /**
     * 有效
     */
    public static final String NORMAL_STATUS_VALID = "1";

    /**
     * 失效
     */
    public static final String NORMAL_STATUS_INVALID = "0";

    /**
     * 失效问题
     */
    public static final String QUESTION_STATUS_INVALID = "0";

    /**
     * 已发布未审批问题
     */
    public static final String QUESTION_STATUS_UNAPPROVED = "1";

    /**
     * 审批失败问题
     */
    public static final String QUESTION_STATUS_APPROVE_FAILED = "5";

    /**
     * 已审批未回复问题
     */
    public static final String QUESTION_STATUS_UNREPLY = "2";

    /**
     * 已回复问题
     */
    public static final String QUESTION_STATUS_REPLY = "3";

    /**
     * 已发布
     */
    public static final String QUESTION_STATUS_DEPLOYED = "4";

    /**
     * 其他类型
     */
    public static final String QUESTION_TYPE_OTHER = "3";

    public static final String QUESTION_VERIFY_APPROVED = "1";

    public static final String QUESTION_VERIFY_APPROVE_FAILED = "0";

    public static final String OPTION_APPROVE = "APPROVE";

    public static final String OPTION_REPLY = "REPLY";

    public static final String OPTION_PUBLISH = "PUBLISH";

    public static final String QUESTION_SORT_BY_CLICKS = "1";

    public static final String QUESTION_SORT_BY_CREATTIME = "0";
}
