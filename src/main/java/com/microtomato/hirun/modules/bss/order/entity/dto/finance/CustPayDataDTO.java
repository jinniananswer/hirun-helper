package com.microtomato.hirun.modules.bss.order.entity.dto.finance;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: hirun-helper
 * @description: 客户付款信息
 * @author: jinnian
 * @create: 2020-10-21 14:53
 **/
@Getter
@Setter
public class CustPayDataDTO {

    private Long custId;

    private Long orderId;

    private Long housesId;

    private String custName;

    private String address;

    private String payNoRemark;

    private String auditComment;
}
