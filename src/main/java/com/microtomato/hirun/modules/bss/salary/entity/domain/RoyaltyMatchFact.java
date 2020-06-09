package com.microtomato.hirun.modules.bss.salary.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @program: hirun-helper
 * @description: 提成条件匹配因子对象
 * @author: jinnian
 * @create: 2020-05-17 18:03
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoyaltyMatchFact {

    private Long employeeId;

    private String jobRole;

    private String jobGrade;

    private Set<Long> roleIds;

    /**
     * 参与角色
     */
    private Long roleId;

    private Long orgId;

    /**
     * 部门性质 市场部 客户部 设计部
     */
    private String nature;

    /**
     * 预算总金额
     */
    private Long budget;

    /**
     * 优惠后合同金额
     */
    private Long contractFee;

    /**
     * 设计费标准
     */
    private Integer designFeeStandard;

    /**
     * 是否优仕馆
     */
    private boolean isExcellent;

    /**
     * 户型
     */
    private String houseMode;

    /**
     * 已收取的量房费
     */
    private Long measurePayed;

    /**
     * 套内面积
     */
    private Long indoorArea;

    /**
     * 流程所做动作
     */
    private String action;
}
