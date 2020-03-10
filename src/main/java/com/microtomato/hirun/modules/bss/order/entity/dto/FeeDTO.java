package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 费用数据类
 * @author: jinnian
 * @create: 2020-03-02 21:13
 **/
@Data
public class FeeDTO {

    private Long feeItemId;

    private Double money;
}
