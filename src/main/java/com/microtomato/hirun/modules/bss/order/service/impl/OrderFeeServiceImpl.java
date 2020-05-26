package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.config.service.IPayItemCfgService;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.PayComponentDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.PayItemDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayItem;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayNo;
import com.microtomato.hirun.modules.bss.order.exception.OrderException;
import com.microtomato.hirun.modules.bss.order.mapper.OrderFeeMapper;
import com.microtomato.hirun.modules.bss.order.service.*;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单费用表 服务实现类
 * </p>
 *
 * @author sunxin
 * @since 2020-02-05
 */
@Slf4j
@Service
public class OrderFeeServiceImpl extends ServiceImpl<OrderFeeMapper, OrderFee> implements IOrderFeeService {

    @Autowired
    private IOrderWorkerService workerService;

    @Autowired
    private IOrderDomainService orderDomainService;

    @Autowired
    private IOrderPayNoService OrderPayNoService;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IPayItemCfgService payItemCfgService;

    @Autowired
    private IOrderPayItemService orderPayItemService;

    @Autowired
    private IFeeDomainService feeDomainService;


    @Override
    public OrderFee queryOrderCollectFee(Long orderId) {
        OrderFee orderFee = null;
        orderFee = this.baseMapper.selectOne(new QueryWrapper<OrderFee>().lambda()
                .eq(OrderFee::getOrderId, orderId));

        if (orderFee == null) {
            List<OrderWorkerDTO> workerList = workerService.queryByOrderId(orderId);
            if (ArrayUtils.isEmpty(workerList)) {
                return null;
            }

            orderFee = new OrderFee();
            for (OrderWorkerDTO dto : workerList) {
                if (dto.getRoleId().equals(15L)) {
                    //  OrderFee.setCustServiceEmployeeId(dto.getEmployeeId());
                } else if (dto.getRoleId().equals(30L)) {
                    //  OrderFee.setDesignEmployeeId(dto.getEmployeeId());
                }
            }
        }
        return orderFee;
    }

    /**
     * 财务审核
     *
     * @param dto
     */

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void submitAudit(OrderFeeDTO dto) {
        //1是审核通过，2是审核不通过
        String auditStatus = dto.getAuditStatus();
        String orderStatus = dto.getOrderStatus();
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        if (auditStatus.equals("1")) {
            List<OrderPayItem> payItems = orderPayItemService.queryByOrderId(dto.getOrderId());
            if (ArrayUtils.isEmpty(payItems)) {
                //只有设计费不需要判断是否已经收取费用，其他工程款都需要判断
                if ("18".equals(orderStatus) || "25".equals(orderStatus) || "30".equals(orderStatus)) {
                    throw new OrderException(OrderException.OrderExceptionEnum.ORDER_COSTFEE_NOT_FOUND);
                }

            }
            else {
                String firstFalg ="";
                String secondFalg ="";
                String thirdFalg ="";
                for (OrderPayItem payItem : payItems) {
                    Integer payPeriod =payItem.getPeriods();
                    if (1==payPeriod){
                        firstFalg="1";
                    }
                    else if (2==payPeriod){
                        secondFalg="1";
                    }
                    else if (3==payPeriod){
                        thirdFalg="1";
                    }
                }
                //判断是否收取了对应的款项
                if("18".equals(orderStatus)&&"".equals(firstFalg)){
                    throw new OrderException(OrderException.OrderExceptionEnum.ORDER_COSTFEE_NOT_FOUND);
                }
                //判断是否收取了对应的款项
                if("25".equals(orderStatus)&&"".equals(secondFalg)){
                    throw new OrderException(OrderException.OrderExceptionEnum.ORDER_COSTFEE_NOT_FOUND);
                }
                //判断是否收取了对应的款项
                if("30".equals(orderStatus)&&"".equals(thirdFalg)){
                    throw new OrderException(OrderException.OrderExceptionEnum.ORDER_COSTFEE_NOT_FOUND);
                }

            }
            orderDomainService.orderStatusTrans(dto.getOrderId(), OrderConst.OPER_NEXT_STEP);
        } else {
            orderDomainService.orderStatusTrans(dto.getOrderId(), OrderConst.OPER_AUDIT_NO);
        }

        //如果需要流转到指定人，才需要处理worker记录 首期款需要选择工程文员
        //判断当前状态，处理worker表

        if (orderStatus.equals("18") && auditStatus.equals("1")) {
            workerService.updateOrderWorker(dto.getOrderId(), 32L, dto.getEngineeringClerk());
        }
        //处理orderFee的审核人与审核备注
        String type = "";
        int periods = 0;
        if (orderStatus.equals("8")) {
            type = "1";
        } else if (orderStatus.equals("18") || orderStatus.equals("25") || orderStatus.equals("30")) {
            type = "2";
            if (orderStatus.equals("18")) {
                periods = 1;
            } else if (orderStatus.equals("25")) {
                periods = 2;
            } else {
                periods = 3;
            }
        }
        LocalDateTime auditTime = RequestTimeHolder.getRequestTime();
        this.updateByOrderId(dto.getOrderId(), type, periods, auditStatus, employeeId, dto.getAuditRemark(), auditTime);
    }

    /**
     * 财务复核
     *
     * @param orderPayNo
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void costReview(OrderPayNo orderPayNo) {
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        OrderPayNoService.update(new UpdateWrapper<OrderPayNo>().lambda().eq(OrderPayNo::getPayNo, orderPayNo.getPayNo()).eq(OrderPayNo::getOrderId, orderPayNo.getOrderId()).gt(OrderPayNo::getEndDate, LocalDateTime.now()).set(OrderPayNo::getAuditStatus, orderPayNo.getAuditStatus()).set(OrderPayNo::getAuditEmployeeId, employeeId).set(OrderPayNo::getUpdateTime, LocalDateTime.now()).set(OrderPayNo::getRemark, orderPayNo.getRemark()));
    }

    /**
     * 初始化费用信息
     *
     * @return
     */
    @Override
    public PayComponentDTO initCostAudit(Long orderId, String orderStatus) {
        PayComponentDTO componentData = new PayComponentDTO();
        if (orderId != null) {
            List<OrderPayItem> payItems = orderPayItemService.queryByOrderId(orderId);
            if (ArrayUtils.isNotEmpty(payItems)) {
                List<PayItemDTO> payItemDTOs = new ArrayList<>();
                for (OrderPayItem payItem : payItems) {
                    PayItemDTO payItemDTO = new PayItemDTO();
                    payItemDTO.setPayItemId("pay_" + payItem.getPayItemId());
                    payItemDTO.setMoney(payItem.getFee().doubleValue() / 100);

                    String payItemName = payItemCfgService.getPath(payItem.getPayItemId());
                    Integer payPeriod = payItem.getPeriods();
                    if (payPeriod != null) {
                        payItemDTO.setPeriod(payPeriod);
                        String payPeriodName = staticDataService.getCodeName("PAY_PERIODS", payPeriod + "");
                        payItemDTO.setPeriodName(payPeriodName);
                        payItemName += '-' + payPeriodName;
                    }
                    payItemDTO.setPayItemName(payItemName);

                    payItemDTOs.add(payItemDTO);
                }
                componentData.setPayItems(payItemDTOs);
            }

        }
        return componentData;
    }

    /**
     * 根据订单ID查询订单费用
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderFee> queryByOrderId(Long orderId) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.list(new QueryWrapper<OrderFee>().lambda().eq(OrderFee::getOrderId, orderId).gt(OrderFee::getEndDate, now));
    }

    /**
     * 根据订单ID、类型、期数查询订单费用
     *
     * @param orderId
     * @param type
     * @param period
     * @return
     */
    @Override
    public OrderFee getByOrderIdTypePeriod(Long orderId, String type, Integer period) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.getOne(new QueryWrapper<OrderFee>().lambda()
                .eq(OrderFee::getOrderId, orderId)
                .eq(OrderFee::getType, type)
                .gt(OrderFee::getEndDate, now)
                .eq(period != null, OrderFee::getPeriods, period));
    }

    /**
     * 根据订单ID与收费类型，期数更新orderfee状态
     *
     * @param orderId
     * @return
     */
    @Override
    public void updateByOrderId(Long orderId, String type, Integer periods, String auditStatus, Long employeeId, String auditRemark, LocalDateTime auditTime) {
        if (type.equals("1")) {
            this.update(new UpdateWrapper<OrderFee>().lambda().eq(OrderFee::getOrderId, orderId).eq(OrderFee::getType, type).gt(OrderFee::getEndDate, LocalDateTime.now()).set(OrderFee::getAuditStatus, auditStatus).set(OrderFee::getAuditEmployeeId, employeeId).set(OrderFee::getAuditComment, auditRemark).set(OrderFee::getAuditTime, auditTime));
        } else {
            this.update(new UpdateWrapper<OrderFee>().lambda().eq(OrderFee::getOrderId, orderId).eq(OrderFee::getType, type).eq(OrderFee::getPeriods, periods).gt(OrderFee::getEndDate, LocalDateTime.now()).set(OrderFee::getAuditStatus, auditStatus).set(OrderFee::getAuditEmployeeId, employeeId).set(OrderFee::getAuditComment, auditRemark).set(OrderFee::getAuditTime, auditTime));
        }

    }
}
