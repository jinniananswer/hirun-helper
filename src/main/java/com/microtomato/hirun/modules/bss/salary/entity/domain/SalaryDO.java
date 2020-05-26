package com.microtomato.hirun.modules.bss.salary.entity.domain;

import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SPELUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFeeItem;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPlaneSketch;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWorker;
import com.microtomato.hirun.modules.bss.order.service.IOrderFeeItemService;
import com.microtomato.hirun.modules.bss.order.service.IOrderFeeService;
import com.microtomato.hirun.modules.bss.order.service.IOrderPlaneSketchService;
import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerService;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryRoyaltyDetail;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryRoyaltyStrategy;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryStatusFeeMapping;
import com.microtomato.hirun.modules.bss.salary.exception.SalaryException;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryRoyaltyDetailService;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryRoyaltyStrategyService;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryStatusFeeMappingService;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 工资领域对象，主要用于计算复杂的工资提成
 * @author: jinnian
 * @create: 2020-05-17 23:50
 **/
@Slf4j
@Component
@Scope("prototype")
public class SalaryDO {

    @Autowired
    private IOrderWorkerService orderWorkerService;

    @Autowired
    private ISalaryRoyaltyStrategyService salaryRoyaltyStrategyService;

    @Autowired
    private ISalaryRoyaltyDetailService salaryRoyaltyDetailService;

    @Autowired
    private IOrderFeeService orderFeeService;

    @Autowired
    private IOrderFeeItemService orderFeeItemService;

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleService;

    @Autowired
    private IOrgService orgService;

    @Autowired
    private IOrderPlaneSketchService orderPlaneSketchService;

    @Autowired
    private ISalaryStatusFeeMappingService statusFeeMappingService;

    /**
     * 根据订单状态创建提成信息
     * @param orderId
     */
    @Async
    public void createRoyalties(Long orderId, String orderStatus) {
        //先看该订单状态对应是否有需要计算的配置，如果没有，则直接返回
        boolean isNeedCompute = this.salaryRoyaltyStrategyService.isNeedCompute(orderStatus);
        if (!isNeedCompute) {
            return;
        }

        //1.先查询订单目前有哪些参与人，只有该笔订单的参与人才能参与这笔订单的提成计算
        List<OrderWorker> workers = this.orderWorkerService.queryValidByOrderId(orderId);
        if (ArrayUtils.isEmpty(workers)) {
            return;
        }

        //2.组装订单的费用等相关信息，这些对象需要进行计算或者更细粒度的条件匹配
        //2.1 查询状态对应要捞取的参与运算的费用配置
        SalaryStatusFeeMapping statusFeeMapping = this.statusFeeMappingService.getStatusFeeMapping(orderStatus);
        if (statusFeeMapping == null) {
            return;
        }

        //2.2 根据状态对应的费用配置查询费用信息
        OrderFee orderFee = this.orderFeeService.getByOrderIdTypePeriod(orderId, statusFeeMapping.getType(), statusFeeMapping.getPeriods());
        if (orderFee == null) {
            //没有查到费用信息，无法计算
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        FeeFact feeFact = new FeeFact();
        BeanUtils.copyProperties(orderFee, feeFact);
        //2.3 如果是工程款，查询费用明细信息
        if (StringUtils.equals("2", statusFeeMapping.getType())) {
            List<OrderFeeItem> orderFeeItems = this.orderFeeItemService.queryByOrderIdFeeNo(orderId, orderFee.getFeeNo());
            if (ArrayUtils.isNotEmpty(orderFeeItems)) {
                List<FeeItemFact> feeItemFacts = new ArrayList<>();
                orderFeeItems.forEach((orderFeeItem -> {
                    FeeItemFact feeItemFact = new FeeItemFact();
                    BeanUtils.copyProperties(orderFeeItem, feeItemFact);
                    feeItemFacts.add(feeItemFact);
                }));
                feeFact.setItems(feeItemFacts);
            }
        }

        //2.4 查询订单设计费标准
        OrderPlaneSketch planeSketch = this.orderPlaneSketchService.getPlaneSketch(orderId);
        Integer designFeeStandard = 0;
        if (planeSketch != null) {
            designFeeStandard = planeSketch.getDesignFeeStandard();
            feeFact.setDesignFeeStandard(designFeeStandard);
        } else {
            throw new SalaryException(SalaryException.SalaryExceptionEnum.DESIGN_FEE_STANDARD_NOT_FOUND, String.valueOf(orderId));
        }

        //3 找到参与人对应的策略配置，再根据费用相关信息进行计算
        Integer salaryMonth = Integer.parseInt(TimeUtils.formatLocalDateTimeToString(now, TimeUtils.DATE_FMT_14));
        SPELUtils executor = new SPELUtils();
        Integer finalDesignFee = designFeeStandard;

        List<SalaryRoyaltyDetail> royaltyDetails = new ArrayList<>();
        workers.forEach(worker -> {
            List<SalaryRoyaltyStrategy> strategies = this.salaryRoyaltyStrategyService.getPriorityByEmployeeIdRoleIdOrderStatus(worker.getEmployeeId(), worker.getRoleId(), orderStatus);
            if (ArrayUtils.isEmpty(strategies)) {
                //没有找到匹配的策略，则返回
                return;
            }

            //构造匹配条件变量
            EmployeeJobRole jobRole = this.employeeJobRoleService.queryLast(worker.getEmployeeId());
            RoyaltyMatchFact matchFact = RoyaltyMatchFact.builder()
                    .employeeId(worker.getEmployeeId())
                    .jobRole(jobRole.getJobRole())
                    .jobGrade(jobRole.getJobGrade())
                    .orgId(jobRole.getOrgId())
                    .designFeeStandard(finalDesignFee)
                    .build();

            Org org = this.orgService.queryByOrgId(jobRole.getOrgId());
            matchFact.setNature(org.getNature());

            //找到完全匹配的那一条
            SalaryRoyaltyStrategy matchStrategy = null;
            for (SalaryRoyaltyStrategy strategy : strategies) {
                boolean isMatch = false;
                isMatch = executor.executeBool(strategy.getMatchCondition(), matchFact);
                if (isMatch) {
                    matchStrategy = strategy;
                    break;
                }
            }

            if (matchStrategy == null) {
                return;
            }

            //根据匹配的配置，开始进行提成金额的计算
            String formula = matchStrategy.getFormula();
            Long value = executor.executeLong(formula, feeFact);

            //todo 多人拆分的计算

            //todo 本月可提和已提的计算

            SalaryRoyaltyDetail royaltyDetail = new SalaryRoyaltyDetail();
            royaltyDetail.setEmployeeId(worker.getEmployeeId());
            royaltyDetail.setOrgId(jobRole.getOrgId());
            royaltyDetail.setJobRole(jobRole.getJobRole());
            royaltyDetail.setRoleId(worker.getRoleId());
            royaltyDetail.setSalaryMonth(salaryMonth);
            royaltyDetail.setOrderId(orderId);
            royaltyDetail.setOrderStatus(orderStatus);
            royaltyDetail.setType(matchStrategy.getType());
            royaltyDetail.setItem(matchStrategy.getItem());
            royaltyDetail.setMode(matchStrategy.getMode());
            royaltyDetail.setValue(matchStrategy.getValue());
            royaltyDetail.setTotalRoyalty(value);
            royaltyDetail.setAlreadyFetch(0L);
            royaltyDetail.setThisMonthFetch(value);
            royaltyDetail.setAuditStatus("0");
            royaltyDetail.setStartTime(now);
            royaltyDetail.setEndTime(TimeUtils.getForeverTime());
            royaltyDetails.add(royaltyDetail);
        });

        if (ArrayUtils.isNotEmpty(royaltyDetails)) {
            this.salaryRoyaltyDetailService.saveBatch(royaltyDetails);
        }
    }

    /**
     * 根据订单ID构造工资提成条件匹配对象
     * @param orderId
     */
    public RoyaltyMatchFact buildRoyaltyMatchFact(Long orderId) {
        return null;
    }

    public List<FeeFact> buildFeeFact(Long orderId) {
        return null;
    }
}
