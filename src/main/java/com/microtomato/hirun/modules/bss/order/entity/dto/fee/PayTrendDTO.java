package com.microtomato.hirun.modules.bss.order.entity.dto.fee;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 主营收入趋势数据对象
 * @author: jinnian
 * @create: 2020-10-13 01:34
 **/
@Data
public class PayTrendDTO {

    private Long orgId;

    private String name;

    private String month;

    private Double money;
}
