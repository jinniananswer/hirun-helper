package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.OrderStatusCfg;
import com.microtomato.hirun.modules.bss.config.service.IOrderStatusCfgService;
import com.microtomato.hirun.modules.bss.config.service.IPayItemCfgService;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.PayComponentDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.PayItemDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.DesignFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.QueryDesignFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayItem;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayNo;
import com.microtomato.hirun.modules.bss.order.exception.OrderException;
import com.microtomato.hirun.modules.bss.order.mapper.OrderFeeMapper;
import com.microtomato.hirun.modules.bss.order.service.*;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    private OrderFeeMapper orderFeeMapper;

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

    @Autowired
    private IOrderWorkerService orderWorkerService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IOrderStatusCfgService orderStatusCfgService;

    @Autowired
    private IOrgService orgService;

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
     *orderStatus:8(审核设计费)，18（审核首期款），25（二期款审核），30（尾款审核）
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
            //只有设计费不需要判断是否已经收取费用，其他工程款都需要判断
            if ("18".equals(orderStatus) || "25".equals(orderStatus) || "30".equals(orderStatus)) {
                List<OrderPayItem> payItems = orderPayItemService.queryByOrderId(dto.getOrderId());
                if (ArrayUtils.isEmpty(payItems)) {
                    throw new OrderException(OrderException.OrderExceptionEnum.ORDER_COSTFEE_NOT_FOUND);
                } else {
                    String firstFalg = "";
                    String secondFalg = "";
                    String thirdFalg = "";

                    for (OrderPayItem payItem : payItems) {
                        Integer payPeriod = payItem.getPeriods();
                        if (1 == payPeriod) {
                            firstFalg = "1";
                        } else if (2 == payPeriod) {
                            secondFalg = "1";
                        } else if (3 == payPeriod) {
                            thirdFalg = "1";
                        }
                    }

                    //判断首期是否收取了对应的款项
                    if ("18".equals(orderStatus) && "".equals(firstFalg)) {
                        throw new OrderException(OrderException.OrderExceptionEnum.ORDER_COSTFEE_NOT_FOUND);
                    }
                    //判断2期是否收取了对应的款项
                    if ("25".equals(orderStatus) && "".equals(secondFalg)) {
                        throw new OrderException(OrderException.OrderExceptionEnum.ORDER_COSTFEE_NOT_FOUND);
                    }
                    //判断尾款是否收取了对应的款项
                    if ("30".equals(orderStatus) && "".equals(thirdFalg)) {
                        throw new OrderException(OrderException.OrderExceptionEnum.ORDER_COSTFEE_NOT_FOUND);
                    }

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
     * 根据传入的多个订单ID查询订单费用
     * @param orderIds
     * @return
     */
    @Override
    public List<OrderFee> queryByOrderIds(List<Long> orderIds) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.list(Wrappers.<OrderFee>lambdaQuery().in(OrderFee::getOrderId, orderIds).gt(OrderFee::getEndDate, now));
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

    /**
     * 设计费查询
     * @param condition
     * @return
     */
    @Override
    public IPage<DesignFeeDTO> queryDesignFees(QueryDesignFeeDTO condition) {
        IPage<QueryDesignFeeDTO> request = new Page<>(condition.getPage(), condition.getLimit());
        QueryWrapper<QueryDesignFeeDTO> wrapper = new QueryWrapper<>();
        wrapper.apply(" b.cust_id = a.cust_id ");
        wrapper.like(StringUtils.isNotBlank(condition.getCustName()), "b.cust_name", condition.getCustName());
        wrapper.like(StringUtils.isNotBlank(condition.getMobileNo()), "b.mobile_no", condition.getMobileNo());

        String shopIds = condition.getShopIds();
        if (StringUtils.isNotBlank(shopIds)) {
            List<String> shopIdArray = Arrays.asList(StringUtils.split(shopIds, ","));
            wrapper.in("a.shop_id", shopIdArray);
        }

        String feeTime = condition.getFeeTime();
        if (StringUtils.isNotBlank(feeTime)) {
            String[] feeTimeArray = StringUtils.split(",");
            wrapper.ge("d.first_pay_time", feeTimeArray[0]);
            wrapper.le("d.first_pay_time", feeTimeArray[1]);
        }

        wrapper.eq(condition.getHousesId() != null, "a.houses_id", condition.getHousesId());

        IPage<DesignFeeDTO> pageDesigns = this.orderFeeMapper.queryDesignFee(request, wrapper);

        List<DesignFeeDTO> designFees = pageDesigns.getRecords();

        //设置订单参与人
        if (ArrayUtils.isNotEmpty(designFees)) {
            List<Long> orderIds = this.distinctOrderId(designFees);
            if (ArrayUtils.isNotEmpty(orderIds)) {
                List<Long> roleIds = new ArrayList<Long>() {{
                    this.add(3L);
                    this.add(15L);
                    this.add(30L);
                    this.add(46L);
                    this.add(47L);
                    this.add(555L);
                }};
                List<OrderWorkerDTO> workers = this.orderWorkerService.queryByOrderIdsRoleIds(orderIds, roleIds);

                if (ArrayUtils.isNotEmpty(workers)) {
                    designFees.forEach(designFee -> {
                        OrderWorkerDTO counselor = this.findWorker(designFee.getOrderId(), 3L, workers);
                        if (counselor != null) {
                            designFee.setCounselorName(counselor.getName());
                        }

                        OrderWorkerDTO agent = this.findWorker(designFee.getOrderId(), 15L, workers);
                        if (agent != null) {
                            designFee.setAgentName(agent.getName());
                        }

                        OrderWorkerDTO designer = this.findWorker(designFee.getOrderId(), 30L, workers);
                        if (designer != null) {
                            designFee.setDesignerName(designer.getName());
                        }

                        OrderWorkerDTO material = this.findWorker(designFee.getOrderId(), 46L, workers);
                        if (material != null) {
                            designFee.setMaterialName(material.getName());
                        }

                        OrderWorkerDTO cabinet = this.findWorker(designFee.getOrderId(), 47L, workers);
                        if (cabinet != null) {
                            designFee.setCabinetName(cabinet.getName());
                        }

                        OrderWorkerDTO report = this.findWorker(designFee.getOrderId(), 555L, workers);
                        if (report != null) {
                            designFee.setReportName(report.getName());
                        }
                    });
                }

                List<Long> payItemIds = new ArrayList<Long>(){{
                    this.add(2L);
                    this.add(3L);
                    this.add(4L);
                    this.add(5L);
                    this.add(6L);
                }};
                List<OrderPayItem> orderPayItems = this.orderPayItemService.queryByOrderIdsPayItems(orderIds, payItemIds);
                if (ArrayUtils.isNotEmpty(orderPayItems)) {
                    designFees.forEach(designFee -> {
                        this.fillByDesignPayItem(designFee, orderPayItems);
                    });
                }
            }
        }

        return pageDesigns;
    }

    /**
     * 汇聚orderId列表，且不重复
     * @param designFees
     * @return
     */
    private List<Long> distinctOrderId(List<DesignFeeDTO> designFees) {
        if (ArrayUtils.isEmpty(designFees)) {
            return null;
        }

        List<Long> orderIds = new ArrayList<>();
        designFees.forEach(designFee -> {
            Long orderId = designFee.getOrderId();
            if (!orderIds.contains(orderId)) {
                orderIds.add(orderId);
            }
        });

        return orderIds;
    }

    /**
     * 根据订单ID与角色查找参与人
     * @param orderId
     * @param roleId
     * @param workers
     * @return
     */
    private OrderWorkerDTO findWorker(Long orderId, Long roleId, List<OrderWorkerDTO> workers) {
        for (OrderWorkerDTO worker : workers) {
            if (orderId.equals(worker.getOrderId()) && roleId.equals(worker.getRoleId())) {
                return worker;
            }
        }

        return null;
    }

    /**
     * 填充费用相关字段
     * @param designFee
     * @param orderPayItems
     */
    private void fillByDesignPayItem(DesignFeeDTO designFee, List<OrderPayItem> orderPayItems) {
        if (ArrayUtils.isEmpty(orderPayItems)) {
            return;
        }
        Long orderId = designFee.getOrderId();
        List<OrderPayItem> designPayItems = new ArrayList<>();
        orderPayItems.forEach(orderPayItem -> {
            if (orderId.equals(orderPayItem.getOrderId())) {
                designPayItems.add(orderPayItem);
            }
        });

        if (ArrayUtils.isEmpty(designPayItems)) {
            return;
        }

        Long total = 0L;
        Long totalDeposit = 0L;

        LocalDateTime payTime = null;

        for (OrderPayItem designPayItem : designPayItems) {
            total += designPayItem.getFee();

            if (payTime == null) {
                payTime = designPayItem.getStartDate();
            } else if (TimeUtils.compareTwoTime(designPayItem.getStartDate(), payTime) < 0) {
                payTime = designPayItem.getStartDate();
            }
            if (designPayItem.getPayItemId().equals(3L) && StringUtils.isBlank(designFee.getDepositFinanceName())) {
                Long userId = designPayItem.getCreateUserId();
                Employee employee = this.employeeService.queryByUserId(userId);
                designFee.setDepositFinanceName(employee.getName());
                totalDeposit += designPayItem.getFee();
            } else if (designPayItem.getPayItemId().equals(4L) && StringUtils.isBlank(designFee.getDesignFeeFinanceName())) {
                Long userId = designPayItem.getCreateUserId();
                Employee employee = this.employeeService.queryByUserId(userId);
                designFee.setDesignFeeFinanceName(employee.getName());
            }
        }
        designFee.setDesignFee(total);
        designFee.setFirstPayTime(payTime);
        designFee.setDepositFee(totalDeposit);
        designFee.setFeeTime(payTime);

        String orderStatus = designFee.getStatus();
        if (StringUtils.isNotBlank(orderStatus)) {
            OrderStatusCfg orderStatusCfg = this.orderStatusCfgService.getCfgByTypeStatus(designFee.getType(), orderStatus);
            designFee.setOrderStatusName(orderStatusCfg.getStatusName());
        }

        Long shopId = designFee.getShopId();
        if (shopId != null) {
            Org shop = this.orgService.getById(shopId);
            if (shop != null) {
                designFee.setShopName(shop.getName());
            }
        }
    }
}
