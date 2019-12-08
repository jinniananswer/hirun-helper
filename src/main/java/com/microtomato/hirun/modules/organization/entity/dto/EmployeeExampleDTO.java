package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 测试（后期删除）
 * @author Steven
 */
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeExampleDTO implements Serializable {
    private String name;
    private Long employeeId;
    private Integer sex;
    private String mobileNo;
    private String identityNo;
    private Integer status;
    private String inDate;
    private Long jobRole;
    private Long orgId;
    private String orgName;
}
