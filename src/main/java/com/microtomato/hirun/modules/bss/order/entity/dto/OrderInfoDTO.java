package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 订单信息数据传输对象
 * @author: jinnian
 * @create: 2020-02-03 01:21
 **/
@Data
public class OrderInfoDTO {

    private Long orderId;

    private Long custId;

    /**
     * 0-预订单 1-转入咨询阶段后的正式订单
     */
    private String type;

    /**
     * 楼盘ID
     */
    private Long housesId;

    /**
     * 楼盘名称
     */
    private String housesName;

    /**
     * 装修具体地址
     */
    private String decorateAddress;

    /**
     * 户型，见静态参数HOUSE_LAYOUT
     */
    private String houseLayout;

    /**
     * 户型名称
     */
    private String houseLayoutName;

    /**
     * 建筑面积
     */
    private String floorage;

    /**
     * 套内面积
     */
    private String indoorArea;

    /**
     * 订单阶段，对应0-酝酿，10-初选，30-初步决策，50-决策，60-施工，95-维护
     */
    private Integer stage;

    /**
     * 订单状态，见静态参数ORDER_STATUS
     */
    private String status;

    private String statusName;

    /**
     * 风格主题ID
     */
    private Long styleId;

    /**
     * 功能蓝图内容
     */
    private String funcContent;

    /**
     * 合同总金额
     */
    private Integer contractFee;

    /**
     * 总应收费用
     */
    private Integer totalFee;

    /**
     * 总实收费用
     */
    private Integer totalActFee;

    /**
     * 参与的活动ID
     */
    private Long activityId;

}
