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
import com.microtomato.hirun.modules.bss.order.entity.dto.*;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.*;
import com.microtomato.hirun.modules.bss.order.entity.po.*;
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
    private IOrderPlaneSketchService orderPlaneSketchService;

    @Autowired
    private IOrderContractService orderContractService;

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
                        if (payItem.getParentPayItemId() == 1) {
                            continue;
                        }
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

        //如果需要流转到指定人，才需要处理worker记录 首期款需要选择工程文员，尾款需要选择售后文员
        //判断当前状态，处理worker表

        if (orderStatus.equals("18") && auditStatus.equals("1")) {
            workerService.updateOrderWorker(dto.getOrderId(), 32L, dto.getEngineeringClerk());
        }
        if (orderStatus.equals("30") && auditStatus.equals("1")) {
            workerService.updateOrderWorker(dto.getOrderId(), 57L, dto.getServiceClerk());
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

        String[] feeTime = condition.getFeeTime();
        if (ArrayUtils.isNotEmpty(feeTime)) {
            wrapper.apply(" exists(select 1 from order_pay_no d where d.order_id = a.order_id and d.pay_date >= {0} and d.pay_date <= {1})", feeTime[0], feeTime[1]);
        }

        if (StringUtils.equals(condition.getCondition(), "1")) {
            wrapper.apply("a.status < 8 ");
        }

        if (condition.getReport() != null) {
            wrapper.apply("exists(select 1 from order_worker e where e.order_id = a.order_id and e.employee_id = {0} and e.role_id = 555 and e.end_date > now()) ", condition.getReport());
        }

        if (condition.getAgent() != null) {
            wrapper.apply("exists(select 1 from order_worker e where e.order_id = a.order_id and e.employee_id = {0} and e.role_id = 15 and e.end_date > now()) ", condition.getAgent());
        }

        if (condition.getDesigner() != null) {
            wrapper.apply("exists(select 1 from order_worker e where e.order_id = a.order_id and e.employee_id = {0} and e.role_id = 30 and e.end_date > now()) ", condition.getDesigner());
        }

        wrapper.eq(condition.getHousesId() != null, "a.houses_id", condition.getHousesId());

        IPage<DesignFeeDTO> pageDesigns = this.orderFeeMapper.queryDesignFee(request, wrapper);

        List<DesignFeeDTO> designFees = pageDesigns.getRecords();

        //设置订单参与人
        if (ArrayUtils.isNotEmpty(designFees)) {
            List<Long> orderIds = this.distinctOrderId(designFees);
            designFees.forEach(designFee -> {
                UsualOrderWorkerDTO usualOrderWorkerDTO = this.orderDomainService.getUsualOrderWorker(designFee.getOrderId());
                designFee.setUsualWorker(usualOrderWorkerDTO);
            });
            if (ArrayUtils.isNotEmpty(orderIds)) {
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

                List<OrderPlaneSketch> planeSketches = this.orderPlaneSketchService.queryByOrderIds(orderIds);
                if (ArrayUtils.isNotEmpty(planeSketches)) {
                    designFees.forEach(designFee -> {
                        Long orderId = designFee.getOrderId();
                        OrderPlaneSketch planeSketch = this.findPlaneSketch(orderId, planeSketches);
                        if (planeSketch != null) {
                            designFee.setDesignTheme(this.staticDataService.getCodeName("DESIGN_THEME", planeSketch.getDesignTheme()));
                            designFee.setDesignFeeStandard(planeSketch.getDesignFeeStandard());
                        }
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
     * 根据订单ID查看设计表
     * @param orderId
     * @param planeSketches
     * @return
     */
    private OrderPlaneSketch findPlaneSketch(Long orderId, List<OrderPlaneSketch> planeSketches) {
        for (OrderPlaneSketch planeSketch : planeSketches) {
            if (orderId.equals(planeSketch.getOrderId())) {
                return planeSketch;
            }
        }
        return null;
    }

    /**
     * 填充设计费用相关字段
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
        designFee.setDesignFee(total / 100d);
        designFee.setFirstPayTime(payTime);
        designFee.setDepositFee(totalDeposit / 100d);
        designFee.setFeeTime(payTime);
        designFee.setHouseLayoutName(this.staticDataService.getCodeName("HOUSE_MODE", designFee.getHouseLayout()));

        String orderStatus = designFee.getStatus();
        if (StringUtils.isNotBlank(orderStatus)) {
            OrderStatusCfg orderStatusCfg = this.orderStatusCfgService.getCfgByTypeStatus(designFee.getType(), orderStatus);
            designFee.setOrderStatusName(orderStatusCfg.getStatusName());
        }

        Long shopId = designFee.getShopId();
        if (shopId != null) {
            Org shop = this.orgService.queryByOrgId(shopId);
            if (shop != null) {
                designFee.setShopName(shop.getName());
            }
        }
    }

    /**
     * 工程款查询
     * @param condition
     * @return
     */
    @Override
    public IPage<ProjectFeeDTO> queryProjectFees(QueryProjectFeeDTO condition) {
        QueryWrapper<QueryProjectFeeDTO> wrapper = new QueryWrapper<>();
        wrapper.apply(" b.cust_id = a.cust_id ");
        wrapper.like(StringUtils.isNotBlank(condition.getCustName()), "b.cust_name", condition.getCustName());
        wrapper.like(StringUtils.isNotBlank(condition.getMobileNo()), "b.mobile_no", condition.getMobileNo());

        String shopIds = condition.getShopIds();
        if (StringUtils.isNotBlank(shopIds)) {
            List<String> shopIdArray = Arrays.asList(StringUtils.split(shopIds, ","));
            wrapper.in("a.shop_id", shopIdArray);
        }

        if (condition.getProjectManager() != null) {
            wrapper.apply("exists(select 1 from order_worker e where e.order_id = a.order_id and e.employee_id = {0} and e.role_id = 33 and e.end_date > now()) ", condition.getProjectManager());
        }

        if (condition.getAgent() != null) {
            wrapper.apply("exists(select 1 from order_worker e where e.order_id = a.order_id and e.employee_id = {0} and e.role_id = 15 and e.end_date > now()) ", condition.getAgent());
        }

        if (condition.getProjectCharger() != null) {
            wrapper.apply("exists(select 1 from order_worker e where e.order_id = a.order_id and e.employee_id = {0} and e.role_id = 48 and e.end_date > now()) ", condition.getProjectCharger());
        }

        if (condition.getDesigner() != null) {
            wrapper.apply("exists(select 1 from order_worker e where e.order_id = a.order_id and e.employee_id = {0} and e.role_id = 30 and e.end_date > now()) ", condition.getDesigner());
        }

        IPage<QueryProjectFeeDTO> page = new Page<>(condition.getPage(), condition.getLimit());
        IPage<ProjectFeeDTO> pageProjectFees = this.orderFeeMapper.queryProjectFee(page, wrapper);

        List<ProjectFeeDTO> projectFees = pageProjectFees.getRecords();

        if (ArrayUtils.isEmpty(projectFees)) {
            return pageProjectFees;
        }

        projectFees.forEach(projectFee -> {
            UsualOrderWorkerDTO usualWorker = this.orderDomainService.getUsualOrderWorker(projectFee.getOrderId());
            projectFee.setUsualWorker(usualWorker);

            //查询订单费用
            UsualFeeDTO usualFee = this.orderDomainService.getUsualOrderFee(projectFee.getOrderId(), projectFee.getType());
            projectFee.setUsualFee(usualFee);

            OrderContract contract = this.orderContractService.getByOrderIdType(projectFee.getOrderId(), "1");
            if (contract != null) {
                projectFee.setContractStartDate(contract.getStartDate());
                projectFee.setContractEndDate(contract.getEndDate());
                projectFee.setBusinessLevel(this.staticDataService.getCodeName("BUSI_LEVEL", contract.getBusiLevel()));
            }

            projectFee.setHouseLayoutName(this.staticDataService.getCodeName("HOUSE_MODE", projectFee.getHouseLayout()));
            String orderStatus = projectFee.getStatus();
            if (StringUtils.isNotBlank(orderStatus)) {
                OrderStatusCfg orderStatusCfg = this.orderStatusCfgService.getCfgByTypeStatus(projectFee.getType(), orderStatus);
                if (orderStatusCfg != null) {
                    projectFee.setOrderStatusName(orderStatusCfg.getStatusName());
                }
            }
        });

        return pageProjectFees;
    }

    /**
     * 查询未收齐款项工地
     * @param condition
     * @return
     */
    @Override
    public IPage<NoBalanceFeeDTO> queryNoBalanceFees(QueryNoBalanceFeeDTO condition) {
        QueryWrapper<QueryProjectFeeDTO> wrapper = new QueryWrapper<>();
        wrapper.apply(" b.cust_id = a.cust_id ");
        wrapper.apply("c.order_id = a.order_id ");
        wrapper.apply(" c.need_pay <> c.pay ");
        wrapper.apply("c.end_date > now() ");
        wrapper.eq(condition.getOrderId() != null, "a.order_id", condition.getOrderId());
        wrapper.eq(condition.getFeeType() != null, "c.type", condition.getFeeType());
        wrapper.eq(condition.getPeriods() != null, "c.periods", condition.getFeeType());

        String shopIds = condition.getShopIds();
        if (StringUtils.isNotBlank(shopIds)) {
            List<String> shopIdArray = Arrays.asList(StringUtils.split(shopIds, ","));
            wrapper.in("a.shop_id", shopIdArray);
        }

        if (condition.getProjectManager() != null) {
            wrapper.apply("exists(select 1 from order_worker e where e.order_id = a.order_id and e.employee_id = {0} and e.role_id = 33 and e.end_date > now()) ", condition.getProjectManager());
        }

        if (condition.getAgent() != null) {
            wrapper.apply("exists(select 1 from order_worker e where e.order_id = a.order_id and e.employee_id = {0} and e.role_id = 15 and e.end_date > now()) ", condition.getAgent());
        }

        if (condition.getCabinetDesigner() != null) {
            wrapper.apply("exists(select 1 from order_worker e where e.order_id = a.order_id and e.employee_id = {0} and e.role_id = 45 and e.end_date > now()) ", condition.getCabinetDesigner());
        }

        if (condition.getProjectCharger() != null) {
            wrapper.apply("exists(select 1 from order_worker e where e.order_id = a.order_id and e.employee_id = {0} and e.role_id = 48 and e.end_date > now()) ", condition.getProjectCharger());
        }

        if (condition.getMaterialManager() != null) {
            wrapper.apply("exists(select 1 from order_worker e where e.order_id = a.order_id and e.employee_id = {0} and e.role_id = 46 and e.end_date > now()) ", condition.getMaterialManager());
        }

        IPage<QueryNoBalanceFeeDTO> page = new Page<>(condition.getPage(), condition.getLimit());
        IPage<NoBalanceFeeDTO> pageNoBalanceFee = this.orderFeeMapper.queryNoBalanceFee(page, wrapper);

        List<NoBalanceFeeDTO> noBalanceFees = pageNoBalanceFee.getRecords();

        if (ArrayUtils.isEmpty(noBalanceFees)) {
            return pageNoBalanceFee;
        }

        noBalanceFees.forEach(noBalanceFee -> {
            UsualOrderWorkerDTO usualWorker = this.orderDomainService.getUsualOrderWorker(noBalanceFee.getOrderId());
            noBalanceFee.setUsualWorker(usualWorker);

            String orderStatus = noBalanceFee.getStatus();
            if (StringUtils.isNotBlank(orderStatus)) {
                OrderStatusCfg orderStatusCfg = this.orderStatusCfgService.getCfgByTypeStatus(noBalanceFee.getType(), orderStatus);
                if (orderStatusCfg != null) {
                    noBalanceFee.setOrderStatusName(orderStatusCfg.getStatusName());
                }
            }

            if (noBalanceFee.getPeriods() != null) {
                noBalanceFee.setPeriodsName(this.staticDataService.getCodeName("PERIODS", noBalanceFee.getPeriodsName()));
            }

            if (noBalanceFee.getFeeType() != null) {
                noBalanceFee.setFeeTypeName(this.staticDataService.getCodeName("FEE_TYPE", noBalanceFee.getFeeType()));
            }

            if (noBalanceFee.getNeedPay() == null) {
                noBalanceFee.setNeedPay(0d);
            } else {
                noBalanceFee.setNeedPay(noBalanceFee.getNeedPay() / 100d);
            }

            if (noBalanceFee.getPay() == null) {
                noBalanceFee.setPay(0d);
            } else {
                noBalanceFee.setPay(noBalanceFee.getPay() / 100d);
            }

            noBalanceFee.setMinus(noBalanceFee.getNeedPay() - noBalanceFee.getPay());
        });

        return pageNoBalanceFee;
    }
}
