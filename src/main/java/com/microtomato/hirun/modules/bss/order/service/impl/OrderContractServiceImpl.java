package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.exception.cases.NotFoundException;
import com.microtomato.hirun.framework.mybatis.sequence.impl.PayNoCycleSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.bss.config.entity.consts.FeeConst;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.DecorateContractDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.FeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderContract;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFeeItem;
import com.microtomato.hirun.modules.bss.order.mapper.OrderContractMapper;
import com.microtomato.hirun.modules.bss.order.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private IOrderFileService orderFileService;

    @Autowired
    private IOrderFeeItemService orderFeeItemService;

    @Autowired
    private IOrderBaseService orderBaseService;

    @Autowired
    private IDualService dualService;

    @Override
    public DecorateContractDTO getDecorateContractInfo(Long orderId) {
        DecorateContractDTO decorateContractDTO = new DecorateContractDTO();
        decorateContractDTO.setOrderId(orderId);

        OrderContract orderContract = this.baseMapper.selectOne(new QueryWrapper<OrderContract>().lambda()
                .eq(OrderContract::getOrderId, orderId).eq(OrderContract::getContractType, "1"));
        if(orderContract == null) {
            return decorateContractDTO;
        }
        BeanUtils.copyProperties(orderContract, decorateContractDTO);

        List<OrderFeeItem> secondOrderFeeItemLiist = orderFeeItemService.queryByOrderIdTypePeriod(orderId, FeeConst.FEE_TYPE_PROJECT, FeeConst.FEE_PERIOD_FIRST);
        for(OrderFeeItem orderFeeItem : secondOrderFeeItemLiist) {
            if(orderFeeItem.getFeeItemId() == 5L) {
                decorateContractDTO.setBaseDecorationFee(orderFeeItem.getFee().intValue());
            } else if(orderFeeItem.getFeeItemId() == 7L) {
                decorateContractDTO.setDoorFee(orderFeeItem.getFee().intValue());
            } else if(orderFeeItem.getFeeItemId() == 8L) {
                decorateContractDTO.setFurnitureFee(orderFeeItem.getFee().intValue());
            } else if(orderFeeItem.getFeeItemId() == 14L) {
                decorateContractDTO.setReturnDesignFee(orderFeeItem.getFee().intValue());
            } else if(orderFeeItem.getFeeItemId() == 15L) {
                decorateContractDTO.setTaxFee(orderFeeItem.getFee().intValue());
            }
        }

        //财务审核人员
        List<OrderWorkerDTO> orderWorkersDTO = orderWorkerService.queryByOrderId(orderId);
        if(orderWorkersDTO != null) {
            for(OrderWorkerDTO orderWorkerDTO : orderWorkersDTO) {
                if(orderWorkerDTO.getRoleId() == 35L) {
                    decorateContractDTO.setFinanceEmployeeId(orderWorkerDTO.getEmployeeId());
                    decorateContractDTO.setFinanceEmployeeName(orderWorkerDTO.getName());
                }
            }
        }

        return decorateContractDTO;
    }

    @Override
    public void submitDecorateContract(DecorateContractDTO decorateContractDTO) {
        //校验规则
        if(orderFileService.getOrderFile(decorateContractDTO.getOrderId(), 16) == null) {
            throw new NotFoundException("请先上传合同附件", -1);
        }

        OrderBase orderBase = this.orderBaseService.queryByOrderId(decorateContractDTO.getOrderId());
        //保存基本信息到
        OrderContract orderContract = this.baseMapper.selectById(decorateContractDTO.getId());
        if(orderContract == null) {
            orderContract = new OrderContract();
            orderContract.setContractType("1");
            orderContract.setContractNo(this.generateContractNo(orderBase.getShopId()));
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

    /**
     * 根据订单ID和合同类型查询合同表
     * @param orderId
     * @param type
     * @return
     */
    @Override
    public OrderContract getByOrderIdType(Long orderId, String type) {
        OrderContract orderContract = this.getOne(Wrappers.<OrderContract>lambdaQuery()
                .eq(OrderContract::getOrderId, orderId)
                .eq(OrderContract::getContractType, type), false);

        return orderContract;
    }

    /**
     * 根据订单ID查询订单合同信息
     * @param orderId
     * @return
     */
    @Override
    public List<OrderContract> queryByOrderId(Long orderId) {
        List<OrderContract> orderContracts = this.list(Wrappers.<OrderContract>lambdaQuery()
                .eq(OrderContract::getOrderId, orderId));

        return orderContracts;
    }

    /**
     * 获取合同编号
     * @param shopId
     * @return
     */
    private String generateContractNo(Long shopId) {
        if (shopId == null) {
            return null;
        }
        Long value = this.dualService.nextval(PayNoCycleSeq.class);
        String strNextval = StringUtils.leftPad(String.valueOf(value), 6, '0');
        String timestamp = TimeUtils.now("yyyy");

        String prefix = "0";
        if (shopId.equals(44L)) {
            prefix = "1";
        } else if (shopId.equals(45L)) {
            prefix = "2";
        } else if (shopId.equals(46L)) {
            prefix = "3";
        } else if (shopId.equals(47L)) {
            prefix = "7";
        } else if (shopId.equals(48L)) {
            prefix = "9";
        } else if (shopId.equals(118L)) {
            prefix = "6";
        } else if (shopId.equals(40L)) {
            prefix = "5";
        }

        return prefix + "." + timestamp + "." + strNextval;
    }
}
