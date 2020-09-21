package com.microtomato.hirun.modules.bss.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.service.entity.dto.RepairOrderInfoDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.RepairOrderRecordDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.ServicePendingTaskDTO;
import com.microtomato.hirun.modules.bss.service.entity.po.ServiceRepairOrder;

import java.util.List;

/**
 * (ServiceRepairOrder)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
public interface IServiceCenterDomainService extends IService<ServiceRepairOrder> {

    /**
     * 查询服务中心代办
     */
    List<ServicePendingTaskDTO> queryServicePendingTask();
}