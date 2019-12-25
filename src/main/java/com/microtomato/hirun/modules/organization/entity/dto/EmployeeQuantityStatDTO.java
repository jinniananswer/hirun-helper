package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 员工在职数据传输对象
 * @author: liuhui7
 **/
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeQuantityStatDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orgId;

    private String orgName;

    private String year;

    private Integer employeeQuantity;

    private String month;

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

    private Long parentOrgId;

    T node;

    List<EmployeeQuantityStatDTO>  statDTOS;

    private Boolean spread;
}
