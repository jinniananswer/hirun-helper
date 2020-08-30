package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderInspectDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderInspect;
import com.microtomato.hirun.modules.bss.order.mapper.OrderInspectMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderInspectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * (OrderInspect)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-08-11 18:07:44
 */
@Service
@Slf4j
public class OrderInspectServiceImpl extends ServiceImpl<OrderInspectMapper, OrderInspect> implements IOrderInspectService {

    @Autowired
    private OrderInspectMapper orderInspectMapper;

    @Autowired
    private OrderDomainServiceImpl orderDomainService;


    @Override
    public void save(OrderInspectDTO dto) {

        OrderInspect orderInspect=new OrderInspect();
        BeanUtils.copyProperties(dto,orderInspect);

        if(dto.getId()==null){
            this.baseMapper.insert(orderInspect);
        }else{
            this.baseMapper.updateById(orderInspect);
        }
    }

    @Override
    public OrderInspectDTO queryOrderInspect(Long orderId) {
        OrderInspect orderInspect=this.orderInspectMapper.selectOne(new QueryWrapper<OrderInspect>().lambda()
                .eq(OrderInspect::getOrderId,orderId));

        OrderInspectDTO dto=new OrderInspectDTO();
        if(orderInspect !=null){
            BeanUtils.copyProperties(orderInspect,dto);
        }
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void nextStep(OrderInspectDTO dto) {
        this.save(dto);
        orderDomainService.orderStatusTrans(dto.getOrderId(), OrderConst.OPER_NEXT_STEP);
    }
}