package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 创建订单用到的数据传输对象
 * @author: jinnian
 * @create: 2020-02-07 00:12
 **/
@Data
public class NewOrderDTO {

    private Long custId;

    /**
     * H-家装订单 W-木制品订单 P-预订单
     */
    private String type;

    /**
     * 楼盘ID
     */
    private Long housesId;

    /**
     * 装修具体地址
     */
    private String decorateAddress;

    /**
     * 户型，见静态参数HOUSE_LAYOUT
     */
    private String houseLayout;

    /**
     * 建筑面积
     */
    private String floorage;

    /**
     * 套内面积
     */
    private String indoorArea;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 参与的活动ID
     */
    private Long activityId;
}
