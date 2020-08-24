package com.microtomato.hirun.modules.bss.order.entity.dto.statistic;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 根据员工对订单数量进行统计
 * @author: jinnian
 * @create: 2020-07-28 11:01
 **/
@Data
public class EmployeeYearOrderCountDTO {

    private Long month;

    private Long num;
}
