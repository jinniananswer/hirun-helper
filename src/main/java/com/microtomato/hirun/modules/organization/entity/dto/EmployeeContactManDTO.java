package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 员工紧急联系人数据传输对象
 * @author: jinnian
 * @create: 2019-12-11 15:47
 **/
@Data
public class EmployeeContactManDTO {

    private String name;

    private String relType;

    private String relTypeName;

    private String contactNo;

}
