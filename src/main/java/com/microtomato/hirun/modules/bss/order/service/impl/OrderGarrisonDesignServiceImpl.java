package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderGarrisonDesign;
import com.microtomato.hirun.modules.bss.order.mapper.OrderGarrisonDesignMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderGarrisonDesignService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-03-04
 */
@Slf4j
@Service
public class OrderGarrisonDesignServiceImpl extends ServiceImpl<OrderGarrisonDesignMapper, OrderGarrisonDesign> implements IOrderGarrisonDesignService {

    @Autowired
    private IOrderDomainService orderDomainService;

    @Override
    public void saveGarrisonDesignInfo(OrderGarrisonDesign orderGarrisonDesign) {
        if(orderGarrisonDesign.getId()==null){
            this.baseMapper.insert(orderGarrisonDesign);
        }else{
            this.baseMapper.updateById(orderGarrisonDesign);
        }
    }

    @Override
    public void submitBudget(OrderGarrisonDesign orderGarrisonDesign) {
        this.saveGarrisonDesignInfo(orderGarrisonDesign);
        orderDomainService.orderStatusTrans(orderGarrisonDesign.getOrderId(),"NEXT");
    }
}
