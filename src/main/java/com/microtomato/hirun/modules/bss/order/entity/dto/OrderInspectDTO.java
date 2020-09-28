package com.microtomato.hirun.modules.bss.order.entity.dto;


import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 客户订单查询条件数据传输对象
 * @author: liuhui
 * @create: 2020-08-15 22:17
 **/
@Data
public class OrderInspectDTO {

    private Long id;

    private Long orderId;

    /** 申报时间 */
    private LocalDate applyDate;

    /** 机构 */
    private String institution;

    private String institutionName;

    /** 送达日期 */
    private LocalDate offerDate;

    /** 检测状态 */
    private String checkStatus;

    private String checkStatusName;

    /** 领取状态 */
    private String receiveStatus;

    private String receiveStatusName;

    /** 领取人 */
    private String receivePeople;

    /** 领取时间 */
    private LocalDate receiveDate;

    /** 领取情况 */
    private String receiveRemark;

    /** 通知情况 */
    private String noticeRemark;

    /** 备注 */
    private String remark;

    /** 保修开始时间 */
    private LocalDate guaranteeStartDate;

    /** 保修结束时间 */
    private LocalDate guaranteeEndDate;

    /** 是否保修 */
    private String isGuarantee;

    /** 结算状态 */
    private String settleStatus;

    private String settleStatusName;

    /** 检测结算时间 */
    private LocalDate checkSettleDate;

    private String custNo;

    private String custName;

    private Long houseId;

    private String houseAddress;

    private Long custServiceEmployeeId;

    private String customerServiceName;

    private Long designEmployeeId;

    private String designEmployeeName;

    private Long shopId;

    private String mobileNo;
}
