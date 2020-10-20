package com.microtomato.hirun.modules.bss.salary.entity.domain;

import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SPELUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.SalaryRoyaltyStrategy;
import com.microtomato.hirun.modules.bss.config.service.ISalaryRoyaltyMultiSplitService;
import com.microtomato.hirun.modules.bss.config.service.ISalaryRoyaltyStrategyService;
import com.microtomato.hirun.modules.bss.config.service.ISalaryStatusFeeMappingService;
import com.microtomato.hirun.modules.bss.house.entity.po.Houses;
import com.microtomato.hirun.modules.bss.house.service.IHousesService;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerActionDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFeeItem;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPlaneSketch;
import com.microtomato.hirun.modules.bss.order.service.*;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryRoyaltyDetail;
import com.microtomato.hirun.modules.bss.salary.exception.SalaryException;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryRoyaltyDetailService;
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
import java.util.*;

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
    private IOrderWorkerActionService orderWorkerActionService;

    @Autowired
    private ISalaryRoyaltyStrategyService salaryRoyaltyStrategyService;

    @Autowired
    private ISalaryRoyaltyMultiSplitService salaryRoyaltyMultiSplitService;

    @Autowired
    private ISalaryRoyaltyDetailService salaryRoyaltyDetailService;

    @Autowired
    private IOrderFeeService orderFeeService;

    @Autowired
    private IOrderBaseService orderBaseService;

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

    @Autowired
    private IHousesService housesService;

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

        //1. 查询订单基本信息
        OrderBase orderBase = this.orderBaseService.queryByOrderId(orderId);
        if (orderBase == null) {
            return;
        }

        Long housesId = orderBase.getHousesId();
        Houses house = this.housesService.getHouse(housesId);
        String houseNature = house != null ? String.valueOf(house.getNature()) : "";


        //1.1 先查询订单目前有哪些参与人,参与人做了哪些动作，只有该笔订单的参与人才能参与这笔订单的提成计算
        List<OrderWorkerActionDTO> workerActions = this.orderWorkerActionService.queryByOrderId(orderId);
        if (ArrayUtils.isEmpty(workerActions)) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        RoyaltyComputeFact computeFact = new RoyaltyComputeFact();

        //2.4 查询订单设计费标准
        OrderPlaneSketch planeSketch = this.orderPlaneSketchService.getByOrderId(orderId);
        Integer designFeeStandard = 0;
        if (planeSketch != null) {
            designFeeStandard = planeSketch.getDesignFeeStandard() / 100;
        } else {
            throw new SalaryException(SalaryException.SalaryExceptionEnum.DESIGN_FEE_STANDARD_NOT_FOUND, String.valueOf(orderId));
        }

        Map<String, FeeFact> feeFactCache = new HashMap<>();
        FeeFact designFact = this.buildFeeFact(orderId, "1", null);
        FeeFact firstFact = this.buildFeeFact(orderId, "2", 1);
        FeeFact secondFact = this.buildFeeFact(orderId, "2", 2);
        FeeFact settlementFact = this.buildFeeFact(orderId, "2", 3);
        feeFactCache.put(orderId + "_1_null", designFact);
        feeFactCache.put(orderId + "_2_1", firstFact);
        feeFactCache.put(orderId + "_2_2", secondFact);
        feeFactCache.put(orderId + "_2_3", settlementFact);


        //3 找到参与人对应的策略配置，再根据费用相关信息进行计算
        Integer salaryMonth = Integer.parseInt(TimeUtils.formatLocalDateTimeToString(now, TimeUtils.DATE_FMT_14));
        int day = now.getDayOfMonth();
        if (day >= 25) {
            salaryMonth = Integer.parseInt(TimeUtils.formatLocalDateTimeToString(now.plusMonths(1L), TimeUtils.DATE_FMT_14));
        }

        List<SalaryRoyaltyDetail> royaltyDetails = new ArrayList<>();
        for (OrderWorkerActionDTO workerAction : workerActions) {
            List<SalaryRoyaltyStrategy> strategies = this.salaryRoyaltyStrategyService.queryByEmployeeIdRoleIdStatusAction(workerAction.getEmployeeId(), workerAction.getRoleId(), orderStatus, workerAction.getAction());
            if (ArrayUtils.isEmpty(strategies)) {
                //没有找到匹配的策略，则返回
                continue;
            }

            //构造匹配条件变量
            RoyaltyMatchFact matchFact = RoyaltyMatchFact.builder()
                    .employeeId(workerAction.getEmployeeId())
                    .roleId(workerAction.getRoleId())
                    .jobRole(workerAction.getJobRole())
                    .jobGrade(workerAction.getJobGrade())
                    .orgId(workerAction.getOrgId())
                    .designFeeStandard(designFeeStandard)
                    .houseNature(houseNature)
                    .build();

            Long orgId = workerAction.getOrgId();
            if (orgId == null) {
                EmployeeJobRole jobRole = this.employeeJobRoleService.queryLast(workerAction.getEmployeeId());
                orgId = jobRole.getOrgId();
                matchFact.setJobRole(jobRole.getJobRole());
                matchFact.setOrgId(orgId);
                matchFact.setJobGrade(jobRole.getJobGrade());
            }

            Org org = this.orgService.queryByOrgId(orgId);
            matchFact.setNature(org.getNature());

            List<SalaryRoyaltyStrategy> matchStrategies = this.match(orderBase.getOrderId(), matchFact, computeFact, strategies, feeFactCache);
            List<SalaryRoyaltyDetail> details = this.generateRoyaltyDetail(matchStrategies, computeFact, matchFact, orderId, salaryMonth, orderStatus, feeFactCache);
            if (ArrayUtils.isNotEmpty(details)) {
                royaltyDetails.addAll(details);
            }
        }

        if (ArrayUtils.isNotEmpty(royaltyDetails)) {
            this.salaryRoyaltyDetailService.saveBatch(royaltyDetails);
        }
    }

    private FeeFact buildFeeFact(Long orderId, String feeType, Integer periods) {
        //1.组装订单的费用等相关信息，这些对象需要进行计算或者更细粒度的条件匹配
        //1.1 查询状态对应要捞取的参与运算的费用配置

        OrderFee orderFee = this.orderFeeService.getByOrderIdTypePeriod(orderId, feeType, periods);
        if (orderFee == null) {
            //没有查到费用信息，无法计算
            return null;
        }
        FeeFact feeFact = new FeeFact();
        BeanUtils.copyProperties(orderFee, feeFact);

        //1.3 如果是工程款，查询费用明细信息
        if (StringUtils.equals("2", feeType)) {
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
        return feeFact;
    }

    /**
     * 找出匹配的提成策略
     * @param matchFact
     * @param computeFact
     * @param strategies
     * @return
     */
    private List<SalaryRoyaltyStrategy> match(Long orderId, RoyaltyMatchFact matchFact, RoyaltyComputeFact computeFact, List<SalaryRoyaltyStrategy> strategies, Map<String, FeeFact> feeFactCache) {
        if (ArrayUtils.isEmpty(strategies)) {
            return null;
        }

        List<SalaryRoyaltyStrategy> temp = new ArrayList<>();
        //初步筛选，看员工ID，roleId, job_role shopId 是否匹配
        for (SalaryRoyaltyStrategy strategy : strategies) {
            FeeFact feeFact = feeFactCache.get(orderId + "_" + strategy.getFeeType() + "_" + strategy.getPeriods());
            if (feeFact != null) {
                matchFact.setContractFee(feeFact.getContractFee());
            }
            //看费用是否付齐
            if (StringUtils.equals("1", strategy.getPayComplete())) {
                if (feeFact == null) {
                    //没有费用信息，无法计算，直接返回
                    continue;
                }
                computeFact.setFeeFact(feeFact);
                Long needPay = computeFact.getFeeFact().getNeedPay();
                Long payed = computeFact.getFeeFact().getPay();
                if (!needPay.equals(payed)) {
                    //未付齐，返回
                    continue;
                }
            }

            //如果配置了员工ID，则为最高优先级
            if (strategy.getEmployeeId() != null && matchFact.getEmployeeId().equals(strategy.getEmployeeId())) {
                temp.add(strategy);
                continue;
            } else if (strategy.getEmployeeId() != null && !matchFact.getEmployeeId().equals(strategy.getEmployeeId())) {
                //员工不匹配，则无需进行下面的筛选了
                continue;
            }

            //如果员工ID为null
            Long shopId = strategy.getShopId();
            switch (shopId + "") {
                case "-2" :
                    break;
                case "-1":
                    if (matchFact.getOrgId().equals(46L) || matchFact.getOrgId().equals(98L) || matchFact.getOrgId().equals(40L)) {
                        continue;
                    }
                    break;
                case "0" :
                    if (matchFact.getOrgId().equals(40L)) {
                        continue;
                    }
                    break;
                default :
                    if (!shopId.equals(matchFact.getOrgId())) {
                        continue;
                    }
                    break;
            }

            String jobRole = strategy.getJobRole();
            if (StringUtils.isNotBlank(jobRole) && !StringUtils.equals(matchFact.getJobRole(), jobRole)) {
                continue;
            }

            temp.add(strategy);
        };

        if (ArrayUtils.isEmpty(temp)) {
            return null;
        }

        //第二步，根据条件表达式精确筛选
        List<SalaryRoyaltyStrategy> result = new ArrayList<>();
        SPELUtils executor = new SPELUtils();
        executor.parse(matchFact);
        for (SalaryRoyaltyStrategy strategy : temp) {
            log.debug("-------------strategy id-----------"+strategy.getId());
            boolean isMatch = false;
            if (StringUtils.isBlank(strategy.getMatchCondition())) {
                isMatch = true;
            } else {
                isMatch = executor.executeBool(strategy.getMatchCondition());
            }
            if (isMatch) {
                result.add(strategy);
            }
        };

        return result;
    }

    public List<SalaryRoyaltyDetail> generateRoyaltyDetail(List<SalaryRoyaltyStrategy> strategies, RoyaltyComputeFact computeFact, RoyaltyMatchFact matchFact, Long orderId, Integer salaryMonth, String orderStatus, Map<String, FeeFact> feeFactCache) {
        if (ArrayUtils.isEmpty(strategies)) {
            return null;
        }

        LocalDateTime now = RequestTimeHolder.getRequestTime();
        SPELUtils executor = new SPELUtils();
        executor.parse(computeFact);
        List<SalaryRoyaltyDetail> royaltyDetails = new ArrayList<>();
        for (SalaryRoyaltyStrategy matchStrategy : strategies) {
            //2.组装订单费用信息
            FeeFact feeFact = feeFactCache.get(orderId + "_" + matchStrategy.getFeeType() + "_" + matchStrategy.getPeriods());
            if (feeFact == null) {
                //没有费用信息，无法计算，直接返回
                continue;
            }
            computeFact.setFeeFact(feeFact);

            //根据匹配的配置，开始进行提成金额的计算
            String formula = matchStrategy.getFormula();
            Long value = executor.executeLong(formula, computeFact);

            String jobRole = matchFact.getJobRole();
            Long employeeId = matchFact.getEmployeeId();
            Long orgId = matchFact.getOrgId();
            Long roleId = matchFact.getRoleId();

            //特殊处理，比如区域经组小组奖，客户代表组长奖
            if (StringUtils.equals("58", jobRole) || StringUtils.equals("118", jobRole)) {
                //区域经理小组奖
                if (!StringUtils.equals(jobRole, matchFact.getJobRole())) {
                    //本人不是区域经理，则找上级
                    EmployeeJobRole employeeJobRole = this.employeeJobRoleService.queryLast(matchFact.getEmployeeId());
                    if (employeeJobRole != null) {
                        Long parentEmployeeId = employeeJobRole.getParentEmployeeId();
                        if (parentEmployeeId != null) {
                            EmployeeJobRole parentJobRole = this.employeeJobRoleService.queryLast(parentEmployeeId);
                            if (parentJobRole != null && StringUtils.equals(parentJobRole.getJobRole(), jobRole)) {
                                employeeId = parentEmployeeId;
                                orgId = parentJobRole.getOrgId();
                            }
                        }
                    }
                }
            }

            //todo 多人拆分的计算
            //SalaryRoyaltyMultiSplit multiSplit = this.salaryRoyaltyMultiSplitService.getMultiSplit(matchStrategy.getType(), matchStrategy.getItem(), 0);


            //本月可提和已提的计算
            String isMinusSend = matchStrategy.getIsMinusSend();
            Long thisMonthFetch = new Long(value.longValue());
            Long alreadyFetch = 0L;
            if (StringUtils.equals("1", isMinusSend)) {
                //表示要减掉已发
                String minusItem = matchStrategy.getMinusItem();
                if (StringUtils.isNotBlank(minusItem)) {
                    List<String> items = Arrays.asList(StringUtils.split(minusItem,","));
                    List<SalaryRoyaltyDetail> sendRoyaltyDetails = this.salaryRoyaltyDetailService.queryByOrderIdEmployeeIdItems(orderId, matchFact.getEmployeeId(), items);

                    if (ArrayUtils.isNotEmpty(sendRoyaltyDetails)) {
                        alreadyFetch = sendRoyaltyDetails.stream().filter(sendRoyaltyDetail -> sendRoyaltyDetail.getTotalRoyalty() != null)
                                .mapToLong(SalaryRoyaltyDetail::getTotalRoyalty)
                                .sum();

                        //减掉已发放的
                        thisMonthFetch -= alreadyFetch;
                    }
                }
            }

            SalaryRoyaltyDetail royaltyDetail = new SalaryRoyaltyDetail();
            royaltyDetail.setEmployeeId(employeeId);
            royaltyDetail.setOrgId(orgId);
            royaltyDetail.setJobRole(jobRole);
            royaltyDetail.setRoleId(roleId);
            royaltyDetail.setSalaryMonth(salaryMonth);
            royaltyDetail.setOrderId(orderId);
            royaltyDetail.setOrderStatus(orderStatus);
            royaltyDetail.setFeeType(matchStrategy.getFeeType());
            royaltyDetail.setPeriods(matchStrategy.getPeriods());
            royaltyDetail.setType(matchStrategy.getType());
            royaltyDetail.setItem(matchStrategy.getItem());
            royaltyDetail.setMode(matchStrategy.getMode());
            royaltyDetail.setValue(matchStrategy.getValue());
            royaltyDetail.setTotalRoyalty(thisMonthFetch);
            royaltyDetail.setAlreadyFetch(alreadyFetch);
            royaltyDetail.setThisMonthFetch(thisMonthFetch);
            royaltyDetail.setAuditStatus("0");
            royaltyDetail.setStartTime(now);
            royaltyDetail.setStrategyId(matchStrategy.getId());
            royaltyDetail.setEndTime(TimeUtils.getForeverTime());
            royaltyDetail.setRemark(matchStrategy.getDescription()+"，由系统自动计算");
            royaltyDetails.add(royaltyDetail);
        }

        return royaltyDetails;
    }
}
