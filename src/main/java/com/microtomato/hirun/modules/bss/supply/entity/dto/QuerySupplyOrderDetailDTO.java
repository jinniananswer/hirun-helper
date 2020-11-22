package com.microtomato.hirun.modules.bss.supply.entity.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: hirun-helper
 * @description: 材料下单查询条件数据对象
 * @author: jinnian
 * @create: 2020-11-20 22:43
 **/
@Getter
@Setter
public class QuerySupplyOrderDetailDTO {

    private String auditStatus;

    private Integer page;

    private Integer limit;

    private Integer count;
}
