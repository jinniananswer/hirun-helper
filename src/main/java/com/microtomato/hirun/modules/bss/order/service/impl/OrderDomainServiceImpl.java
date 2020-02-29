package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.mybatis.sequence.impl.PayNoCycleSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.security.Role;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.config.entity.dto.CascadeDTO;
import com.microtomato.hirun.modules.bss.config.entity.dto.CollectFeeDTO;
import com.microtomato.hirun.modules.bss.config.entity.dto.PayComponentDTO;
import com.microtomato.hirun.modules.bss.config.entity.dto.PayItemDTO;
import com.microtomato.hirun.modules.bss.config.entity.po.*;
import com.microtomato.hirun.modules.bss.config.service.*;
import com.microtomato.hirun.modules.bss.house.service.IHousesService;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.*;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayItem;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayMoney;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayNo;
import com.microtomato.hirun.modules.bss.order.exception.OrderException;
import com.microtomato.hirun.modules.bss.order.mapper.OrderBaseMapper;
import com.microtomato.hirun.modules.bss.order.service.*;
import com.microtomato.hirun.modules.system.entity.po.StaticData;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 订单领域服务实现类
 * @author: jinnian
 * @create: 2020-02-03 01:34
 **/
@Slf4j
@Service
public class OrderDomainServiceImpl implements IOrderDomainService {

    @Autowired
    private IOrderBaseService orderBaseService;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IOrderWorkerService orderWorkerService;

    @Autowired
    private IOrderRoleCfgService orderRoleCfgService;

    @Autowired
    private IOrderStatusCfgService orderStatusCfgService;

    @Autowired
    private IOrderStatusTransCfgService orderStatusTransCfgService;

    @Autowired
    private IOrderOperLogService orderOperLogService;

    @Autowired
    private IRoleAttentionStatusCfgService roleAttentionStatusCfgService;

    @Autowired
    private IPayItemCfgService payItemCfgService;

    @Autowired
    private IHousesService housesService;

    @Autowired
    private IOrderPayNoService orderPayNoService;

    @Autowired
    private IOrderPayItemService orderPayItemService;

    @Autowired
    private IOrderPayMoneyService orderPayMoneyService;

    @Autowired
    private IDualService dualService;

    @Autowired
    private OrderBaseMapper orderBaseMapper;


    /**
     * 查询订单综合信息
     * @param orderId
     * @return
     */
    @Override
    public OrderDetailDTO getOrderDetail(Long orderId) {
        OrderDetailDTO orderInfo = new OrderDetailDTO();
        OrderBase orderBase = this.orderBaseService.queryByOrderId(orderId);

        BeanUtils.copyProperties(orderBase, orderInfo);

        Long housesId = orderBase.getHousesId();
        if (housesId != null) {
            //查询楼盘信息
            if (housesId != null) {
                orderInfo.setHousesName(this.housesService.queryHouseName(housesId));
            }
        }

        if (StringUtils.isNotBlank(orderInfo.getStatus())) {
            orderInfo.setStatusName(this.staticDataService.getCodeName("ORDER_STATUS", orderInfo.getStatus()));
        }

        if (StringUtils.isNotBlank(orderInfo.getHouseLayout())) {
            orderInfo.setHouseLayoutName(this.staticDataService.getCodeName("HOUSE_LAYOUT", orderInfo.getHouseLayout()));
        }
        return orderInfo;
    }

    /**
     * 查询订单工作人员信息
     * @param orderId
     * @return
     */
    @Override
    public List<OrderWorkerDTO> queryOrderWorkers(Long orderId) {
        List<OrderWorkerDTO> orderWorkers = new ArrayList<>();

        List<OrderRoleCfg> configs = this.orderRoleCfgService.queryAllValid();
        if (ArrayUtils.isNotEmpty(configs)) {
            for (OrderRoleCfg config : configs) {
                OrderWorkerDTO orderWorker = new OrderWorkerDTO();
                orderWorker.setRoleId(config.getRoleId());
                orderWorker.setRoleName(config.getRoleName());
                orderWorkers.add(orderWorker);
            }
        }

        List<OrderWorkerDTO> existsWorkers = this.orderWorkerService.queryByOrderId(orderId);
        if (ArrayUtils.isNotEmpty(existsWorkers)) {
            for (OrderWorkerDTO worker : orderWorkers) {
                worker.setStatus("wait");
                worker.setName("暂无");
                for (OrderWorkerDTO existsWorker : existsWorkers) {
                    if (worker.getRoleId().equals(existsWorker.getRoleId())) {
                        worker.setName(existsWorker.getName());
                        //匹配上了，表示状态有效，前端则点亮
                        worker.setStatus("finish");
                    }
                }
            }
        }
        return orderWorkers;
    }

    /**
     * 创建新订单
     * @param newOrder
     */
    @Override
    public void createNewOrder(NewOrderDTO newOrder) {
        if (newOrder == null) {
            return;
        }
        OrderBase order = new OrderBase();
        BeanUtils.copyProperties(newOrder, order);
        if (StringUtils.isNotBlank(newOrder.getStatus())) {
            order.setStatus(OrderConst.ORDER_STATUS_ASKING);
        }

        if (StringUtils.isBlank(newOrder.getType())) {
            order.setType(OrderConst.ORDER_TYPE_PRE);
        }

        OrderStatusCfg orderStatusCfg = this.orderStatusCfgService.getCfgByTypeStatus(order.getType(), order.getStatus());

        if (orderStatusCfg != null) {
            order.setStage(orderStatusCfg.getOrderStage());
        }

        this.orderBaseService.save(order);
        this.orderOperLogService.createOrderOperLog(order.getOrderId(), OrderConst.LOG_TYPE_CREATE, order.getStage(), order.getStatus(), OrderConst.OPER_LOG_CONTENT_CREATE);
    }

    /**
     * 订单状态切换
     * @param orderId
     * @param oper 见OrderConst里面的常量
     */
    @Override
    public void orderStatusTrans(Long orderId, String oper) {
        OrderBase order = this.orderBaseService.queryByOrderId(orderId);
        this.orderStatusTrans(order, oper);
    }

    /**
     * 订单状态切换
     * @param order
     * @param oper 见OrderConst里面的常量
     */
    @Override
    public void orderStatusTrans(OrderBase order, String oper) {
        Integer stage = order.getStage();
        String status = order.getStatus();
        String orderType = order.getType();
        OrderStatusCfg statusCfg = this.orderStatusCfgService.getCfgByTypeStatus(orderType, status);
        OrderStatusTransCfg statusTransCfg = null;
        if (StringUtils.equals(OrderConst.OPER_RUN, oper)) {
            statusTransCfg = this.orderStatusTransCfgService.getByStatusIdOper(-1L, oper);
        } else {
            statusTransCfg = this.orderStatusTransCfgService.getByStatusIdOper(statusCfg.getId(), oper);
        }

        if (statusTransCfg == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.STATUS_TRANS_CFG_NOT_FOUND);
        }

        Long newStatusId = statusTransCfg.getNextOrderStatusId();
        OrderStatusCfg newStatusCfg = this.orderStatusCfgService.getById(newStatusId);

        if (newStatusCfg == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.STATUS_CFG_NOT_FOUND);
        }

        Integer isUpdatePreious = statusTransCfg.getIsUpdatePrevious();

        if (isUpdatePreious.equals(new Integer(1))) {
            //需要修改前续订单状态
            order.setPreviousStage(stage);
            order.setPreviousStatus(status);
        }

        order.setStage(newStatusCfg.getOrderStage());
        order.setStatus(newStatusCfg.getOrderStatus());

        //保存订单信息
        this.orderBaseService.updateById(order);

        String stageName = this.staticDataService.getCodeName("ORDER_STAGE", stage + "");
        String statusName = this.staticDataService.getCodeName("ORDER_STATUS", status);

        String newStageName = this.staticDataService.getCodeName("ORDER_STAGE", newStatusCfg.getOrderStage() + "");
        String newStatusName = this.staticDataService.getCodeName("ORDER_STATUS", newStatusCfg.getOrderStatus());

        String logContent = "，由订单阶段：" + stageName + "，订单状态：" + statusName + "变为新订单阶段：" + newStageName+"，新订单状态：" + newStatusName;

        this.orderOperLogService.createOrderOperLog(order.getOrderId(), OrderConst.LOG_TYPE_STATUS_TRANS, newStatusCfg.getOrderStage(), newStatusCfg.getOrderStatus(), OrderConst.OPER_LOG_CONTENT_STATUS_CHANGE+logContent);
    }

    /**
     * 查询主营业务系统的待办任务
     * @return
     */
    @Override
    public List<PendingTaskDTO> queryPendingTask() {
        List<Role> roles = WebContextUtils.getUserContext().getRoles();
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        if (ArrayUtils.isEmpty(roles)) {
            return null;
        }

        List<Long> roleIds = new ArrayList<>();
        for (Role role : roles) {
            roleIds.add(role.getId());
        }
        List<RoleAttentionStatusCfg> roleAttentionStatusCfgs = this.roleAttentionStatusCfgService.queryInRoleIds(roleIds);
        if (ArrayUtils.isEmpty(roleAttentionStatusCfgs)) {
            return null;
        }

        String statuses = "";
        List<PendingTaskDTO> tasks = new ArrayList<>();

        for (RoleAttentionStatusCfg attentionStatusCfg : roleAttentionStatusCfgs) {
            Long statusId = attentionStatusCfg.getAttentionStatusId();
            OrderStatusCfg statusCfg = this.orderStatusCfgService.getById(statusId);
            statuses += statusCfg.getOrderStatus() + ",";

            PendingTaskDTO task = new PendingTaskDTO();
            task.setStatusId(statusCfg.getId());
            task.setStatus(statusCfg.getOrderStatus());
            task.setPageUrl(statusCfg.getPageUrl());
            task.setStatusName(this.staticDataService.getCodeName("ORDER_STATUS", statusCfg.getOrderStatus()));
            tasks.add(task);
        }

        List<PendingOrderDTO> orders = this.orderBaseMapper.queryAttentionStatusOrders(statuses.substring(0, statuses.length() -1), employeeId);
        if (ArrayUtils.isEmpty(orders)) {
            return tasks;
        }

        for (PendingTaskDTO task : tasks) {
            String status = task.getStatus();
            List<PendingOrderDTO> statusOrders = new ArrayList<>();
            for (PendingOrderDTO order : orders) {
                if (StringUtils.equals(order.getStatus(), status)) {
                    statusOrders.add(order);
                }
            }

            task.setOrders(statusOrders);
        }

        return tasks;
    }

    /**
     * 分页查询客户订单信息
     * @return
     */
    @Override
    public IPage<CustOrderInfoDTO> queryCustOrderInfos(CustOrderQueryDTO queryCondition, Page<CustOrderQueryDTO> page) {
        QueryWrapper<CustOrderQueryDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(queryCondition.getCustName()), "b.cust_name", queryCondition.getCustName());
        queryWrapper.eq(StringUtils.isNotEmpty(queryCondition.getSex()), "b.sex", queryCondition.getSex());
        queryWrapper.likeRight(StringUtils.isNotEmpty(queryCondition.getMobileNo()), "b.mobile_no", queryCondition.getMobileNo());
        queryWrapper.eq(StringUtils.isNotEmpty(queryCondition.getOrderStatus()), "a.status", queryCondition.getOrderStatus());
        queryWrapper.eq(queryCondition.getHousesId() != null, "a.housesId", queryCondition.getHousesId());
        IPage<CustOrderInfoDTO> result = this.orderBaseMapper.queryCustOrderInfo(page, queryWrapper);

        List<CustOrderInfoDTO> custOrders = result.getRecords();
        if (ArrayUtils.isNotEmpty(custOrders)) {
            for (CustOrderInfoDTO custOrder : custOrders) {
                custOrder.setStageName(this.staticDataService.getCodeName("ORDER_STAGE", custOrder.getStage()));
                custOrder.setSexName(this.staticDataService.getCodeName("SEX", custOrder.getSex()));
                custOrder.setStatusName(this.staticDataService.getCodeName("ORDER_STATUS", custOrder.getStatus()));
                custOrder.setHouseLayoutName(this.staticDataService.getCodeName("HOUSE_LAYOUT", custOrder.getHouseLayout()));
                Long housesId = custOrder.getHousesId();
                if (housesId != null) {
                    custOrder.setHousesName(this.housesService.queryHouseName(housesId));

                }
            }
        }
        return result;
    }

    /**
     * 获取支付方式
     * @return
     */
    @Override
    public List<PaymentDTO> queryPayment() {
        List<PaymentDTO> payments = new ArrayList<>();

        List<StaticData> configs = this.staticDataService.getStaticDatas("PAYMENT_TYPE");
        if (ArrayUtils.isEmpty(configs)) {
            return payments;
        }

        for (StaticData config : configs) {
            PaymentDTO payment = new PaymentDTO();
            payment.setPaymentType(config.getCodeValue());
            payment.setPaymentName(config.getCodeName());
            payments.add(payment);
        }
        return payments;
    }

    /**
     * 初始化支付组件
     * @return
     */
    @Override
    public PayComponentDTO initPayComponent(Long orderId, Long payNo) {
        PayComponentDTO componentData = new PayComponentDTO();
        List<PaymentDTO> payments = new ArrayList<>();

        List<StaticData> configs = this.staticDataService.getStaticDatas("PAYMENT_TYPE");
        if (ArrayUtils.isNotEmpty(configs)) {
            for (StaticData config : configs) {
                PaymentDTO payment = new PaymentDTO();
                payment.setPaymentType(config.getCodeValue());
                payment.setPaymentName(config.getCodeName());
                payments.add(payment);
            }
            componentData.setPayments(payments);
        }

        if (orderId != null && payNo != null) {
            OrderPayNo orderPayNo = this.orderPayNoService.getByOrderIdAndPayNo(orderId, payNo);
            if (orderPayNo != null) {
                Long totalMoney = orderPayNo.getTotalMoney();
                if (totalMoney != null) {
                    //存储用分为单位，到界面上转换成元
                    componentData.setNeedPay(totalMoney.doubleValue()/100);
                }
                componentData.setPayDate(orderPayNo.getPayDate());
            }

            List<OrderPayItem> payItems = this.orderPayItemService.queryByOrderIdPayNo(orderId, payNo);
            if (ArrayUtils.isNotEmpty(payItems)) {
                List<PayItemDTO> payItemDTOs = new ArrayList<>();
                for (OrderPayItem payItem : payItems) {
                    PayItemDTO payItemDTO = new PayItemDTO();
                    payItemDTO.setPayItemId("pay_"+payItem.getPayItemId());
                    payItemDTO.setMoney(payItem.getFee().doubleValue()/100);

                    String payItemName = this.payItemCfgService.getPath(payItem.getPayItemId());
                    Integer payPeriod = payItem.getPeriods();
                    if (payPeriod != null) {
                        payItemDTO.setPeriod(payPeriod);
                        String payPeriodName = this.staticDataService.getCodeName("PAY_PERIODS", payPeriod + "");
                        payItemDTO.setPeriodName(payPeriodName);
                        payItemName += '-'+payPeriodName;
                    }
                    payItemDTO.setPayItemName(payItemName);

                    payItemDTOs.add(payItemDTO);
                }
                componentData.setPayItems(payItemDTOs);
            }

            List<OrderPayMoney> payMonies = this.orderPayMoneyService.queryByOrderIdPayNo(orderId, payNo);
            if (ArrayUtils.isNotEmpty(payMonies)) {
                for (OrderPayMoney payMoney : payMonies) {
                    for (PaymentDTO payment : payments) {
                        if (StringUtils.equals(payment.getPaymentType(), payMoney.getPaymentType())) {
                            payment.setMoney(payMoney.getMoney().doubleValue()/100);
                            break;
                        }
                    }
                }
            }
        }
        List<PayItemCfg> payItemCfgs = this.payItemCfgService.queryPlusPayItems();
        List<CascadeDTO<PayItemCfg>> payItems = this.buildPayItemCascade(payItemCfgs);

        if (ArrayUtils.isNotEmpty(payItems)) {
            for (CascadeDTO<PayItemCfg> node : payItems) {
                this.addPeriods(node);
            }
            componentData.setPayItemOption(payItems);
        }



        return componentData;
    }

    /**
     * 订单财务收款
     * @param feeData
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void collectFee(CollectFeeDTO feeData) {
        List<PayItemDTO> payItems = feeData.getPayItems();
        Long payNo = this.dualService.nextval(PayNoCycleSeq.class);
        Long orderId = feeData.getOrderId();
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        LocalDate payDate = feeData.getPayDate();
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        LocalDateTime forever = TimeUtils.getForeverTime();

        Double needPayDouble = feeData.getNeedPay();

        if (needPayDouble == null || needPayDouble <= 0.001) {
            throw new OrderException(OrderException.OrderExceptionEnum.PAY_MUST_MORE_THAN_ZERO);
        }

        Long needPay = new Long(Math.round(needPayDouble * 100));
        Long payItemTotal = 0L;
        List<OrderPayItem> orderPayItems = new ArrayList<>();

        if (ArrayUtils.isNotEmpty(payItems)) {
            for (PayItemDTO payItem : payItems) {
                Double money = payItem.getMoney();
                if (money == null || money <= 0.001) {
                    continue;
                }

                Long fee = new Long(Math.round(money*100));

                OrderPayItem orderPayItem = new OrderPayItem();
                orderPayItem.setOrderId(orderId);
                orderPayItem.setPayItemId(new Long(payItem.getPayItemId()));
                orderPayItem.setPeriods(payItem.getPeriod());
                orderPayItem.setFee(fee);
                orderPayItem.setPayNo(payNo);
                orderPayItem.setStartDate(now);
                orderPayItem.setEndDate(forever);
                orderPayItems.add(orderPayItem);
                payItemTotal+= fee;
            }
        }
        List<PaymentDTO> payments = feeData.getPayments();
        List<OrderPayMoney> payMonies = new ArrayList<>();

        Long totalMoney = 0L;

        if (ArrayUtils.isNotEmpty(payments)) {
            for (PaymentDTO payment : payments) {
                OrderPayMoney payMoney = new OrderPayMoney();
                payMoney.setOrderId(orderId);
                payMoney.setPaymentType(payment.getPaymentType());

                Double money = payment.getMoney();
                if (money == null || money <= 0.001) {
                    continue;
                }
                Long fee = new Long(Math.round(money*100));
                payMoney.setMoney(fee);
                payMoney.setPayNo(payNo);
                payMoney.setStartDate(now);
                payMoney.setEndDate(forever);
                payMonies.add(payMoney);
                totalMoney+=fee;
            }
        }
        if (!payItemTotal.equals(needPay)) {
            throw new OrderException(OrderException.OrderExceptionEnum.PAY_MUST_EQUAL_PAYITEM);
        }
        if (!payItemTotal.equals(totalMoney)) {
            throw new OrderException(OrderException.OrderExceptionEnum.PAY_MUST_EQUAL_PAYITEM);
        }

        OrderPayNo orderPayNo = new OrderPayNo();
        orderPayNo.setOrderId(orderId);
        orderPayNo.setPayDate(payDate);
        orderPayNo.setPayNo(payNo);
        //待审核状态
        orderPayNo.setAuditStatus("0");
        orderPayNo.setStartDate(now);
        orderPayNo.setEndDate(forever);
        orderPayNo.setTotalMoney(needPay);
        orderPayNo.setPayEmployeeId(employeeId);
        orderPayNo.setOrgId(WebContextUtils.getUserContext().getOrgId());
        this.orderPayNoService.save(orderPayNo);

        if (ArrayUtils.isNotEmpty(orderPayItems)) {
            this.orderPayItemService.saveBatch(orderPayItems);
        }
        if (ArrayUtils.isNotEmpty(payMonies)) {
            this.orderPayMoneyService.saveBatch(payMonies);
        }

        //todo 补充更新order_fee对应实收的逻辑
    }

    /**
     * 构建支付类型选项树
     * @return
     */
    private List<CascadeDTO<PayItemCfg>> buildPayItemCascade(List<PayItemCfg> payItemCfgs) {
        if (ArrayUtils.isEmpty(payItemCfgs)) {
            return null;
        }

        List<CascadeDTO<PayItemCfg>> roots = new ArrayList<>();
        for (PayItemCfg payItemCfg : payItemCfgs) {
            if (payItemCfg.getParentPayItemId().equals(-1L)) {
                CascadeDTO<PayItemCfg> root = new CascadeDTO<>();
                root.setLabel(payItemCfg.getName());
                root.setValue("pay_"+payItemCfg.getId());
                root.setSelf(payItemCfg);
                roots.add(root);
            }
        }

        if (ArrayUtils.isNotEmpty(roots)) {
            for (CascadeDTO<PayItemCfg> root : roots) {
                this.buildPayItemChildren(root, payItemCfgs);
            }
        }

        return roots;
    }

    /**
     * 构建子孙节点
     * @param root
     * @param payItemCfgs
     * @return
     */
    private void buildPayItemChildren(CascadeDTO root, List<PayItemCfg> payItemCfgs) {
        List<CascadeDTO<PayItemCfg>> children = new ArrayList<>();
        for (PayItemCfg payItemCfg : payItemCfgs) {
            if (StringUtils.equals("pay_"+payItemCfg.getParentPayItemId(),root.getValue())) {
                CascadeDTO<PayItemCfg> child = new CascadeDTO<>();
                child.setLabel(payItemCfg.getName());
                child.setValue("pay_"+payItemCfg.getId());
                child.setSelf(payItemCfg);
                children.add(child);

                buildPayItemChildren(child, payItemCfgs);
            }
        }
        if (ArrayUtils.isNotEmpty(children)) {
            root.setChildren(children);
        }
    }

    /**
     * 给级联增加分期付款期数
     * @param node
     */
    private void addPeriods(CascadeDTO<PayItemCfg> node) {
        if (ArrayUtils.isNotEmpty(node.getChildren())) {
            List<CascadeDTO<PayItemCfg>> children = node.getChildren();
            for (CascadeDTO child : children) {
                this.addPeriods(child);
            }
        } else {
            PayItemCfg payItemCfg = node.getSelf();
            if (payItemCfg.getIsPeriod().equals(new Integer(1))) {
                String periods = payItemCfg.getPeriods();
                String[] periodsArray = periods.split(",");
                List<CascadeDTO<PayItemCfg>> results = new ArrayList<>();
                for (String period : periodsArray) {
                    String periodName = this.staticDataService.getCodeName("PAY_PERIODS", period);
                    CascadeDTO<PayItemCfg> result = new CascadeDTO<>();
                    result.setLabel(periodName);
                    result.setValue("period_"+period);
                    results.add(result);
                }

                if (ArrayUtils.isNotEmpty(results)) {
                    node.setChildren(results);
                }
            }

        }
    }
}
