package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 奖惩数据传输对象
 * @author: liuhui7
 **/
@Data
public class EmployeePenaltyDTO {

    private Long Id;

    private Long employeeId;

    private String employeeName;

    private String penaltyType;

    private String penaltyStatus;

    private String penaltyContent;

    private String remark;

    private String orgPath;

    private LocalDateTime penaltyTime;

    private String penaltyScore;

    private String jobRoleName;

    private String jobRole;

    private Long orgId;

}
