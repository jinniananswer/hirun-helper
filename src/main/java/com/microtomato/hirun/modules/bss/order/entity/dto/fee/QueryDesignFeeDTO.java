package com.microtomato.hirun.modules.bss.order.entity.dto.fee;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 设计费用查询条件数据对象
 * @author: jinnian
 * @create: 2020-07-04 15:09
 **/
@Data
public class QueryDesignFeeDTO {

    private String custName;

    private String mobileNo;

    private Long housesId;

    private String shopIds;

    private String[] feeTime;

    private Integer page;

    private Integer limit;

    private Integer count;
}
