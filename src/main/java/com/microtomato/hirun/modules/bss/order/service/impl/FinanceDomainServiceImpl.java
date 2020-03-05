package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.mybatis.sequence.impl.PayNoCycleSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.config.entity.dto.CascadeDTO;
import com.microtomato.hirun.modules.bss.config.entity.dto.CollectFeeDTO;
import com.microtomato.hirun.modules.bss.config.entity.dto.PayComponentDTO;
import com.microtomato.hirun.modules.bss.config.entity.dto.PayItemDTO;
import com.microtomato.hirun.modules.bss.config.entity.po.PayItemCfg;
import com.microtomato.hirun.modules.bss.config.service.IPayItemCfgService;
import com.microtomato.hirun.modules.bss.house.service.IHousesService;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.*;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayItem;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayMoney;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayNo;
import com.microtomato.hirun.modules.bss.order.exception.OrderException;
import com.microtomato.hirun.modules.bss.order.mapper.OrderBaseMapper;
import com.microtomato.hirun.modules.bss.order.service.IFinanceDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderPayItemService;
import com.microtomato.hirun.modules.bss.order.service.IOrderPayMoneyService;
import com.microtomato.hirun.modules.bss.order.service.IOrderPayNoService;
import com.microtomato.hirun.modules.system.entity.po.StaticData;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @program: hirun-helper
 * @description: 财务领域服务实现类
 * @author: jinnian
 * @create: 2020-03-01 00:59
 **/
@Service
@Slf4j
public class FinanceDomainServiceImpl implements IFinanceDomainService {

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IPayItemCfgService payItemCfgService;

    @Autowired
    private IOrderPayNoService orderPayNoService;

    @Autowired
    private IOrderPayItemService orderPayItemService;

    @Autowired
    private IOrderPayMoneyService orderPayMoneyService;

    @Autowired
    private IHousesService housesService;

    @Autowired
    private IDualService dualService;

    @Autowired
    private OrderBaseMapper orderBaseMapper;

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

        componentData.setNeedPay(new Double(0));

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

                Long payItemId = new Long(payItem.getPayItemId());
                PayItemCfg payItemCfg = this.payItemCfgService.getPayItem(payItemId);
                if (payItemCfg == null) {
                    throw new OrderException(OrderException.OrderExceptionEnum.PAY_ITEM_NOT_FOUND, payItem.getPayItemId());
                }
                OrderPayItem orderPayItem = new OrderPayItem();
                orderPayItem.setOrderId(orderId);
                orderPayItem.setPayItemId(payItemId);
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
        orderPayNo.setAuditStatus(OrderConst.AUDIT_STATUS_INIT);
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
     * 更改收费项目或者付款方式
     * @param feeData
     */
    @Override
    public void changePay(CollectFeeDTO feeData) {
        Long orderId = feeData.getOrderId();
        Long payNo = feeData.getPayNo();

        if (orderId == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.ARGUMENT_NOT_FOUND, "orderId");
        }
        if (payNo == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.ARGUMENT_NOT_FOUND, "payNo");
        }

        LocalDateTime now = RequestTimeHolder.getRequestTime();
        //先终止原来的收款记录
        OrderPayNo orderPayNo = this.orderPayNoService.getByOrderIdAndPayNo(orderId, payNo);
        if (orderPayNo != null) {
            orderPayNo.setEndDate(now);
            this.orderPayNoService.updateById(orderPayNo);
        }

        List<OrderPayItem> orderPayItems = this.orderPayItemService.queryByOrderIdPayNo(orderId, payNo);
        if (ArrayUtils.isNotEmpty(orderPayItems)) {
            for (OrderPayItem orderPayItem : orderPayItems) {
                orderPayItem.setEndDate(now);
            }

            this.orderPayItemService.updateBatchById(orderPayItems);
        }

        List<OrderPayMoney> orderPayMonies = this.orderPayMoneyService.queryByOrderIdPayNo(orderId, payNo);
        if (ArrayUtils.isNotEmpty(orderPayMonies)) {
            for (OrderPayMoney orderPayMoney : orderPayMonies) {
                orderPayMoney.setEndDate(now);
            }
            this.orderPayMoneyService.updateBatchById(orderPayMonies);
        }

        this.collectFee(feeData);
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

    /**
     * 分页查询客户订单信息
     * @return
     */
    @Override
    public IPage<CustOrderInfoDTO> queryCustOrderInfos(CustOrderQueryDTO queryCondition, Page<CustOrderQueryDTO> page) {
        QueryWrapper<CustOrderQueryDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply(" b.cust_id = a.cust_id ");
        queryWrapper.like(StringUtils.isNotEmpty(queryCondition.getCustName()), "b.cust_name", queryCondition.getCustName());
        queryWrapper.eq(StringUtils.isNotEmpty(queryCondition.getSex()), "b.sex", queryCondition.getSex());
        queryWrapper.likeRight(StringUtils.isNotEmpty(queryCondition.getMobileNo()), "b.mobile_no", queryCondition.getMobileNo());
        queryWrapper.eq(StringUtils.isNotEmpty(queryCondition.getOrderStatus()), "a.status", queryCondition.getOrderStatus());
        queryWrapper.eq(queryCondition.getHousesId() != null, "a.housesId", queryCondition.getHousesId());
        //排除售后，订单关闭的状态
        queryWrapper.notIn("a.status", "32", "33", "100");
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

    @Override
    public List<FinancePendingTaskDTO> queryFinancePendingTask() {
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        //todo 检查有没有审核权限
        List<FinancePendingOrderDTO> financeOrders = this.orderBaseMapper.queryFinancePendingOrders(employeeId, "0,2");
        if (ArrayUtils.isEmpty(financeOrders)) {
            return null;
        }

        List<FinancePendingTaskDTO> financeTasks = new ArrayList<>();
        Map<String, FinancePendingTaskDTO> temp = new HashMap<>();
        for (FinancePendingOrderDTO financeOrder : financeOrders) {
            String auditStatus = financeOrder.getAuditStatus();
            String orderStatus = financeOrder.getStatus();
            financeOrder.setStatusName(this.staticDataService.getCodeName("ORDER_STATUS", orderStatus));
            financeOrder.setAuditStatusName(this.staticDataService.getCodeName("AUDIT_STATUS", auditStatus));
            FinancePendingTaskDTO task = null;
            if (!temp.containsKey(auditStatus)) {
                task = new FinancePendingTaskDTO();
                task.setStatus(auditStatus);
                task.setStatusName(this.staticDataService.getCodeName("AUDIT_STATUS", auditStatus));
                temp.put(auditStatus, task);
            } else {
                task = temp.get(auditStatus);
            }

            List<FinancePendingOrderDTO> orders = null;
            if (task.getOrders() == null) {
                orders = new ArrayList<>();
                task.setOrders(orders);
            } else {
                orders = task.getOrders();
            }

            orders.add(financeOrder);
        }

        List<FinancePendingTaskDTO> result = new ArrayList<>();
        Set<String> keys = temp.keySet();
        for (String key : keys) {
            result.add(temp.get(key));
        }
        return result;
    }
}
