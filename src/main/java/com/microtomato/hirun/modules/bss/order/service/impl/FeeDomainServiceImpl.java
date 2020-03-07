package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.framework.mybatis.sequence.impl.FeeNoCycleSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.config.entity.consts.FeeConst;
import com.microtomato.hirun.modules.bss.config.entity.po.FeeItemCfg;
import com.microtomato.hirun.modules.bss.config.entity.po.FeePayRelCfg;
import com.microtomato.hirun.modules.bss.config.service.IFeeItemCfgService;
import com.microtomato.hirun.modules.bss.config.service.IFeePayRelCfgService;
import com.microtomato.hirun.modules.bss.order.entity.dto.FeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFeeItem;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayItem;
import com.microtomato.hirun.modules.bss.order.exception.OrderException;
import com.microtomato.hirun.modules.bss.order.service.IFeeDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderFeeItemService;
import com.microtomato.hirun.modules.bss.order.service.IOrderFeeService;
import com.microtomato.hirun.modules.bss.order.service.IOrderPayItemService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: hirun-helper
 * @description: 费用领域服务实现类
 * @author: jinnian
 * @create: 2020-03-02 21:11
 **/
@Service
@Slf4j
public class FeeDomainServiceImpl implements IFeeDomainService {

    @Autowired
    private IOrderFeeService orderFeeService;

    @Autowired
    private IOrderFeeItemService orderFeeItemService;

    @Autowired
    private IFeeItemCfgService feeItemCfgService;

    @Autowired
    private IFeePayRelCfgService feePayRelCfgService;

    @Autowired
    private IOrderPayItemService orderPayItemService;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IDualService dualService;

    /**
     * 创建订单费用信息,如果之前存在同类型的，则终止原来的费用记录
     * @param orderId
     * @param type 期数 1-设计费 2-工程款
     * @param period 期数 1-首期 2-二期款 3-结算款
     * @param fees 费用项数据
     */
    @Override
    public void createOrderFee(Long orderId, String type, Integer period, List<FeeDTO> fees) {
        if (ArrayUtils.isEmpty(fees)) {
            return;
        }
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        Long orgId = WebContextUtils.getUserContext().getOrgId();
        Long feeNo = dualService.nextval(FeeNoCycleSeq.class);
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        LocalDateTime forever = TimeUtils.getForeverTime();

        long totalFee = 0L;
        List<OrderFeeItem> feeItems = new ArrayList<>();
        for (FeeDTO fee : fees) {
            //创建费用项信息
            OrderFeeItem feeItem = new OrderFeeItem();
            Long feeItemId = fee.getFeeItemId();

            FeeItemCfg feeItemCfg = this.feeItemCfgService.getFeeItem(feeItemId);
            if (feeItemCfg == null) {
                throw new OrderException(OrderException.OrderExceptionEnum.FEE_ITEM_NOT_FOUND, String.valueOf(feeItemId));
            }

            long money = (long)(fee.getMoney()*100);

            if (FeeConst.FEE_DIRECTION_PLUS.equals(feeItemCfg.getDirection())) {
                totalFee += money;
                feeItem.setFee(money);
            } else {
                totalFee = totalFee - money;
                feeItem.setFee(money*-1);
            }

            feeItem.setOrderId(orderId);
            feeItem.setFeeItemId(feeItemId);
            feeItem.setParentFeeItemId(feeItemCfg.getParentFeeItemId());
            feeItem.setType(type);
            feeItem.setFeeNo(feeNo);
            feeItem.setPeriods(period);
            feeItem.setFeeEmployeeId(employeeId);
            feeItem.setOrgId(orgId);
            feeItem.setStartDate(now);
            feeItem.setEndDate(forever);
            feeItems.add(feeItem);
        }

        OrderFee orderFee = new OrderFee();
        orderFee.setFeeEmployeeId(employeeId);
        orderFee.setOrgId(orgId);
        orderFee.setTotalFee(totalFee);
        orderFee.setPeriods(period);
        orderFee.setType(type);
        orderFee.setFeeNo(feeNo);
        orderFee.setStartDate(now);
        orderFee.setEndDate(forever);
        orderFee.setOrderId(orderId);

        Long needPay = this.getNeedPay(totalFee, orderId, type, period);
        orderFee.setNeedPay(needPay);

        OrderFee oldOrderFee = this.orderFeeService.getByOrderIdTypePeriod(orderId, type, period);
        if (oldOrderFee != null) {
            oldOrderFee.setEndDate(now);
            this.orderFeeService.updateById(oldOrderFee);
        }
        this.orderFeeService.save(orderFee);

        List<OrderFeeItem> oldFeeItems = this.orderFeeItemService.queryByOrderIdTypePeriod(orderId, type, period);
        if (ArrayUtils.isNotEmpty(oldFeeItems)) {
            for (OrderFeeItem orderFeeItem : oldFeeItems) {
                orderFeeItem.setEndDate(now);
            }
            this.orderFeeItemService.updateBatchById(oldFeeItems);
        }
        this.orderFeeItemService.saveBatch(feeItems);
    }

    /**
     * 查询某类型费用已付款信息，如果要查询某类型全部的已付款，period字段传null
     * @param orderId
     * @param type
     * @param period
     * @return
     */
    @Override
    public Long getPayedMoney(Long orderId, String type, Integer period) {
        List<FeeItemCfg> leafFeeItems = this.feeItemCfgService.queryLeafByType(type);

        if (ArrayUtils.isEmpty(leafFeeItems)) {
            return 0L;
        }

        List<Long> feeItemIds = new ArrayList<>();
        for (FeeItemCfg leafFeeItem : leafFeeItems) {
            feeItemIds.add(leafFeeItem.getId());
        }

        List<FeePayRelCfg> feePayRelCfgs = this.feePayRelCfgService.queryByFeeItemIds(feeItemIds);

        if (ArrayUtils.isEmpty(feePayRelCfgs)) {
            return 0L;
        }

        List<Long> payItemIds = new ArrayList<>();
        for (FeePayRelCfg feePayRelCfg : feePayRelCfgs) {
            Long payItemId = feePayRelCfg.getPayItemId();
            payItemIds.add(payItemId);
        }

        payItemIds = payItemIds.stream().distinct().collect(Collectors.toList());

        if (ArrayUtils.isEmpty(payItemIds)) {
            return 0L;
        }

        List<OrderPayItem> orderPayItems = this.orderPayItemService.queryByPayItemIds(orderId, payItemIds, period);
        if (ArrayUtils.isEmpty(orderPayItems)) {
            return 0L;
        }

        long totalPay = 0L;
        for (OrderPayItem orderPayItem : orderPayItems) {
            totalPay = totalPay + orderPayItem.getFee();
        }
        return totalPay;
    }

    /**
     * 获取本次应收费用
     * @param fee
     * @param orderId
     * @param type
     * @param period
     * @return
     */
    @Override
    public Long getNeedPay(Long fee, Long orderId, String type, Integer period) {
        Long payed = this.getPayedMoney(orderId, type, period);
        return fee-payed;
    }

    /**
     * 根据订单ID获取所有费用信息
     * @param orderId
     * @return
     */
    @Override
    public List<OrderFeeInfoDTO> queryOrderFeeInfo(Long orderId) {
        List<OrderFee> orderFees = this.orderFeeService.queryByOrderId(orderId);
        if (ArrayUtils.isEmpty(orderFees)) {
            return null;
        }

        List<OrderFeeInfoDTO> orderFeeInfos = new ArrayList<OrderFeeInfoDTO>();
        for (OrderFee orderFee : orderFees) {
            OrderFeeInfoDTO orderFeeInfo = new OrderFeeInfoDTO();
            orderFeeInfo.setType(orderFee.getType());
            orderFeeInfo.setTypeName(this.staticDataService.getCodeName("ORDER_FEE_TYPE", orderFee.getType()));
            if (orderFee.getPeriods() != null) {
                orderFeeInfo.setPeriods(orderFee.getPeriods());
                orderFeeInfo.setPeriodName(this.staticDataService.getCodeName("FEE_PERIODS", orderFee.getPeriods()+""));
            } else {
                orderFeeInfo.setPeriodName("-");
            }

            orderFeeInfo.setRowKey(orderFee.getFeeNo());

            if (orderFee.getTotalFee() != null) {
                orderFeeInfo.setTotalMoney(orderFee.getTotalFee().doubleValue() / 100);
            } else {
                orderFeeInfo.setTotalMoney(0d);
            }

            List<OrderFeeItem> orderFeeItems = this.orderFeeItemService.queryByOrderIdFeeNo(orderId, orderFee.getFeeNo());
            if (ArrayUtils.isNotEmpty(orderFeeItems)) {
                List<OrderFeeInfoDTO> children = new ArrayList<>();
                long row = 0;
                for (OrderFeeItem orderFeeItem : orderFeeItems) {
                    OrderFeeInfoDTO child = new OrderFeeInfoDTO();
                    FeeItemCfg feeItemCfg = this.feeItemCfgService.getFeeItem(orderFeeItem.getFeeItemId());
                    child.setTypeName(feeItemCfg.getName());
                    if(orderFeeItem.getPeriods() != null) {
                        child.setPeriodName(this.staticDataService.getCodeName("FEE_PERIODS", orderFeeItem.getPeriods()+""));
                    }
                    if (orderFeeItem.getFee() != null) {
                        child.setTotalMoney(orderFeeItem.getFee().doubleValue() / 100);
                    } else {
                        child.setTotalMoney(0d);
                    }
                    child.setRowKey(row);
                    children.add(child);
                    row++;
                }
                orderFeeInfo.setChildren(children);
            }
            orderFeeInfos.add(orderFeeInfo);
        }
        return orderFeeInfos;
    }
}
