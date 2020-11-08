package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 非主营收款业务数据传输对象
 * @author: sunxin
 * @create: 2020-03-30 00:00
 **/
@Data
public class NonCollectFeeDTO {

    /**
     * 付款日期
     */
    private LocalDate payDate;

    /**
     * 应付总金额
     */
    private Double needPay;

    /**
     * 付款编码，修改时使用
     */
    private Long payNo;


    /**
     * 付款项
     */
    private List<NormalPayItemDTO> payItems;

    /**
     * 付款方式明细
     */
    private List<PaymentDTO> payments;

    /**
     * 备注
     */
    private String remark;

    /**
     * 收款人
     */
    private String employeeName;

    /**
     * 状态名称
     */
    private String auditStatusName;

    /**
     * 单据状态
     */
    private String auditStatus;

    /**
     * 复核意见
     */
    private String auditComment;

    /**
     * 应付总金额
     */
    private Double totalMoney;
}
