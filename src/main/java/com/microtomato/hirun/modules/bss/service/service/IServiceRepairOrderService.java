package com.microtomato.hirun.modules.bss.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.service.entity.dto.QueryRepairCondDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.RepairOrderDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.RepairOrderInfoDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.RepairOrderRecordDTO;
import com.microtomato.hirun.modules.bss.service.entity.po.ServiceRepairOrder;

import java.util.List;

/**
 * (ServiceRepairOrder)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
public interface IServiceRepairOrderService extends IService<ServiceRepairOrder> {
    void saveRepairRecord(RepairOrderInfoDTO infoDTO);

    /**
     * 查询完整维修数据
     * @param orderId
     * @param customerId
     * @param repairNo
     * @return
     */
    RepairOrderRecordDTO queryRepairRecordInfo(Long orderId,Long customerId,String repairNo);

    void nextStep(RepairOrderInfoDTO infoDTO);

    List<RepairOrderDTO> queryRepairAllRecord(QueryRepairCondDTO condDTO);

}