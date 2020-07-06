package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.dto.DecorateContractDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderContract;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单合同 服务类
 * </p>
 *
 * @author anwx
 * @since 2020-02-23
 */
public interface IOrderContractService extends IService<OrderContract> {

    public DecorateContractDTO getDecorateContractInfo(Long orderId);

    public void submitDecorateContract(DecorateContractDTO decorateContractDTO);
}
