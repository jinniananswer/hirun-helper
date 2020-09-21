package com.microtomato.hirun.modules.bss.salary.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 查询提成明细数据对象
 * @author: jinnian
 * @create: 2020-07-01 11:45
 **/
@Data
public class QueryRoyaltyDetailDTO {

    private String custName;

    private String mobileNo;

    private Long houseId;

    private String orgIds;

    private String auditStatus;

    private Integer salaryMonth;

    private Integer page;

    private Integer limit;

    private Integer count;

    private Integer projectPage;

    private Integer projectLimit;

    private Integer projectCount;
}
