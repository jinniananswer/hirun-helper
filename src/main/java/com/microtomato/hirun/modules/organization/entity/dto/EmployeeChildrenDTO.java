package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 员工子女信息数据传输对象
 * @author: jinnian
 * @create: 2019-11-19 17:41
 **/
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeChildrenDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String sex;

    private String sexName;

    private LocalDate birthday;

    private Integer age;

}
