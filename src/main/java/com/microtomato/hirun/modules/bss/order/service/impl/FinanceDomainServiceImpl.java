package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.mybatis.sequence.impl.FeeNoCycleSeq;
import com.microtomato.hirun.framework.mybatis.sequence.impl.PayNoCycleSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.*;
import com.microtomato.hirun.modules.bss.config.service.*;
import com.microtomato.hirun.modules.bss.customer.entity.po.CustBase;
import com.microtomato.hirun.modules.bss.customer.service.ICustBaseService;
import com.microtomato.hirun.modules.bss.house.entity.po.Houses;
import com.microtomato.hirun.modules.bss.house.service.IHousesService;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.*;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.CustPayDataDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.FinanceOrderTaskDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.FinanceOrderTaskQueryDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.*;
import com.microtomato.hirun.modules.bss.order.exception.OrderException;
import com.microtomato.hirun.modules.bss.order.mapper.NormalPayNoMapper;
import com.microtomato.hirun.modules.bss.order.mapper.OrderBaseMapper;
import com.microtomato.hirun.modules.bss.order.service.*;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplierBrand;
import com.microtomato.hirun.modules.bss.supply.service.ISupplierBrandService;
import com.microtomato.hirun.modules.finance.entity.po.FinanceAcct;
import com.microtomato.hirun.modules.finance.service.IFinanceAcctService;
import com.microtomato.hirun.modules.organization.entity.domain.EmployeeDO;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
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

    @Autowired
    private ICollectionItemCfgService collectionItemCfgService;

    @Autowired
    private IDecoratorService decoratorService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IEnterpriseService enterpriseService;

    @Autowired
    private IOrgService orgService;

    @Autowired
    private ISupplierBrandService supplierBrandService;

    @Autowired
    private INormalPayItemService normalPayItemService;

    @Autowired
    private INormalPayMoneyService normalPayMoneyService;

    @Autowired
    private INormalPayNoService normalPayNoService;

    @Autowired
    private NormalPayNoMapper normalPayNoMapper;

    @Autowired
    private IOrderStatusCfgService orderStatusCfgService;

    @Autowired
    private IFeePayRelCfgService feePayRelCfgService;

    @Autowired
    private IFeeItemCfgService feeItemCfgService;

    @Autowired
    private IOrderFeeService orderFeeService;

    @Autowired
    private IOrderBaseService orderBaseService;

    @Autowired
    private IFinanceAcctService financeAcctService;

    @Autowired
    private ICustBaseService custBaseService;
    /**
     * 初始化支付组件
     *
     * @return
     */
    @Override
    public PayComponentDTO initPayComponent(Long orderId, Long payNo) {
        PayComponentDTO componentData = new PayComponentDTO();
        List<PaymentDTO> payments = new ArrayList<>();

        List<FinanceAcct> financeAccts = this.financeAcctService.queryByLoginEmployeeId();
        if (ArrayUtils.isNotEmpty(financeAccts)) {
            for (FinanceAcct financeAcct : financeAccts) {
                PaymentDTO payment = new PaymentDTO();
                payment.setPaymentId(financeAcct.getId());
                payment.setPaymentName(financeAcct.getName());
                payment.setPaymentType(financeAcct.getType());
                payment.setPaymentTypeName(this.staticDataService.getCodeName("FINANCE_ACCT_TYPE", financeAcct.getType()));
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
                    componentData.setNeedPay(totalMoney.doubleValue() / 100);
                }
                componentData.setPayDate(orderPayNo.getPayDate());
                componentData.setAuditComment(orderPayNo.getAuditComment());
                componentData.setRemark(orderPayNo.getRemark());
            }

            List<OrderPayItem> payItems = this.orderPayItemService.queryByOrderIdPayNo(orderId, payNo);
            if (ArrayUtils.isNotEmpty(payItems)) {
                List<PayItemDTO> payItemDTOs = new ArrayList<>();
                for (OrderPayItem payItem : payItems) {
                    PayItemDTO payItemDTO = new PayItemDTO();
                    payItemDTO.setPayItemId("pay_" + payItem.getPayItemId());
                    payItemDTO.setMoney(payItem.getFee().doubleValue() / 100);

                    String payItemName = this.payItemCfgService.getPath(payItem.getPayItemId());
                    Integer payPeriod = payItem.getPeriods();
                    if (payPeriod != null) {
                        payItemDTO.setPeriod("period_" + payPeriod);
                        String payPeriodName = this.staticDataService.getCodeName("PAY_PERIODS", payPeriod + "");
                        payItemDTO.setPeriodName(payPeriodName);
                        payItemName += '-' + payPeriodName;
                    }
                    payItemDTO.setPayItemName(payItemName);
                    payItemDTO.setRemark(payItem.getRemark());
                    payItemDTOs.add(payItemDTO);
                }
                componentData.setPayItems(payItemDTOs);
            }

            List<OrderPayMoney> payMonies = this.orderPayMoneyService.queryByOrderIdPayNo(orderId, payNo);
            if (ArrayUtils.isNotEmpty(payMonies)) {
                for (OrderPayMoney payMoney : payMonies) {
                    for (PaymentDTO payment : payments) {
                        if (payment.getPaymentId().equals(payMoney.getPaymentId())) {
                            payment.setMoney(payMoney.getMoney().doubleValue() / 100);
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
     * 获取支付其他信息
     * @param orderId
     * @param payNo
     * @return
     */
    @Override
    public CustPayDataDTO getCustPayData(Long orderId, Long payNo) {
        CustPayDataDTO data = new CustPayDataDTO();

        OrderBase orderBase = this.orderBaseService.queryByOrderId(orderId);
        if (orderBase == null) {
            return null;
        }

        data.setOrderId(orderBase.getOrderId());
        data.setCustId(orderBase.getCustId());
        data.setAddress(orderBase.getDecorateAddress());
        data.setHousesId(orderBase.getHousesId());

        OrderPayNo orderPayNo = this.orderPayNoService.getByOrderIdAndPayNo(orderId, payNo);
        if (orderPayNo != null) {
            data.setPayNoRemark(orderPayNo.getRemark());
            data.setAuditComment(orderPayNo.getAuditComment());
        }

        CustBase custBase = this.custBaseService.queryByCustId(orderBase.getCustId());
        if (custBase != null) {
            data.setCustName(custBase.getCustName());
        }

        return data;
    }

    /**
     * 订单财务收款
     *
     * @param feeData
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void collectFee(CollectFeeDTO feeData) {

        LocalDateTime now = RequestTimeHolder.getRequestTime();

        List<PayItemDTO> payItems = feeData.getPayItems();
        Long orderId = feeData.getOrderId();

        Long oldPayNo = feeData.getPayNo();
        if (oldPayNo != null) {
            //终止老的未审核通过的信息
            OrderPayNo orderPayNo = this.orderPayNoService.getByOrderIdAndPayNo(orderId, oldPayNo);
            if (orderPayNo != null) {
                orderPayNo.setEndDate(now);
                this.orderPayNoService.updateById(orderPayNo);
            }

            List<OrderPayMoney> orderPayMonies = this.orderPayMoneyService.queryByOrderIdPayNo(orderId, oldPayNo);
            if (ArrayUtils.isNotEmpty(orderPayMonies)) {
                orderPayMonies.forEach(orderPayMoney -> {
                    orderPayMoney.setEndDate(now);
                });
                this.orderPayMoneyService.updateBatchById(orderPayMonies);
            }

            List<OrderPayItem> orderPayItems = this.orderPayItemService.queryByOrderIdPayNo(orderId, oldPayNo);
            if (ArrayUtils.isNotEmpty(orderPayItems)) {
                orderPayItems.forEach(orderPayItem -> {
                    orderPayItem.setEndDate(now);
                });
                this.orderPayItemService.updateBatchById(orderPayItems);
            }
        }
        Long payNo = this.dualService.nextval(PayNoCycleSeq.class);
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        LocalDate payDate = feeData.getPayDate();
        LocalDateTime forever = TimeUtils.getForeverTime();

        Double needPayDouble = feeData.getNeedPay();

        if (needPayDouble == null || needPayDouble <= 0.001) {
            throw new OrderException(OrderException.OrderExceptionEnum.PAY_MUST_MORE_THAN_ZERO);
        }

        Long needPay = new Long(Math.round(needPayDouble * 100));
        Long payItemTotal = 0L;
        List<OrderPayItem> orderPayItems = new ArrayList<>();

        Map<String, Long> feeType = new HashMap<>();

        if (ArrayUtils.isNotEmpty(payItems)) {
            for (PayItemDTO payItem : payItems) {
                Double money = payItem.getMoney();
                if (money == null || money <= 0.001) {
                    continue;
                }

                Long fee = new Long(Math.round(money * 100));

                Long payItemId = new Long(payItem.getPayItemId());
                PayItemCfg payItemCfg = this.payItemCfgService.getPayItem(payItemId);
                if (payItemCfg == null) {
                    throw new OrderException(OrderException.OrderExceptionEnum.PAY_ITEM_NOT_FOUND, payItem.getPayItemId());
                }
                OrderPayItem orderPayItem = new OrderPayItem();
                orderPayItem.setOrderId(orderId);
                orderPayItem.setPayItemId(payItemId);
                orderPayItem.setParentPayItemId(payItemCfg.getParentPayItemId());
                if (StringUtils.isNotBlank(payItem.getPeriod())) {
                    orderPayItem.setPeriods(Integer.parseInt(payItem.getPeriod()));
                }
                orderPayItem.setFee(fee);
                orderPayItem.setPayNo(payNo);
                orderPayItem.setStartDate(now);
                orderPayItem.setEndDate(forever);
                orderPayItem.setRemark(payItem.getRemark());
                orderPayItems.add(orderPayItem);
                payItemTotal += fee;

                //查看付款项与费用类型的关系
                FeePayRelCfg feePayRelCfg = this.feePayRelCfgService.getByPayItemId(payItemId);
                if (feePayRelCfg != null) {
                    Long feeItemId = feePayRelCfg.getFeeItemId();
                    FeeItemCfg feeItemCfg = this.feeItemCfgService.getFeeItem(feeItemId);
                    if (feeItemCfg != null) {
                        String type = feeItemCfg.getType();
                        String key = type;

                        Boolean isPeriod = feeItemCfg.getIsPeriod();
                        if (isPeriod) {
                            key = type + "," +payItem.getPeriod();
                        }
                        if (!feeType.containsKey(key)) {
                            feeType.put(key, new Long(0L));
                        }

                        Long payed = feeType.get(key);
                        payed += fee;
                        feeType.put(key, payed);
                    }
                }
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
                payMoney.setPaymentId(payment.getPaymentId());

                Double money = payment.getMoney();
                if (money == null || money <= 0.001) {
                    continue;
                }
                Long fee = new Long(Math.round(money * 100));
                payMoney.setMoney(fee);
                payMoney.setPayNo(payNo);
                payMoney.setStartDate(now);
                payMoney.setEndDate(forever);
                payMonies.add(payMoney);
                totalMoney += fee;
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
        orderPayNo.setRemark(feeData.getRemark());
        orderPayNo.setOrgId(WebContextUtils.getUserContext().getOrgId());
        this.orderPayNoService.save(orderPayNo);

        if (ArrayUtils.isNotEmpty(orderPayItems)) {
            this.orderPayItemService.saveBatch(orderPayItems);
        }
        if (ArrayUtils.isNotEmpty(payMonies)) {
            this.orderPayMoneyService.saveBatch(payMonies);
        }

        OrderBase orderBase = this.orderBaseService.queryByOrderId(orderId);
        orderBase.setHousesId(feeData.getHousesId());
        orderBase.setDecorateAddress(feeData.getAddress());

        this.updatePayed(orderBase);

        CustBase custBase = this.custBaseService.queryByCustId(orderBase.getCustId());
        if (StringUtils.isNotBlank(feeData.getCustName())) {
            custBase.setCustName(feeData.getCustName());
            this.custBaseService.updateById(custBase);
        }
    }

    /**
     * 更新主台帐及费用表的实收信息
     * @param orderBase
     */
    private void updatePayed(OrderBase orderBase) {
        String orderType = orderBase.getType();
        Map<String, Long> payCache = new HashMap<>();
        Long orderId = orderBase.getOrderId();
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();

        List<OrderPayItem> payItems = this.orderPayItemService.queryByOrderId(orderId);

        if (ArrayUtils.isEmpty(payItems)) {
            return;
        }

        Map<String, Long> feeType = new HashMap<>();
        payItems.forEach(payItem -> {
            //查看付款项与费用类型的关系
            FeePayRelCfg feePayRelCfg = this.feePayRelCfgService.getByPayItemId(payItem.getPayItemId());
            if (feePayRelCfg != null) {
                Long feeItemId = feePayRelCfg.getFeeItemId();
                FeeItemCfg feeItemCfg = this.feeItemCfgService.getFeeItem(feeItemId);
                if (feeItemCfg != null) {
                    String type = feeItemCfg.getType();
                    String key = type;

                    Boolean isPeriod = feeItemCfg.getIsPeriod();
                    if (isPeriod) {
                        key = type + "," +payItem.getPeriods();
                    }
                    if (!feeType.containsKey(key)) {
                        feeType.put(key, new Long(0L));
                    }

                    Long payed = feeType.get(key);
                    payed += payItem.getFee();
                    feeType.put(key, payed);
                }
            }
        });
        feeType.forEach((key, value) -> {
            OrderFee orderFee = null;
            String type = null;
            Integer period = null;
            if (StringUtils.indexOf(key, ",") > 0) {
                //有分期信息
                String[] keyArray = key.split(",");
                //费用类型
                type = keyArray[0];
                //分期期数
                period = Integer.parseInt(keyArray[1]);

                if (period == 1) {
                    payCache.put("FIRST", value);
                } else if (period == 2 && StringUtils.equals(OrderConst.ORDER_TYPE_HOME, orderType)) {
                    payCache.put("SECOND", value);
                } else if (period == 2 && StringUtils.equals(OrderConst.ORDER_TYPE_WOOD, orderType)) {
                    payCache.put("SETTLEMENT", value);
                } else if (period == 3) {
                    payCache.put("SETTLEMENT", value);
                }
                orderFee = this.orderFeeService.getByOrderIdTypePeriod(orderId, type, period);
            } else {
                type = key;
                if (StringUtils.equals("1", type)) {
                    payCache.put("DESIGN", value);
                } else if (StringUtils.equals("3", type)) {
                    payCache.put("CABINET", value);
                } else if (StringUtils.equals("4", type)) {
                    payCache.put("MATERIAL", value);
                }
                orderFee = this.orderFeeService.getByOrderIdTypePeriod(orderId, key, null);
            }

            LocalDateTime now = RequestTimeHolder.getRequestTime();
            LocalDateTime forever = TimeUtils.getForeverTime();
            if (orderFee != null) {
                orderFee.setPay(value);
                this.orderFeeService.updateById(orderFee);
            } else {
                Long orgId = WebContextUtils.getUserContext().getOrgId();
                Long feeNo = this.dualService.nextval(FeeNoCycleSeq.class);
                orderFee = new OrderFee();
                orderFee.setFeeEmployeeId(employeeId);
                orderFee.setOrgId(orgId);
                orderFee.setTotalFee(value);
                orderFee.setNeedPay(value);
                orderFee.setPeriods(period);
                orderFee.setType(type);
                orderFee.setFeeNo(feeNo);
                orderFee.setStartDate(now);
                orderFee.setEndDate(forever);
                orderFee.setOrderId(orderId);
                this.orderFeeService.save(orderFee);
            }
        });

        if (payCache.size() > 0) {
            Long first = payCache.get("FIRST");
            if (first != null) {
                orderBase.setContractPay(first);
            } else {
                first = 0L;
            }

            Long second = payCache.get("SECOND");
            if (second != null) {
                orderBase.setSecondContractPay(second);
            } else {
                second = 0L;
            }

            Long settlement = payCache.get("SETTLEMENT");
            if (settlement != null) {
                orderBase.setSettlementPay(settlement);
            } else {
                settlement = 0L;
            }

            Long design = payCache.get("DESIGN");
            if (design != null) {
                orderBase.setDesignPay(design);
            } else {
                design = 0L;
            }

            Long cabinet = payCache.get("CABINET");
            if (cabinet != null) {
                orderBase.setCabinetPay(cabinet);
            } else {
                cabinet = 0L;
            }

            Long material = payCache.get("MATERIAL");
            if (material != null) {
                orderBase.setMaterialPay(material);
            } else {
                material = 0L;
            }

            Long total = first + second + settlement + design + cabinet + material;
            orderBase.setTotalPay(total);

            this.orderBaseService.updateById(orderBase);
        }
    }

    /**
     * 更改收费项目或者付款方式
     *
     * @param feeData
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
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
     *
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
                root.setValue("pay_" + payItemCfg.getId());
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
     *
     * @param root
     * @param payItemCfgs
     * @return
     */
    private void buildPayItemChildren(CascadeDTO root, List<PayItemCfg> payItemCfgs) {
        List<CascadeDTO<PayItemCfg>> children = new ArrayList<>();
        for (PayItemCfg payItemCfg : payItemCfgs) {
            if (StringUtils.equals("pay_" + payItemCfg.getParentPayItemId(), root.getValue())) {
                CascadeDTO<PayItemCfg> child = new CascadeDTO<>();
                child.setLabel(payItemCfg.getName());
                child.setValue("pay_" + payItemCfg.getId());
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
     *
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
                    result.setValue("period_" + period);
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
     *
     * @return
     */
    @Override
    public IPage<CustOrderInfoDTO> queryCustOrderInfos(CustOrderQueryDTO queryCondition, Page<CustOrderQueryDTO> page) {
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        QueryWrapper<CustOrderQueryDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply(" b.cust_id = a.cust_id ");
        queryWrapper.like(StringUtils.isNotEmpty(queryCondition.getCustName()), "b.cust_name", queryCondition.getCustName());
        queryWrapper.eq(StringUtils.isNotEmpty(queryCondition.getSex()), "b.sex", queryCondition.getSex());
        queryWrapper.likeRight(StringUtils.isNotEmpty(queryCondition.getMobileNo()), "b.mobile_no", queryCondition.getMobileNo());
        queryWrapper.eq(StringUtils.isNotEmpty(queryCondition.getOrderStatus()), "a.status", queryCondition.getOrderStatus());
        queryWrapper.eq(queryCondition.getHousesId() != null, "a.housesId", queryCondition.getHousesId());
        queryWrapper.eq(queryCondition.getCustNo() != null, "b.cust_no", queryCondition.getCustNo());
        //排除售后，订单关闭的状态
        queryWrapper.notIn("a.status", "32", "33", "100");
        queryWrapper.exists("select 1 from order_worker w where w.order_id = a.order_id and w.end_date > now() and employee_id = " + employeeId);
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
     * 查询财务待办任务
     *
     * @return
     */
    @Override
    public List<FinancePendingTaskDTO> queryFinancePendingTask() {
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        //todo 检查有没有审核权限
        List<FinancePendingOrderDTO> financeOrders = this.orderBaseMapper.queryFinancePendingOrders(employeeId, "0,2");
        if (ArrayUtils.isEmpty(financeOrders)) {
            return null;
        }

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

    /**
     * 查询订单客户付款信息
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderPayInfoDTO> queryPayInfoByOrderId(Long orderId) {
        List<OrderPayNo> orderPayNos = this.orderPayNoService.queryByOrderId(orderId);
        if (ArrayUtils.isEmpty(orderPayNos)) {
            return null;
        }

        List<OrderPayInfoDTO> orderPayInfos = new ArrayList<>();
        for (OrderPayNo orderPayNo : orderPayNos) {
            OrderPayInfoDTO orderPayInfo = new OrderPayInfoDTO();
            orderPayInfo.setPayDate(orderPayNo.getPayDate());

            Long employeeId = orderPayNo.getPayEmployeeId();
            if (employeeId != null) {
                EmployeeDO employeeDO = SpringContextUtils.getBean(EmployeeDO.class, employeeId);
                orderPayInfo.setEmployeeName(employeeDO.getEmployee().getName());
            }

            Long orgId = orderPayNo.getOrgId();
            if (orgId != null) {
                OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
                if (orgDO.getOrg() != null) {
                    Org shop = orgDO.getBelongShop();
                    if (shop != null) {
                        orderPayInfo.setShopName(shop.getName());
                    }
                }
            }

            Long auditEmployeeId = orderPayNo.getAuditEmployeeId();
            if (auditEmployeeId != null) {
                EmployeeDO employeeDO = SpringContextUtils.getBean(EmployeeDO.class, auditEmployeeId);
                orderPayInfo.setAuditEmployeeName(employeeDO.getEmployee().getName());
            }

            orderPayInfo.setAuditStatusName(this.staticDataService.getCodeName("AUDIT_STATUS", orderPayNo.getAuditStatus()));

            if (orderPayNo.getTotalMoney() != null) {
                orderPayInfo.setTotalMoney(orderPayNo.getTotalMoney().doubleValue() / 100);
            } else {
                orderPayInfo.setTotalMoney(0d);
            }

            List<OrderPayItem> orderPayItems = this.orderPayItemService.queryByOrderIdPayNo(orderId, orderPayNo.getPayNo());
            if (ArrayUtils.isNotEmpty(orderPayItems)) {
                List<OrderPayItemInfoDTO> orderPayItemInfos = new ArrayList<>();
                for (OrderPayItem orderPayItem : orderPayItems) {
                    OrderPayItemInfoDTO orderPayItemInfo = new OrderPayItemInfoDTO();
                    Long payItemId = orderPayItem.getPayItemId();
                    PayItemCfg payItemCfg = this.payItemCfgService.getPayItem(payItemId);
                    String payItemName = payItemCfg.getName();

                    Long parentPayItemId = orderPayItem.getParentPayItemId();
                    if (parentPayItemId != null && parentPayItemId != -1) {
                        PayItemCfg parentPayItemCfg = this.payItemCfgService.getPayItem(parentPayItemId);
                        payItemName = parentPayItemCfg.getName() + "-" + payItemName;
                    }

                    Integer period = orderPayItem.getPeriods();
                    if (period != null) {
                        payItemName += "-" + this.staticDataService.getCodeName("PAY_PERIODS", period + "");
                    }
                    orderPayItemInfo.setPayItemName(payItemName);

                    Long money = orderPayItem.getFee();
                    if (money != null) {
                        orderPayItemInfo.setMoney(money.doubleValue() / 100);
                    } else {
                        orderPayItemInfo.setMoney(0d);
                    }
                    orderPayItemInfos.add(orderPayItemInfo);
                }
                orderPayInfo.setPayItems(orderPayItemInfos);
            }

            List<OrderPayMoney> orderPayMonies = this.orderPayMoneyService.queryByOrderIdPayNo(orderId, orderPayNo.getPayNo());
            if (ArrayUtils.isNotEmpty(orderPayMonies)) {
                List<OrderPayMoneyInfoDTO> orderPayMoneyInfos = new ArrayList<>();
                for (OrderPayMoney orderPayMoney : orderPayMonies) {
                    OrderPayMoneyInfoDTO orderPayMoneyInfo = new OrderPayMoneyInfoDTO();
                    orderPayMoneyInfo.setPaymentName(this.staticDataService.getCodeName("PAYMENT_TYPE", orderPayMoney.getPaymentType()));
                    Long money = orderPayMoney.getMoney();
                    if (money != null) {
                        orderPayMoneyInfo.setMoney(money.doubleValue() / 100);
                    } else {
                        orderPayMoneyInfo.setMoney(0d);
                    }
                    orderPayMoneyInfo.setPaymentTypeName(this.staticDataService.getCodeName("FINANCE_ACCT_TYPE", orderPayMoney.getPaymentType()));
                    orderPayMoneyInfos.add(orderPayMoneyInfo);
                }
                orderPayInfo.setPayMonies(orderPayMoneyInfos);
            }

            orderPayInfos.add(orderPayInfo);
        }
        return orderPayInfos;
    }

    /**
     * 初始化非主营收费组件
     *
     * @return
     */
    @Override
    public CollectionComponentDTO initCollectionComponent(Long payNo) {
        CollectionComponentDTO componentData = new CollectionComponentDTO();
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
        componentData.setAuditStatus("");

        List<CollectionItemCfg> collectionItemCfgs = this.collectionItemCfgService.queryPlusCollectionyItems();
        List<CascadeDTO<CollectionItemCfg>> collectionItems = this.buildPayItemCollectionCascade(collectionItemCfgs);
        if (ArrayUtils.isNotEmpty(collectionItems)) {
            componentData.setCollectionItemOption(collectionItems);
        }

        if (payNo != null&&payNo !=0L) {
            NormalPayNo normalPayNo = this.normalPayNoService.getByPayNo(payNo);
            if (normalPayNo != null) {
                Long totalMoney = normalPayNo.getTotalMoney();
                if (totalMoney != null) {
                    //存储用分为单位，到界面上转换成元
                    componentData.setNeedPay(totalMoney.doubleValue() / 100);
                }
                componentData.setPayDate(normalPayNo.getPayDate());
                componentData.setAuditStatus(normalPayNo.getAuditStatus());
            }

            List<NormalPayItem> payItems = this.normalPayItemService.queryByPayNo(payNo);
            if (ArrayUtils.isNotEmpty(payItems)) {
                List<NormalPayItemDTO> normalPayItemDTOS = new ArrayList<>();
                for (NormalPayItem payItem : payItems) {
                    NormalPayItemDTO normalPayItemDTO = new NormalPayItemDTO();
                    String payItemId = payItem.getPayItemId().toString();
                    Long projectId = payItem.getProject();
                    normalPayItemDTO.setPayItemId("pay_" + payItem.getParentPayItemId());
                    normalPayItemDTO.setSubjectId("pay_" + payItem.getPayItemId());
                    normalPayItemDTO.setProjectId("pay_" + projectId);
                    normalPayItemDTO.setMoney(payItem.getFee().doubleValue() / 100);

                    String payItemName = this.collectionItemCfgService.getFeeItem(payItem.getParentPayItemId()).getName();
                    String subjectName = this.collectionItemCfgService.getFeeItem(payItem.getPayItemId()).getName();
                    String projectName = "";
                    //根据不同的收费小类信息区分项目信息
                    //查询品牌信息
                    if (StringUtils.equals("12", payItemId)) {
                        SupplierBrand supplierBrands = this.supplierBrandService.getSupplierBrand(projectId);
                        projectName = supplierBrands.getName();
                    }
                    //查询工人信息
                    if (StringUtils.equals("16", payItemId) || StringUtils.equals("25", payItemId) || StringUtils.equals("28", payItemId) || StringUtils.equals("33", payItemId) || StringUtils.equals("34", payItemId)) {
                        Decorator decorators = this.decoratorService.getDecorator(projectId);
                        projectName = decorators.getName();
                    }

                    //查询用户信息
                    if (StringUtils.equals("20", payItemId)) {
                        Employee employees = this.employeeService.queryByUserId(projectId);
                        projectName = employees.getName();
                    }
                    //查询公司信息
                    if (StringUtils.equals("19", payItemId)) {
                        Enterprise enterprises = this.enterpriseService.getEnterpriseId(projectId);
                        projectName = enterprises.getName();
                    }
                    //查询门店信息
                    if (StringUtils.equals("13", payItemId) || StringUtils.equals("14", payItemId) || StringUtils.equals("15", payItemId) || StringUtils.equals("18", payItemId)
                            || StringUtils.equals("21", payItemId) || StringUtils.equals("22", payItemId) || StringUtils.equals("23", payItemId) || StringUtils.equals("26", payItemId)
                            || StringUtils.equals("27", payItemId) || StringUtils.equals("29", payItemId) || StringUtils.equals("30", payItemId) || StringUtils.equals("31", payItemId)
                            || StringUtils.equals("32", payItemId) || StringUtils.equals("35", payItemId) || StringUtils.equals("36", payItemId)) {
                        Org orgs = this.orgService.queryByOrgId(projectId);
                        projectName = orgs.getName();

                    }

                    normalPayItemDTO.setPayItemName(payItemName);
                    normalPayItemDTO.setSubjectName(subjectName);
                    normalPayItemDTO.setProjectName(projectName);

                    normalPayItemDTOS.add(normalPayItemDTO);
                }
                componentData.setPayItems(normalPayItemDTOS);
            }

            List<NormalPayMoney> payMonies = this.normalPayMoneyService.queryByPayNo(payNo);
            if (ArrayUtils.isNotEmpty(payMonies)) {
                for (NormalPayMoney payMoney : payMonies) {
                    for (PaymentDTO payment : payments) {
                        if (StringUtils.equals(payment.getPaymentType(), payMoney.getPaymentType())) {
                            payment.setMoney(payMoney.getMoney().doubleValue() / 100);
                            break;
                        }
                    }
                }
            }

        }

        return componentData;
    }


    /**
     * sunxin
     * 构建选项树
     *
     * @return
     */
    private List<CascadeDTO<CollectionItemCfg>> buildPayItemCollectionCascade(List<CollectionItemCfg> collectionItemCfgs) {
        if (ArrayUtils.isEmpty(collectionItemCfgs)) {
            return null;
        }
        List<CascadeDTO<CollectionItemCfg>> roots = new ArrayList<>();
        for (CollectionItemCfg collectionItemCfg : collectionItemCfgs) {
            if (collectionItemCfg.getParentCollectionItemId().equals(-1L)) {
                CascadeDTO<CollectionItemCfg> root = new CascadeDTO<>();
                root.setLabel(collectionItemCfg.getName());
                root.setValue("pay_" + collectionItemCfg.getId());
                root.setSelf(collectionItemCfg);
                roots.add(root);
            }
        }

        if (ArrayUtils.isNotEmpty(roots)) {
            for (CascadeDTO<CollectionItemCfg> root : roots) {
                this.buildPayItemCollectionChildren(root, collectionItemCfgs);
            }
        }

        return roots;
    }

    /**
     * sunxin
     * 构建子孙节点
     *
     * @param root
     * @param collectionItemCfgs
     * @return
     */
    private void buildPayItemCollectionChildren(CascadeDTO root, List<CollectionItemCfg> collectionItemCfgs) {
        List<CascadeDTO<CollectionItemCfg>> childrens = new ArrayList<>();
        for (CollectionItemCfg collectionItemCfg : collectionItemCfgs) {
            if (StringUtils.equals("pay_" + collectionItemCfg.getParentCollectionItemId(), root.getValue())) {
                CascadeDTO<CollectionItemCfg> child = new CascadeDTO<>();
                child.setLabel(collectionItemCfg.getName());
                child.setValue("pay_" + collectionItemCfg.getId());
                child.setSelf(collectionItemCfg);
                childrens.add(child);

                buildPayItemCollectionChildren(child, collectionItemCfgs);
            }
        }
        if (ArrayUtils.isNotEmpty(childrens)) {
            root.setChildren(childrens);
            for (CascadeDTO<CollectionItemCfg> children : childrens) {
                this.buildCollectionGrandChildren(children);
            }
        }
    }

    /**
     * sunxin
     * 构建子孙节点
     *
     * @param children
     * @return
     */
    private void buildCollectionGrandChildren(CascadeDTO children) {
        //根据不同的科目类别获取不同的第三级选择
        String id = children.getValue();
        //查询品牌信息
        if (StringUtils.equals("pay_12", id)) {
            List<CascadeDTO<SupplierBrand>> grandChildrens = new ArrayList<>();
            List<SupplierBrand> supplierBrands = this.supplierBrandService.queryAllInfo();
            for (SupplierBrand supplierBrand : supplierBrands) {
                CascadeDTO<SupplierBrand> child = new CascadeDTO<>();
                child.setLabel(supplierBrand.getName());
                child.setValue("pay_" + supplierBrand.getId());
                child.setSelf(supplierBrand);
                grandChildrens.add(child);
            }
            if (ArrayUtils.isNotEmpty(grandChildrens)) {
                children.setChildren(grandChildrens);
            }
        }
        //查询工人信息
        if (StringUtils.equals("pay_16", id) || StringUtils.equals("pay_25", id) || StringUtils.equals("pay_28", id) || StringUtils.equals("pay_33", id) || StringUtils.equals("pay_34", id)) {
            List<CascadeDTO<Decorator>> grandChildrens = new ArrayList<>();
            List<Decorator> decorators = this.decoratorService.queryAllInfo();
            for (Decorator decorator : decorators) {
                CascadeDTO<Decorator> child = new CascadeDTO<>();
                child.setLabel(decorator.getName());
                child.setValue("pay_" + decorator.getDecoratorId());
                child.setSelf(decorator);
                grandChildrens.add(child);
            }
            if (ArrayUtils.isNotEmpty(grandChildrens)) {
                children.setChildren(grandChildrens);
            }
        }

        //查询用户信息
        if (StringUtils.equals("pay_20", id)) {
            List<Employee> employees = this.employeeService.loadEmployee();
            List<CascadeDTO<Employee>> grandChildrens = new ArrayList<>();
            for (Employee employee : employees) {
                CascadeDTO<Employee> child = new CascadeDTO<>();
                child.setLabel(employee.getName());
                child.setValue("pay_" + employee.getUserId());
                child.setSelf(employee);
                grandChildrens.add(child);
            }
            if (ArrayUtils.isNotEmpty(grandChildrens)) {
                children.setChildren(grandChildrens);
            }
        }
        //查询公司信息
        if (StringUtils.equals("pay_19", id)) {
            List<Enterprise> enterprises = this.enterpriseService.queryAll();
            List<CascadeDTO<Enterprise>> grandChildrens = new ArrayList<>();
            for (Enterprise enterprise : enterprises) {
                CascadeDTO<Enterprise> child = new CascadeDTO<>();
                child.setLabel(enterprise.getName());
                child.setValue("pay_" + enterprise.getEnterpriseId());
                child.setSelf(enterprise);
                grandChildrens.add(child);
            }
            if (ArrayUtils.isNotEmpty(grandChildrens)) {
                children.setChildren(grandChildrens);
            }
        }
        //查询门店信息
        if (StringUtils.equals("pay_13", id) || StringUtils.equals("pay_14", id) || StringUtils.equals("pay_15", id) || StringUtils.equals("pay_18", id)
                || StringUtils.equals("pay_21", id) || StringUtils.equals("pay_22", id) || StringUtils.equals("pay_23", id) || StringUtils.equals("pay_26", id)
                || StringUtils.equals("pay_27", id) || StringUtils.equals("pay_29", id) || StringUtils.equals("pay_30", id) || StringUtils.equals("pay_31", id)
                || StringUtils.equals("pay_32", id) || StringUtils.equals("pay_35", id) || StringUtils.equals("pay_36", id)) {
            List<Org> orgs = this.orgService.listByType("4");
            List<CascadeDTO<Org>> grandChildrens = new ArrayList<>();
            for (Org org : orgs) {
                CascadeDTO<Org> child = new CascadeDTO<>();
                child.setLabel(org.getName());
                child.setValue("pay_" + org.getOrgId());
                child.setSelf(org);
                grandChildrens.add(child);
            }
            if (ArrayUtils.isNotEmpty(grandChildrens)) {
                children.setChildren(grandChildrens);
            }
        }
    }

    /**
     * 非主营业务收款
     *
     * @param feeData
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void nonCollectFee(NonCollectFeeDTO feeData) {

        List<NormalPayItemDTO> payItems = feeData.getPayItems();
        Long payNo = feeData.getPayNo();

            payNo = this.dualService.nextval(PayNoCycleSeq.class);
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
            List<NormalPayItem> normalPayItems = new ArrayList<>();

            if (ArrayUtils.isNotEmpty(payItems)) {
                for (NormalPayItemDTO payItem : payItems) {
                    Double money = payItem.getMoney();
                    if (money == null || money <= 0.001) {
                        continue;
                    }

                    Long fee = new Long(Math.round(money * 100));

                    Long payItemId = new Long(payItem.getPayItemId());
                    Long subjectId = new Long(payItem.getSubjectId());
                    Long projectId = new Long(payItem.getProjectId());
                    CollectionItemCfg payItemCfg = this.collectionItemCfgService.getFeeItem(subjectId);
                    if (payItemCfg == null) {
                        throw new OrderException(OrderException.OrderExceptionEnum.PAY_ITEM_NOT_FOUND, payItem.getSubjectId());
                    }
                    NormalPayItem normalPayItem = new NormalPayItem();
                    normalPayItem.setPayItemId(subjectId);
                    normalPayItem.setParentPayItemId(payItemId);
                    normalPayItem.setProject(projectId);
                    normalPayItem.setFee(fee);
                    normalPayItem.setPayNo(payNo);
                    normalPayItem.setStartDate(now);
                    normalPayItem.setEndDate(forever);
                    normalPayItems.add(normalPayItem);
                    payItemTotal += fee;
                }
            }
            List<PaymentDTO> payments = feeData.getPayments();
            List<NormalPayMoney> payMonies = new ArrayList<>();
            Long totalMoney = 0L;

            if (ArrayUtils.isNotEmpty(payments)) {
                for (PaymentDTO payment : payments) {
                    NormalPayMoney payMoney = new NormalPayMoney();
                    payMoney.setPaymentType(payment.getPaymentType());

                    Double money = payment.getMoney();
                    if (money == null || money <= 0.001) {
                        continue;
                    }
                    Long fee = new Long(Math.round(money * 100));
                    payMoney.setMoney(fee);
                    payMoney.setPayNo(payNo);
                    payMoney.setStartDate(now);
                    payMoney.setEndDate(forever);
                    payMonies.add(payMoney);
                    totalMoney += fee;
                }
            }
            if (!payItemTotal.equals(needPay)) {
                throw new OrderException(OrderException.OrderExceptionEnum.PAY_MUST_EQUAL_PAYITEM);
            }
            if (!payItemTotal.equals(totalMoney)) {
                throw new OrderException(OrderException.OrderExceptionEnum.PAY_MUST_EQUAL_PAYITEM);
            }

            NormalPayNo normalPayNo = new NormalPayNo();
            normalPayNo.setPayDate(payDate);
            normalPayNo.setPayNo(payNo);
            //待审核状态
            normalPayNo.setAuditStatus(OrderConst.AUDIT_STATUS_INIT);
            normalPayNo.setStartDate(now);
            normalPayNo.setEndDate(forever);
            normalPayNo.setTotalMoney(needPay);
            normalPayNo.setPayEmployeeId(employeeId);
            normalPayNo.setOrgId(WebContextUtils.getUserContext().getOrgId());
            normalPayNo.setAuditComment(feeData.getAuditRemark());
            this.normalPayNoService.save(normalPayNo);

            if (ArrayUtils.isNotEmpty(normalPayItems)) {
                this.normalPayItemService.saveBatch(normalPayItems);
            }
            if (ArrayUtils.isNotEmpty(payMonies)) {
                this.normalPayMoneyService.saveBatch(payMonies);
            }

    }

    /**
     * 查询非主营收费信息
     *
     * @param queryCondition
     * @return
     */
    @Override
    public List<NonCollectFeeDTO> queryPayInfoByCond(NonCollectFeeQueryDTO queryCondition) {

        QueryWrapper<NormalPayNo> queryWrapper = new QueryWrapper<>();
        if(null!=queryCondition.getStartDate()&&null!=queryCondition.getEndDate()){
            queryWrapper.between("pay_date", queryCondition.getStartDate(), queryCondition.getEndDate());
        }
        queryWrapper.eq(StringUtils.isNotEmpty(queryCondition.getAuditStatus()), "audit_status", queryCondition.getAuditStatus());
        queryWrapper.eq(queryCondition.getEmployeeId() != null, "pay_employee_id", queryCondition.getEmployeeId());
        queryWrapper.ge("end_date", RequestTimeHolder.getRequestTime());
        List<NormalPayNo> normalPayNos = this.normalPayNoMapper.queryPayNoInfo(queryWrapper);

//        if (ArrayUtils.isEmpty(normalPayNos)) {
//            return null;
//        }

        List<NonCollectFeeDTO> normalPayInfos = new ArrayList<>();
        for (NormalPayNo normalPayNo : normalPayNos) {
            NonCollectFeeDTO normalPayInfo = new NonCollectFeeDTO();
            normalPayInfo.setPayDate(normalPayNo.getPayDate());
            normalPayInfo.setPayNo(normalPayNo.getPayNo());
            Long employeeId = normalPayNo.getPayEmployeeId();
            if (employeeId != null) {
                EmployeeDO employeeDO = SpringContextUtils.getBean(EmployeeDO.class, employeeId);
                normalPayInfo.setEmployeeName(employeeDO.getEmployee().getName());
            }
            normalPayInfo.setAuditStatusName(this.staticDataService.getCodeName("AUDIT_STATUS", normalPayNo.getAuditStatus()));


            if (normalPayNo.getTotalMoney() != null) {
                normalPayInfo.setTotalMoney(normalPayNo.getTotalMoney().doubleValue() / 100);
            } else {
                normalPayInfo.setTotalMoney(0d);
            }
            normalPayInfos.add(normalPayInfo);
        }
        return normalPayInfos;
    }

    /**
     * 更改收费项目或者付款方式
     *
     * @param feeData
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void nonCollectFeeUpdate(NonCollectFeeDTO feeData) {
        Long payNo = feeData.getPayNo();
        if (payNo == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.ARGUMENT_NOT_FOUND, "payNo");
        }

        LocalDateTime now = RequestTimeHolder.getRequestTime();
        //先终止原来的收款记录
        NormalPayNo normalPayNo = this.normalPayNoService.getByPayNo(payNo);
        if (normalPayNo != null) {
            normalPayNo.setEndDate(now);
            this.normalPayNoService.updateById(normalPayNo);
        }

        List<NormalPayItem> normalPayItems = this.normalPayItemService.queryByPayNo( payNo);
        if (ArrayUtils.isNotEmpty(normalPayItems)) {
            for (NormalPayItem normalPayItem : normalPayItems) {
                normalPayItem.setEndDate(now);
            }

            this.normalPayItemService.updateBatchById(normalPayItems);
        }

        List<NormalPayMoney> normalPayMonies = this.normalPayMoneyService.queryByPayNo(payNo);
        if (ArrayUtils.isNotEmpty(normalPayMonies)) {
            for (NormalPayMoney normalPayMoney : normalPayMonies) {
                normalPayMoney.setEndDate(now);
            }
            this.normalPayMoneyService.updateBatchById(normalPayMonies);
        }

        this.nonCollectFee(feeData);
    }

    /**
     * 状态更新
     *
     * @param feeData
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void nonCollectFeeForAudit(NonCollectFeeDTO feeData) {
        Long payNo = feeData.getPayNo();
        String auditStatus=feeData.getAuditStatus();
        if (payNo == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.ARGUMENT_NOT_FOUND, "payNo");
        }

        LocalDateTime now = RequestTimeHolder.getRequestTime();
        //更新payNo表记录
        NormalPayNo normalPayNo = this.normalPayNoService.getByPayNo(payNo);
        if (normalPayNo != null) {
            normalPayNo.setAuditStatus(auditStatus);
            normalPayNo.setAuditEmployeeId(WebContextUtils.getUserContext().getEmployeeId());
            this.normalPayNoService.updateById(normalPayNo);
        }
    }

    @Override
    public IPage<FinanceOrderTaskDTO> queryFinanceOrderTasks(FinanceOrderTaskQueryDTO condition) {
        QueryWrapper<FinanceOrderTaskQueryDTO> wrapper = new QueryWrapper<>();
        wrapper.apply("b.cust_id = a.cust_id ");
        wrapper.apply("c.order_id = a.order_id");
        wrapper.like(StringUtils.isNotBlank(condition.getCustName()), "b.cust_name", condition.getCustName());
        wrapper.eq(StringUtils.isNotBlank(condition.getAuditStatus()), "c.audit_status", condition.getAuditStatus());
        wrapper.eq(condition.getHousesId() != null, "a.houses_id", condition.getHousesId());
        wrapper.eq(StringUtils.isNotBlank(condition.getMobileNo()), "b.mobile_no", condition.getMobileNo());

        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        wrapper.exists("select 1 from order_worker w where w.order_id = a.order_id and w.end_date > now() and employee_id = " + employeeId);
        wrapper.orderByAsc("a.status", "a.create_time");

        IPage<FinanceOrderTaskQueryDTO> page = new Page<>(condition.getPage(), condition.getLimit());
        IPage<FinanceOrderTaskDTO> pageTasks = this.orderBaseMapper.queryFinanceOrderTaskInConsole(page, wrapper);

        List<FinanceOrderTaskDTO> tasks = pageTasks.getRecords();
        if (ArrayUtils.isEmpty(tasks)) {
            return pageTasks;
        }

        tasks.forEach(task -> {
            task.setHouseLayoutName(this.staticDataService.getCodeName("HOUSE_MODE", task.getHouseLayout()));
            task.setTypeName(this.staticDataService.getCodeName("ORDER_TYPE", task.getType()));
            task.setAuditStatusName(this.staticDataService.getCodeName("AUDIT_STATUS", task.getAuditStatus()));
            OrderStatusCfg statusCfg = this.orderStatusCfgService.getCfgByTypeStatus(task.getType(), task.getStatus());
            if (statusCfg != null) {
                task.setStatusName(statusCfg.getStatusName());
            }

            if (task.getHousesId() != null) {
                Houses house = this.housesService.getHouse(task.getHousesId());
                if (house != null) {
                    task.setHousesName(house.getName());
                }
            }
        });
        return pageTasks;
    }
}
