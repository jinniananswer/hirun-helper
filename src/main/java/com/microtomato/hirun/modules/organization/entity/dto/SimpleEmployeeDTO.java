package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 员工档案简要信息数据传输对象，多用于下拉框选择员工
 * @author: jinnian
 * @create: 2020-02-04 22:31
 **/
@Data
public class SimpleEmployeeDTO {

    private Long employeeId;

    private String name;

    private Long orgId;

    private String jobRole;

    /**
     * 完整部门路径
     */
    private String orgPath;

    /**
     * 归属部门名称
     */
    private String orgName;

    /**
     * 归属门店名称
     */
    private String shopName;

    /**
     * 归属公司名称
     */
    private String companyName;
}
