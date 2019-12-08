package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;

/**
 * @program: hirun-helper
 * @description: 员工工作经历数据传输对象
 * @author: jinnian
 * @create: 2019-10-05 02:04
 **/
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeWorkExperienceDTO {

    private String startDate;

    private String endDate;

    private String content;
}
