package com.microtomato.hirun.modules.bss.order.entity.dto.factory;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 编辑工厂订单DTO
 * @author: jinnian
 * @create: 2020-09-26 23:26
 **/
@Data
public class FactoryOrderDTO {

    private Long id;

    /** 订单ID */
    private Long orderId;

    /** 生产批号 */
    private String produceNo;

    /** 驻点人员 */
    private String garrisonName;

    /** 接收图纸日期 */
    private LocalDate receivePictureDate;

    /** 接收预算日期 */
    private LocalDate receiveBudgetDate;

    /** 接收尺寸表日期 */
    private LocalDate receiveSizeDate;

    /** 驻点收到日期 */
    private LocalDate receiveGarrisonDate;

    /** 订单正本日期 */
    private LocalDate orderOriginalDate;

    /** 收到订单正本日期 */
    private LocalDate receiveOrderOriginalDate;

    /** 签单项目 */
    private String signItem;

    /** 产品描述 */
    private String productDescription;

    /** 装修验收日期 */
    private LocalDate checkDate;

    /** 交付日期 */
    private LocalDate deliverDate;

    /** 木门预计日期 */
    private LocalDate doorExpectedDate;

    /** 木门实际到货日期 */
    private LocalDate doorActualDate;

    /** 实际入库日期 */
    private LocalDate storageDate;

    /** 家具预计日期 */
    private LocalDate furnitureExpectedDate;

    /** 家具实际到货日期 */
    private LocalDate furnitureActualDate;

    /** 安装日期 */
    private LocalDate setupDate;

    /** 跟踪情况 */
    private String followInfo;

    /** 工厂进度 */
    private String factorySchedule;

    /** 返工及变更 */
    private String backChange;

    /** 责任方 */
    private String responsible;

    /** 不做木制品 */
    private String noWood;

    /** 跟单完毕 */
    private String followFinish;

    /** 驻点重新接单日期 */
    private LocalDate regarrisonDate;

    /** 驻点核尺日期 */
    private LocalDate garrisonCheckSizeDate;

    /** 状态 */
    private String status;

    /** 订单管理员 */
    private Long factoryOrderManager;

    private List<FactoryOrderFollowDTO> follows;
}
