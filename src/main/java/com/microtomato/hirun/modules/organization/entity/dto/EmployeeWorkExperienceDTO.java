package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;

import java.io.Serializable;

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
public class EmployeeWorkExperienceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String startDate;

    private String endDate;

    private String content;
}
