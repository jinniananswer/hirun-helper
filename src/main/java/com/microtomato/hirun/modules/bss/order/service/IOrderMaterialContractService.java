package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderMaterialContractDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.QueryMaterialCondDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderMaterialContract;

import java.util.List;

/**
 * (OrderMaterialContract)表服务接口
 *
 * @author
 * @version 1.0.0
 * @date 2020-09-24 00:05:42
 */
public interface IOrderMaterialContractService extends IService<OrderMaterialContract> {
    /**
     * 查询主材合同
     * @param condDTO
     * @return
     */
    List<OrderMaterialContractDTO> queryMaterialContracts(QueryMaterialCondDTO condDTO);
}