package com.microtomato.hirun.modules.bss.supply.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.sequence.impl.PayNoCycleSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.CollectionItemCfg;
import com.microtomato.hirun.modules.bss.order.entity.dto.NonCollectFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.NormalPayItemDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.NormalPayItem;
import com.microtomato.hirun.modules.bss.order.exception.OrderException;
import com.microtomato.hirun.modules.bss.order.service.INormalPayItemService;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyMaterialDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyOrderDTO;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyOrder;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyOrderDetail;
import com.microtomato.hirun.modules.bss.supply.mapper.SupplyOrderMapper;
import com.microtomato.hirun.modules.bss.supply.service.ISupplyOrderDetailService;
import com.microtomato.hirun.modules.bss.supply.service.ISupplyOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 供应订单表(SupplyOrder)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-15 11:26:08
 */
@Service
@Slf4j
public class SupplyOrderServiceImpl extends ServiceImpl<SupplyOrderMapper, SupplyOrder> implements ISupplyOrderService {

    @Autowired
    private SupplyOrderMapper supplyOrderMapper;

    @Autowired
    private ISupplyOrderService supplyOrderService;

    @Autowired
    private ISupplyOrderDetailService supplyOrderDetailService;

    @Autowired
    private IDualService dualService;


    /**
     * 材料下单
     *
     * @param supplyOrderInfo
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public   void materialOrderDeal(SupplyOrderDTO supplyOrderInfo){
        List<SupplyMaterialDTO> supplyMaterialOrders = supplyOrderInfo.getSupplyMaterial();
        if (ArrayUtils.isNotEmpty(supplyMaterialOrders)) {
            for (SupplyMaterialDTO supplyMaterialOrder : supplyMaterialOrders) {
                SupplyOrder supplyOrder = new SupplyOrder();
                SupplyOrderDetail supplyOrderDetail = new SupplyOrderDetail();
                Long supplyId =supplyOrderInfo.getOrderId();//需要新增序列，暂时用orderid
                //拼supplyOrder表数据
                supplyOrder.setOrderId(supplyOrderInfo.getOrderId());
                supplyOrder.setCreateTime(RequestTimeHolder.getRequestTime());
                supplyOrder.setCreateUserId( WebContextUtils.getUserContext().getEmployeeId());
                supplyOrder.setSupplyId(supplyId);
                supplyOrder.setSupplyOrderType(supplyOrderInfo.getSupplyOrderType());
                supplyOrder.setSupplyStatus("0");
                supplyOrderService.save(supplyOrder);
                //拼SupplyOrderDetail表数据
                supplyOrderDetail.setMaterialId(supplyMaterialOrder.getId());
                supplyOrderDetail.setNum(supplyMaterialOrder.getNum());
                supplyOrderDetail.setSupplyId(supplyId);
                supplyOrderDetail.setFee(supplyMaterialOrder.getCostPrice());
                supplyOrderDetail.setCreateTime(RequestTimeHolder.getRequestTime());
                supplyOrderDetail.setCreateUserId( WebContextUtils.getUserContext().getEmployeeId());
                supplyOrderDetailService.save(supplyOrderDetail);
            }
        }

    }

}