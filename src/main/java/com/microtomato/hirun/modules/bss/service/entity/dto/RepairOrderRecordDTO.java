package com.microtomato.hirun.modules.bss.service.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 员工维修传输对象
 * @author: liuhui
 * @create: 2020-07-12 14:24
 **/
@Data
public class RepairOrderRecordDTO {

    /**
     * 历史维修记录
     */
    List<RepairOrderDTO> historyRepairRecord;
    /**
     * 保修卡记录
     */
    GuaranteeDTO guaranteeInfo;

    /**
     *
     */
    List<RepairOrderDTO> repairNoRecord;
}
