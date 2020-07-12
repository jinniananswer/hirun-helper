package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 常用查询时展示的订单相关人员信息
 * @author: jinnian
 * @create: 2020-07-12 01:47
 **/
@Data
public class UsualOrderWorkerDTO {

    private String counselorName;

    private String agentName;

    private String designerName;

    private String financeName;

    private String materialName;

    private String cabinetName;

    private String reportName;

    private String projectManagerName;
}
