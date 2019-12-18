package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 员工在职数据统计
 * @author: liuhui7
 **/
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeQuantityStatDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orgId;

    private String orgName;

    private Integer janurayCount;

    private Integer februaryCount;

    private Integer marchCount;

    private Integer aprilCount;

    private Integer mayCount;

    private Integer juneCount;

    private Integer julyCount;

    private Integer augustCount;

    private Integer septemberCount;

    private Integer octoberCount;

    private Integer novemberCount;

    private Integer decemberCount;


}
