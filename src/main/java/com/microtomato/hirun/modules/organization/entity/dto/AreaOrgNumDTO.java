package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;

/**
 * @program: hirun-helper
 * @description: 部门数量统计传输对象
 * @author: jinnian
 * @create: 2019-12-01 14:02
 **/
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AreaOrgNumDTO {

    private String id;

    private String area;

    private Integer value;

    private String name;
}
