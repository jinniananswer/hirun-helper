package com.microtomato.hirun.modules.bss.salary.entity.dto;

import lombok.*;

/**
 * @program: hirun-helper
 * @description: 根据查询条件查询提成明细信息数据对象
 * @author: jinnian
 * @create: 2020-07-01 16:01
 **/
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class OrderSalaryRoyaltyDetailDTO extends EmployeeSalaryRoyaltyDetailDTO {

    private Long custId;

    private String custName;

    private String custNo;
}
