package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @program: hirun-helper
 * @description: 员工通用统计数据对象
 * @author: jinnian
 * @create: 2019-11-22 02:28
 **/
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePieStatisticDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String num;
}
