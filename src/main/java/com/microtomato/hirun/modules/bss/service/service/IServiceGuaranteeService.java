package com.microtomato.hirun.modules.bss.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.service.entity.dto.GuaranteeDTO;
import com.microtomato.hirun.modules.bss.service.entity.po.ServiceGuarantee;

/**
 * (ServiceGuarantee)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
public interface IServiceGuaranteeService extends IService<ServiceGuarantee> {
    /**
     * 查询保修卡信息
     * @param orderId
     * @return
     */
    GuaranteeDTO queryCustomerGuaranteeInfo(Long orderId);

    /**
     * 保存保修卡信息
     * @param dto
     */
    void saveGuaranteeInfo(GuaranteeDTO dto);
}