package com.microtomato.hirun.modules.bss.supply.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.ProjectFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.QueryProjectFeeDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.QuerySupplyOrderDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyMaterialDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyOrderDTO;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyOrder;
import com.microtomato.hirun.modules.finance.entity.dto.FinanceVoucherDTO;

import java.util.List;

/**
 * 供应订单表(SupplyOrder)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-15 11:26:08
 */
public interface ISupplyOrderService extends IService<SupplyOrder> {

    void materialOrderDeal(SupplyOrderDTO supplyOrderInfo);

    IPage<SupplyOrderDTO> querySupplyInfo(QuerySupplyOrderDTO condition);

    List<SupplyMaterialDTO> querySupplyDetailInfo(QuerySupplyOrderDTO condition);

    void auditSupplyDetail(List<SupplyOrderDTO> supplyOrderDTOS);

}