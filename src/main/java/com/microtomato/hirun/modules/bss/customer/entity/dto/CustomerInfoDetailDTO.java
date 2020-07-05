package com.microtomato.hirun.modules.bss.customer.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 客户信息数据传输对象，主要用于客户资料的展示，如应用于客户信息展示组件
 * @author: liuhui
 **/
@Data
public class CustomerInfoDetailDTO {

    private Long custId;

    private String custNo;

    private String openId;

    private String customerName;

    private String partyName;

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

    private String hobby;

    private String hobbyName;

    private String otherHobby;

    private String custType;

    private String custTypeName;

    private String custProperty;

    private String custPropertyName;

    private String status;

    private Long houseId;

    private String houseName;

    private String houseMode;

    private String houseModeName;

    private String houseArea;

    private String houseBuilding;

    private String houseRoomNo;

    private String houseDetail;

    private String informationSource;

    private Long custServiceEmployeeId;

    private String customerServiceName;

    private Long designEmployeeId;

    private String designEmployeeName;

    private String remark;

    private Long linkEmployeeId;

    private Long houseCounselorId;

    private String houseCounselorName;

    private LocalDateTime consultTime;

    private String counselorHouseMode;

    private String houseAddress;

    private Long partyId;

    private String counselorMobileNo;

}
