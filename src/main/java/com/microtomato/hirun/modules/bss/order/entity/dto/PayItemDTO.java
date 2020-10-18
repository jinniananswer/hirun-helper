package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 收款项数据传输对象
 * @author: jinnian
 * @create: 2020-02-23 01:18
 **/
@Data
public class PayItemDTO {

    private String payItemId;

    private String payItemName;

    private String period;

    private String periodName;

    private Double money;

    private String remark;
}
