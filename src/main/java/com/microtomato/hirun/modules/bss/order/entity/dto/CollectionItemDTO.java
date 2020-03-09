package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 非主营收款项数据传输对象
 * @author: sunxin
 *  @create: 2020-03-9 12:47
 **/
@Data
public class CollectionItemDTO {

    private String collectionItemId;

    private String collectionItemName;

    private Integer period;

    private String periodName;

    private Double money;
}
