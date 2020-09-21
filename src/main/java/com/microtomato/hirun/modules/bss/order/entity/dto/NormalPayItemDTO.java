package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 非主营收款项数据传输对象
 * @author: sunxin
 * @create: 2020-03-30 01:18
 **/
@Data
public class NormalPayItemDTO {

    private String payItemId;

    private String payItemName;

    private String subjectId;

    private String subjectName;

    private String projectId;

    private String projectName;

    private Double money;
}
