package com.microtomato.hirun.modules.bss.order.entity.dto.finance;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 非主营收款查询结果DTO
 * @author: jinnian
 * @create: 2020-10-31 04:09
 **/
@Getter
@Setter
public class NormalPayNoDTO {

    /**
     * 付款日期
     */
    private LocalDate payDate;

    /**
     * 应付总金额
     */
    private Double needPay;

    /**
     * 付款项名称
     */
    private String payItemName;

    /**
     * 付款编码，修改时使用
     */
    private Long payNo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 收款人
     */
    private String employeeName;

    /**
     * 收单会计
     */
    private String financeEmployeeName;

    /**
     * 状态名称
     */
    private String auditStatusName;

    /**
     * 单据状态
     */
    private String auditStatus;

    /**
     * 应付总金额
     */
    private Double totalMoney;
}
