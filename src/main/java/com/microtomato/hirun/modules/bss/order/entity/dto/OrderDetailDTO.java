package com.microtomato.hirun.modules.bss.order.entity.dto;

import com.microtomato.hirun.modules.bss.customer.entity.dto.CustInfoDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.XQLTEInfoDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.XQLTYInfoDTO;
import lombok.Data;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 订单信息数据传输对象,订单信息组件会用到
 * @author: jinnian
 * @create: 2020-02-03 01:21
 **/
@Data
public class OrderDetailDTO {

    private Long orderId;

    private Long custId;

    /**
     * H -家装订单  W-木制品订单  P-预订单
     */
    private String type;

    /**
     * 类型名称
     */
    private String typeName;

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

    /**
     * 状态名称
     */
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
     * 切换到订单信息展示组件的哪一个标签页
     */
    private String tabShow;

    /**
     * 参与的活动ID
     */
    private Long activityId;

    /**
     * 上一步状态
     */
    private String previousStatus;

    /**
     * 上一步状态
     */
    private String previousStatusName;

    /**
     * 客户信息
     */
    private CustInfoDTO customer;

    /**
     * 订单费用信息
     */
    private List<OrderFeeInfoDTO> orderFees;

    /**
     * 订单付款信息
     */
    private List<OrderPayInfoDTO> orderPays;

    /**
     * 订单上传文件信息
     */
    private List<OrderFileDTO> orderFiles;

    /**
     * 订单工作人员信息
     */
    private List<OrderWorkerDetailDTO> orderWorkers;

    /**
     * 订单合同信息
     */
    private List<OrderContractDTO> orderContracts;

    /**
     * 订单优惠项目
     */
    private List<OrderDiscountItemDTO> orderDiscountItems;

    private List<XQLTYInfoDTO> xqltyInfo;

    private XQLTEInfoDTO xqlteInfo;
}
