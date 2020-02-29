package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.DecorateContractDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderContract;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import com.microtomato.hirun.modules.bss.order.mapper.OrderContractMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderContractService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.order.service.IOrderDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderFeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 订单合同 服务实现类
 * </p>
 *
 * @author anwx
 * @since 2020-02-23
 */
@Slf4j
@Service
public class OrderContractServiceImpl extends ServiceImpl<OrderContractMapper, OrderContract> implements IOrderContractService {

    @Autowired
    private IOrderFeeService orderFeeService;

    @Autowired
    private IOrderDomainService orderDomainService;

    public DecorateContractDTO getDecorateContractInfo(Long orderId) {
        DecorateContractDTO decorateContractDTO = new DecorateContractDTO();
        decorateContractDTO.setOrderId(orderId);

        OrderContract orderContract = this.baseMapper.selectOne(new QueryWrapper<OrderContract>().lambda()
                .eq(OrderContract::getOrderId, orderId).eq(OrderContract::getContractType, "1"));
        if(orderContract == null) {
            return decorateContractDTO;
        }
        BeanUtils.copyProperties(orderContract, decorateContractDTO);

        List<OrderFee> orderFeeList = orderFeeService.list(new QueryWrapper<OrderFee>().lambda()
            .eq(OrderFee::getOrderId, orderId));

        //填充费用到DTO
        for(OrderFee orderFee : orderFeeList) {

        }

        return decorateContractDTO;
    }

    public void submitDecorateContract(DecorateContractDTO decorateContractDTO) {
        //保存基本信息到
        OrderContract orderContract = this.baseMapper.selectById(decorateContractDTO.getId());
        if(orderContract == null) {
            orderContract = new OrderContract();
            orderContract.setContractType("1");
        }
        BeanUtils.copyProperties(decorateContractDTO, orderContract);
        this.saveOrUpdate(orderContract);
        //保存费用信息到各个表

        orderDomainService.orderStatusTrans(decorateContractDTO.getOrderId(), OrderConst.OPER_NEXT_STEP);
    }

}
