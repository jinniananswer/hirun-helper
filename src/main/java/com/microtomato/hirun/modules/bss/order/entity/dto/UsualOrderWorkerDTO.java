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

    /**
     * 家装顾问
     */
    private String counselorName;

    /**
     * 客户代表
     */
    private String agentName;

    /**
     * 设计师
     */
    private String designerName;

    /**
     * 财务人员
     */
    private String financeName;

    /**
     * 主材管家
     */
    private String materialName;

    /**
     * 橱柜管家
     */
    private String cabinetName;

    /**
     * 橱柜设计师
     */
    private String cabinetDesignerName;

    /**
     * 报备人员
     */
    private String reportName;

    /**
     * 项目经理
     */
    private String projectManagerName;
}
