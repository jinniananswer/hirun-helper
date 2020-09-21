package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 客户跑单信息数据传输对象
 * @create: 2020-02-14 01:21
 **/
@Data
public class OrderEscapeDTO {

    private Long id;

    private Long orderId;

    private String status;

    /**
     * 跑单节点
     */
    private String escapeNode;
    /**
     * 节点名称
     */
    private String escapeNodeName;

    private String escapeType;

    private String escapeTrend;

    private String escapeCause;

    private String escapeRemark;


}
