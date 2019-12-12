package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 待办数据传输对象
 * @author: liuhui7
 **/
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class HrPendingInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long Id;

    private Long employeeId;

    private String employeeName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String pendingType;

    private String pendingStatus;

    private Long pendingCreateId;

    private String pendingCreateName;

    private Long pendingExecuteId;

    private String pendingExecuteName;

    private String content;

    private String remark;

    private LocalDateTime createTime;

    private EmployeeJobRoleDTO employeeJobRole;

    private String home;

    private Integer homeProv;

    private Integer homeCity;

    private Integer homeRegion;

    private String homeAddress;
}
