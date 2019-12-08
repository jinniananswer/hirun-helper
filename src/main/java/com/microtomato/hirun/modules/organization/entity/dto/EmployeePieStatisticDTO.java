package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;

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
public class EmployeePieStatisticDTO {

    private String name;

    private String num;
}
