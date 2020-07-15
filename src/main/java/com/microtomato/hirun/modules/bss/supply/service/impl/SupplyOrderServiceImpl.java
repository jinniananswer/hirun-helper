package com.microtomato.hirun.modules.bss.supply.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyOrder;
import com.microtomato.hirun.modules.bss.supply.mapper.SupplyOrderMapper;
import com.microtomato.hirun.modules.bss.supply.service.ISupplyOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    

}