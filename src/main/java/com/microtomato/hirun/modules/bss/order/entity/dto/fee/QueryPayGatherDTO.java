package com.microtomato.hirun.modules.bss.order.entity.dto.fee;

import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 主营收款查询条件数据对象
 * @author: jinnian
 * @create: 2020-10-11 18:21
 **/
@Data
public class QueryPayGatherDTO {

    private String orgIds;

    private LocalDate[] queryDate;
}
