package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustConsultDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.CustBase;
import com.microtomato.hirun.modules.bss.customer.service.ICustBaseService;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderConsult;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWorker;
import com.microtomato.hirun.modules.bss.order.mapper.OrderConsultMapper;
import com.microtomato.hirun.modules.bss.order.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-15
 */
@Slf4j
@Service
public class OrderConsultServiceImpl extends ServiceImpl<OrderConsultMapper, OrderConsult> implements IOrderConsultService {

    @Autowired
    private IOrderWorkerService workerService;

    @Autowired
    private IOrderDomainService orderDomainService;

    @Autowired
    private IOrderBaseService orderBaseService;

    @Autowired
    private ICustBaseService custBaseService;

    @Autowired
    private IOrderWorkerActionService workerActionService;

    @Override
    public OrderConsult queryOrderConsult(Long orderId) {
        OrderConsult orderConsult = null;
        orderConsult = this.baseMapper.selectOne(new QueryWrapper<OrderConsult>().lambda()
                .eq(OrderConsult::getOrderId, orderId));

        if (orderConsult == null) {
            List<OrderWorkerDTO> workerList = workerService.queryByOrderId(orderId);
            if (ArrayUtils.isEmpty(workerList)) {
                return null;
            }

            orderConsult = new OrderConsult();
            for (OrderWorkerDTO dto : workerList) {
                if (dto.getRoleId().equals(15L)) {
                    orderConsult.setCustServiceEmployeeId(dto.getEmployeeId());
                } else if (dto.getRoleId().equals(30L)) {
                    orderConsult.setDesignEmployeeId(dto.getEmployeeId());
                }
            }
        }
        return orderConsult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveCustomerConsultInfo(CustConsultDTO dto) {

        OrderConsult orderConsult = new OrderConsult();
        BeanUtils.copyProperties(dto, orderConsult);

        if (dto.getId() == null) {
            this.baseMapper.insert(orderConsult);
        } else {
            this.baseMapper.updateById(orderConsult);
        }
        workerService.updateOrderWorker(dto.getOrderId(), 15L, dto.getCustServiceEmployeeId());
        workerService.updateOrderWorker(dto.getOrderId(), 45L, dto.getDesignCupboardEmployeeId());
        workerService.updateOrderWorker(dto.getOrderId(), 46L, dto.getMainMaterialKeeperEmployeeId());
        workerService.updateOrderWorker(dto.getOrderId(), 47L, dto.getCupboardKeeperEmployeeId());
        if (dto.getDesignEmployeeId() != null) {
            workerService.updateOrderWorker(dto.getOrderId(), 30L, dto.getDesignEmployeeId());
        }
        //更新客户表中的咨询时间
        OrderBase orderBase = orderBaseService.getById(dto.getOrderId());
        custBaseService.update(new UpdateWrapper<CustBase>().lambda().eq(CustBase::getCustId, orderBase.getCustId()).set(CustBase::getConsultTime, dto.getConsultTime()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void submitMeasure(CustConsultDTO dto) {
        this.saveCustomerConsultInfo(dto);
        orderDomainService.orderStatusTrans(dto.getOrderId(), OrderConst.OPER_NEXT_STEP);
        //插入workerAction,用于工资计算
        List<OrderWorker> workers=workerService.queryValidByOrderId(dto.getOrderId());
        if(ArrayUtils.isNotEmpty(workers)){
            for(OrderWorker orderWorker:workers){
                if(orderWorker.getRoleId().equals(15L)){
                    workerActionService.createOrderWorkerAction(dto.getOrderId(),orderWorker.getEmployeeId(),orderWorker.getId(),"2","consult");
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void submitSneak(CustConsultDTO dto) {
        this.saveCustomerConsultInfo(dto);
        orderDomainService.orderStatusTrans(dto.getOrderId(), OrderConst.OPER_RUN);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void transOrder(CustConsultDTO dto) {
        this.saveCustomerConsultInfo(dto);
        orderDomainService.orderStatusTrans(dto.getOrderId(), OrderConst.OPER_NEXT_STEP);
    }

    @Override
    public CustConsultDTO queryOrderConsultForTrans(Long orderId) {
        OrderConsult orderConsult=this.queryOrderConsult(orderId);
        CustConsultDTO dto=new CustConsultDTO();
        BeanUtils.copyProperties(orderConsult,dto);
        OrderBase orderBase=this.orderBaseService.getById(orderId);
        dto.setCustId(orderBase.getCustId());
        return dto;
    }
}
