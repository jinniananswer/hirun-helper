package com.microtomato.hirun.modules.bss.customer.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 客户信息数据传输对象，主要用于客户资料的展示，如应用于客户信息展示组件
 * @author: jinnian
 * @create: 2020-02-01 23:54
 **/
@Data
public class CustInfoDTO {

    private Long custId;

    private String custNo;

    private String openId;

    private String custName;

    private String mobileNo;

    private String sex;

    private String sexName;

    private String age;

    private String wxNick;

    private String headUrl;

    private String qqNo;

    private String wxNo;

    private String profession;

    private String educational;

    private String familyIncome;

    private String familyMembersCount;

    private String oldmanCount;

    private String oldwomanCount;

    private String olderDetail;

    private String boyCount;

    private String girlCount;

    private String childDetail;

    private String hobby;

    private String otherHobby;

    private String custType;

    private String custTypeName;

    private Long prepareEmployeeId;

    private String prepareEmployeeName;

    private LocalDateTime prepareTime;

    private String custProperty;

    private String custPropertyName;

    private String status;

    private LocalDateTime preparationExpireTime;

    private Long rulingEmployeeId;

    private String rulingEmployeeName;

    private LocalDateTime rulingTime;

    private String rulingRemark;

    private LocalDateTime consultTime;

    private Long prepareId;

    private Long houseId;

    private String houseMode;

    private String houseModeName;

    private String houseArea;

    private String houseBuilding;

    private String houseRoomNo;

    private Long enterEmployeeId;

    private String enterEmployeeName;

    private Boolean allowSelect;

    private LocalDateTime enterTime;

    private String houseAddress;

    private Integer prepareStatus;

    private String prepareStatusName;

    private Long orderId;
}
