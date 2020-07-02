package com.microtomato.hirun.modules.bss.customer.entity.dto;


import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 图表信息信息查询条件传输对象
 * @author: liuhui
 * @create: 2020-02-05 23:54
 **/
@Data
public class ReportQueryCondDTO {

    private Long orgId;

    private Integer month;

    private String queryType;

    private Long employeeId;

    private Long companyId;

}
