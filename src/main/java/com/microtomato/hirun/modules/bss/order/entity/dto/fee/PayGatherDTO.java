package com.microtomato.hirun.modules.bss.order.entity.dto.fee;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 主营收款查询结果数据对象
 * @author: jinnian
 * @create: 2020-10-11 18:23
 **/
@Data
public class PayGatherDTO {

    private String orgName;

    private String orgId;

    private String queryDate;

    private Double money;
}
