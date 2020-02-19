package com.microtomato.hirun.modules.bss.customer.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 客户报备信息传输对象
 * @author: liuhui
 * @create: 2020-02-01 23:54
 **/
@Data
public class CustPreparationDTO {

    private String custNo;

    private String custName;

    private String mobileNo;

    private String houseMode;

    private Long houseId;

    private String houseBuilding;

    private String houseRoomNo;

    private String houseAddress;

    private Long prepareOrgId;

    private Long prepareEmployeeId;

    private String prepareEmployeeName;

    private LocalDateTime prepareTime;

    private Long enterEmployeeId;

    private String enterEmployeeName;

    private String custProperty;

    private String custPropertyName;

    private String refereeFixPlace;

    private String refereeName;

    private String refereeMobileNo;

    private String remark;

    private LocalDateTime consultTime;

    private Integer status;

    private String preparationStatusName;

    private Long rulingEmployeeId;

    private String rulingRemark;

    private Long custId;

    private LocalDateTime enterTime;

    private String houseArea;

    private String houseModeName;

    private Long id;
}
