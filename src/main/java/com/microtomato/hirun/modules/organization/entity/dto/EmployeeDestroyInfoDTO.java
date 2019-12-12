package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 员工销户信息传输对象
 * @author:
 **/
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDestroyInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long employeeId;

    private String name;

    private String mobileNo;

    private String identityNo;

    private LocalDateTime destroyDate;

    private String destroyWay;

    private Integer destroyTimes;

    private String destroyReason;

    private String isBlackList;

    private LocalDate socialSecurityEnd;

    private Long newParentEmployeeId;
}
