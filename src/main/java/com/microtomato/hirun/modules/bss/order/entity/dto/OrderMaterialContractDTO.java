package com.microtomato.hirun.modules.bss.order.entity.dto;


import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 主材查询结果数据传输对象
 * @author: liuhui
 * @create: 2020-08-15 22:17
 **/
@Data
public class OrderMaterialContractDTO {

    private Long id;

    private Long orderId;

    /** 材料类型 */
    private String materialType;

    /** 合同金额 */
    private Long contractFee;

    /** 优惠金额 */
    private Long discountFee;

    private Long actualFee;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime payTime;

    private String brandCode;

    private String remark;

    private String custNo;

    private String agentName;

    private String designName;

    private String custName;

    private String projectManageName;

    private String mainMaterialName;

    private String cabinetDesignerName;

    private Long housesId;

    private String houseName;

    private Long shopId;

    private String shopName;

    private String brandCodeName;

    private String kindId;

    private String kindName;

    private String feeTypeName;
}
