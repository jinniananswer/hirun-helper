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
public class OrderMaterialFeeDetailDTO {

    private Long id;

    private Long orderId;

    private Long fee;

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

    private Long houseId;

    private String houseName;

    private String brandCodeName;

    private String feeTypeName;

    private String parentItemId;

    private String parentItemName;

    private String periodsName;
}
