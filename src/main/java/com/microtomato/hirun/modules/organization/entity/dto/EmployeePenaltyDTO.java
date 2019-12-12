package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 奖惩数据传输对象
 * @author: liuhui7
 **/
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePenaltyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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
