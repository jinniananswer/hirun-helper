package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 员工业绩信息
 * @author: jinnian
 * @create: 2020-02-15 17:02
 **/
@Data
public class EmployeeResultsDTO {

    private String employeeName;

    private String employeeLevel;

    private Integer orderCount = 0;

    private Double contractTotalFee = 0d;

    private Double doorFee = 0d;

    private Double furnitureFee = 0d;

    private Double mainMaterialFee = 0d;

    private Double cupboardFee = 0d;

    private String orgName;

}
