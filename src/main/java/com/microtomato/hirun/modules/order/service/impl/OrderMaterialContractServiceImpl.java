package com.microtomato.hirun.modules.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.order.mapper.OrderMaterialContractMapper;
import com.microtomato.hirun.modules.order.entity.po.OrderMaterialContract;
import com.microtomato.hirun.modules.order.service.IOrderMaterialContractService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * (OrderMaterialContract)表服务实现类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-21 12:41:46
 */
@Service("orderMaterialContractService")
public class OrderMaterialContractServiceImpl extends ServiceImpl<OrderMaterialContractMapper, OrderMaterialContract> implements IOrderMaterialContractService {

    @Autowired
    private OrderMaterialContractMapper orderMaterialContractMapper;
    

}