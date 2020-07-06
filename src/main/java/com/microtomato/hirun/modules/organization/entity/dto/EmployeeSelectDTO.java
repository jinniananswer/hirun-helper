package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 员工选择vue组件数据传输对象
 * @author: jinnian
 * @create: 2020-04-25 16:12
 **/
@Data
public class EmployeeSelectDTO {

    private Long orgId;

    private Long roleId = -1L;

    private String mode;
}
