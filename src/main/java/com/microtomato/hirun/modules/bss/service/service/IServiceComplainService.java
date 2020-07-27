package com.microtomato.hirun.modules.bss.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.service.entity.dto.*;
import com.microtomato.hirun.modules.bss.service.entity.po.ServiceComplain;

import java.util.List;

/**
 * (ServiceComplain)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
public interface IServiceComplainService extends IService<ServiceComplain> {
    /**
     * 保存投诉信息
     * @param infoDTO
     */
    void saveComplainOrder(ComplainOrderInfoDTO infoDTO);

    /**
     * 查询投诉代办
     * @param employeeId
     * @param status
     * @return
     */
    List<ServicePendingOrderDTO> queryComplainPendingOrders(Long employeeId, String status);

    /**
     * 查询完整投诉数据
     * @param orderId
     * @param customerId
     * @param repairNo
     * @return
     */
    ComplainOrderRecordDTO queryComplainRecordInfo(Long orderId, Long customerId, String repairNo);
}