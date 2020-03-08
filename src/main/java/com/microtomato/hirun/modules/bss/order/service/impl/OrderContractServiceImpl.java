package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.modules.bss.config.entity.consts.FeeConst;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.DecorateContractDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.FeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderContract;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import com.microtomato.hirun.modules.bss.order.mapper.OrderContractMapper;
import com.microtomato.hirun.modules.bss.order.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
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

    @Autowired
    private IOrderWorkerService orderWorkerService;

    @Autowired
    private IFeeDomainService feeDomainService;

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
        List<FeeDTO> fees = new ArrayList<>();
        //基础装修费
        FeeDTO baseFee = new FeeDTO();
        baseFee.setFeeItemId(5L);
        baseFee.setMoney(decorateContractDTO.getBaseDecorationFee().doubleValue()/100);
        fees.add(baseFee);
        //门总金额
        FeeDTO doorFee = new FeeDTO();
        doorFee.setFeeItemId(7L);
        doorFee.setMoney(decorateContractDTO.getDoorFee().doubleValue()/100);
        fees.add(doorFee);
        //家具总金额
        FeeDTO furnitureFee = new FeeDTO();
        furnitureFee.setFeeItemId(8L);
        furnitureFee.setMoney(decorateContractDTO.getFurnitureFee().doubleValue()/100);
        fees.add(furnitureFee);
        //返设计金额
        FeeDTO returnDesignFee = new FeeDTO();
        returnDesignFee.setFeeItemId(14L);
        returnDesignFee.setMoney(decorateContractDTO.getReturnDesignFee().doubleValue()/100);
        fees.add(returnDesignFee);
        //税金额
        FeeDTO taxFee = new FeeDTO();
        taxFee.setFeeItemId(15L);
        taxFee.setMoney(decorateContractDTO.getTaxFee().doubleValue()/100);
        fees.add(taxFee);
        feeDomainService.createOrderFee(decorateContractDTO.getOrderId(), FeeConst.FEE_TYPE_PROJECT, FeeConst.FEE_PERIOD_FIRST, fees);

        orderDomainService.orderStatusTrans(decorateContractDTO.getOrderId(), OrderConst.OPER_NEXT_STEP);

        orderWorkerService.updateOrderWorker(decorateContractDTO.getOrderId(), 35L, decorateContractDTO.getFinanceEmployeeId());
    }

}
