package com.microtomato.hirun.modules.bss.service.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 投诉传输对象
 * @author: liuhui
 * @create: 2020-07-12 14:24
 **/
@Data
public class ComplainOrderRecordDTO {

    /**
     * 历史投诉记录
     */
    List<ComplainOrderDTO> historyComplainRecord;
    /**
     * 保修卡记录
     */
    GuaranteeDTO guaranteeInfo;

    /**
     *在途的记录
     */
    List<ComplainOrderDTO> complainRecord;
}
