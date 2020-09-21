package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.security.Role;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.OrderStatusCfg;
import com.microtomato.hirun.modules.bss.config.entity.po.OrderStatusTransCfg;
import com.microtomato.hirun.modules.bss.config.entity.po.RoleAttentionStatusCfg;
import com.microtomato.hirun.modules.bss.config.service.IOrderRoleCfgService;
import com.microtomato.hirun.modules.bss.config.service.IOrderStatusCfgService;
import com.microtomato.hirun.modules.bss.config.service.IOrderStatusTransCfgService;
import com.microtomato.hirun.modules.bss.config.service.IRoleAttentionStatusCfgService;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustInfoDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.XQLTEInfoDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.XQLTYInfoDTO;
import com.microtomato.hirun.modules.bss.customer.service.ICustBaseService;
import com.microtomato.hirun.modules.bss.customer.service.ICustomerDomainService;
import com.microtomato.hirun.modules.bss.house.entity.po.Houses;
import com.microtomato.hirun.modules.bss.house.service.IHousesService;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.*;
import com.microtomato.hirun.modules.bss.order.entity.po.*;
import com.microtomato.hirun.modules.bss.order.exception.OrderException;
import com.microtomato.hirun.modules.bss.order.mapper.OrderBaseMapper;
import com.microtomato.hirun.modules.bss.order.service.*;
import com.microtomato.hirun.modules.bss.salary.entity.domain.SalaryDO;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeSelectDTO;
import com.microtomato.hirun.modules.organization.entity.dto.SimpleEmployeeDTO;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private ICustBaseService custBaseService;

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
    private IFeeDomainService feeDomainService;

    @Autowired
    private IHousesService housesService;

    @Autowired
    private IFinanceDomainService financeDomainService;

    @Autowired
    private IOrderFileService orderFileService;

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleService;

    @Autowired
    private IOrderFeeService orderFeeService;

    @Autowired
    private IOrderFeeItemService orderFeeItemService;

    @Autowired
    private IOrderPayItemService orderPayItemService;

    @Autowired
    private IOrderPayNoService orderPayNoService;

    @Autowired
    private IOrderContractService orderContractService;

    @Autowired
    private IOrderDiscountItemService orderDiscountItemService;

    @Autowired
    private ICustomerDomainService customerDomainService;

    @Autowired
    private OrderBaseMapper orderBaseMapper;

    @Autowired
    private IEmployeeService employeeService;

    /**
     * 查询订单综合信息
     * @param orderId
     * @return
     */
    @Override
    public OrderDetailDTO getOrderDetail(Long orderId) {
        OrderDetailDTO orderInfo = new OrderDetailDTO();

        OrderBase orderBase = this.orderBaseService.queryByOrderId(orderId);
        
        if (orderBase == null) {
            return orderInfo;
        }

        Long custId = orderBase.getCustId();
        CustInfoDTO customer = this.custBaseService.queryByCustIdOrOrderId(custId, orderId);
        orderInfo.setCustomer(customer);


        BeanUtils.copyProperties(orderBase, orderInfo);
        orderInfo.setTypeName(this.staticDataService.getCodeName("ORDER_TYPE", orderBase.getType()));
        Long housesId = orderBase.getHousesId();
        if (housesId != null) {
            //查询楼盘信息
            if (housesId != null) {
                orderInfo.setHousesName(this.housesService.queryHouseName(housesId));
            }
        }

        if (StringUtils.isNotBlank(orderInfo.getStatus())) {
            orderInfo.setStatusName(this.staticDataService.getCodeName("ORDER_STATUS", orderInfo.getStatus()));
            OrderStatusCfg statusCfg = this.orderStatusCfgService.getCfgByTypeStatus(orderBase.getType(), orderInfo.getStatus());
            if (statusCfg != null) {
                orderInfo.setTabShow(statusCfg.getOrderTabShow());
            }
        }

        if (StringUtils.isNotBlank(orderInfo.getHouseLayout())) {
            orderInfo.setHouseLayoutName(this.staticDataService.getCodeName("HOUSE_MODE", orderInfo.getHouseLayout()));
        }

        List<OrderWorkerDetailDTO> orderWorkers = this.orderWorkerService.queryOrderWorkerDetails(orderId);
        if (ArrayUtils.isNotEmpty(orderWorkers)) {
            orderInfo.setOrderWorkers(orderWorkers);
        }

        List<OrderFeeInfoDTO> orderFees = this.feeDomainService.queryOrderFeeInfo(orderId);
        if (ArrayUtils.isNotEmpty(orderFees)) {
            orderInfo.setOrderFees(orderFees);
        }

        List<OrderPayInfoDTO> orderPays = this.financeDomainService.queryPayInfoByOrderId(orderId);
        if (ArrayUtils.isNotEmpty(orderPays)) {
            orderInfo.setOrderPays(orderPays);
        }

        List<OrderFileDTO> orderFiles = this.orderFileService.queryOrderFiles(orderId);
        if (ArrayUtils.isNotEmpty(orderFiles)) {
            orderInfo.setOrderFiles(orderFiles);
        }

        List<OrderContract> orderContracts = this.orderContractService.queryByOrderId(orderId);
        if (ArrayUtils.isNotEmpty(orderContracts)) {
            List<OrderContractDTO> contracts = new ArrayList<>();
            orderContracts.forEach(orderContract -> {
                OrderContractDTO contract = new OrderContractDTO();
                BeanUtils.copyProperties(orderContract, contract);
                contract.setContractTypeName(this.staticDataService.getCodeName("ORDER_CONTRACT_TYPE", contract.getContractType()));
                contract.setBusiLevelName(this.staticDataService.getCodeName("BUSI_LEVEL", contract.getBusiLevel()));
                contract.setEnvironmentalTestingAgencyName(this.staticDataService.getCodeName("environmentalTestingAgency", contract.getEnvironmentalTestingAgency()));
                contracts.add(contract);
            });
            orderInfo.setOrderContracts(contracts);
        }

        List<OrderDiscountItemDTO> orderDiscountItems = this.orderDiscountItemService.queryByOrderId(orderId);
        if (ArrayUtils.isNotEmpty(orderDiscountItems)) {
            orderInfo.setOrderDiscountItems(orderDiscountItems);
        }

        List<XQLTYInfoDTO> bluePics = this.customerDomainService.getXQLTYInfo(customer.getOpenId());
        XQLTEInfoDTO bluePicSecond = this.customerDomainService.getXQLTEInfo(customer.getOpenId());
        if (ArrayUtils.isNotEmpty(bluePics)) {
            orderInfo.setXqltyInfo(bluePics);
        }

        if (bluePicSecond != null) {
            orderInfo.setXqlteInfo(bluePicSecond);
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
        List<OrderWorkerDTO> orderWorkers = this.orderWorkerService.queryByOrderId(orderId);
        return orderWorkers;
    }

    /**
     * 创建新订单
     * @param newOrder
     */
    @Override
    public Long createNewOrder(NewOrderDTO newOrder) {
        if (newOrder == null) {
            return null;
        }
        OrderBase order = new OrderBase();
        BeanUtils.copyProperties(newOrder, order);
        if (StringUtils.isBlank(newOrder.getStatus())) {
            order.setStatus(OrderConst.ORDER_STATUS_ASKING);
        }

        if (StringUtils.isBlank(newOrder.getType())) {
            order.setType(OrderConst.ORDER_TYPE_PRE);
        }

        OrderStatusCfg orderStatusCfg = this.orderStatusCfgService.getCfgByTypeStatus(order.getType(), order.getStatus());

        if (orderStatusCfg != null) {
            order.setStage(orderStatusCfg.getOrderStage());
        }

        UserContext userContext = WebContextUtils.getUserContext();
        Long orgId = userContext.getOrgId();
        if (orgId != null) {
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
            Org shop = orgDO.getBelongShop();
            if (shop != null) {
                order.setCreateShopId(shop.getOrgId());
                //归属店面在创建的时候与员工归属的店面保持一致，后面根据收设计费归属的店面进行修改，归属店面是以设计费收的店面为准
                order.setShopId(shop.getOrgId());
            }
        }

        this.orderBaseService.save(order);
        this.orderOperLogService.createOrderOperLog(order.getOrderId(), OrderConst.LOG_TYPE_CREATE, order.getStage(), order.getStatus(), OrderConst.OPER_LOG_CONTENT_CREATE);
        return order.getOrderId();
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

        try {
            SalaryDO salaryDO = SpringContextUtils.getBean(SalaryDO.class);
            salaryDO.createRoyalties(order.getOrderId(), newStatusCfg.getOrderStatus());
        } catch (Exception e) {
            log.error("计算提成错误", e);
        }
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
        queryWrapper.apply(" b.cust_id = a.cust_id ");
        queryWrapper.like(StringUtils.isNotEmpty(queryCondition.getCustName()), "b.cust_name", queryCondition.getCustName());
        queryWrapper.eq(StringUtils.isNotEmpty(queryCondition.getSex()), "b.sex", queryCondition.getSex());
        queryWrapper.likeRight(StringUtils.isNotEmpty(queryCondition.getMobileNo()), "b.mobile_no", queryCondition.getMobileNo());
        queryWrapper.eq(StringUtils.isNotEmpty(queryCondition.getOrderStatus()), "a.status", queryCondition.getOrderStatus());
        queryWrapper.eq(queryCondition.getHousesId() != null, "a.housesId", queryCondition.getHousesId());
        IPage<CustOrderInfoDTO> result = this.orderBaseMapper.queryCustOrderInfo(page, queryWrapper);

        List<CustOrderInfoDTO> custOrders = result.getRecords();
        if (ArrayUtils.isNotEmpty(custOrders)) {
            for (CustOrderInfoDTO custOrder : custOrders) {
                UsualFeeDTO usualFee = this.getUsualOrderFee(custOrder.getOrderId(), custOrder.getType());
                custOrder.setUsualFee(usualFee);
                custOrder.setStageName(this.staticDataService.getCodeName("ORDER_STAGE", custOrder.getStage()));
                custOrder.setSexName(this.staticDataService.getCodeName("SEX", custOrder.getSex()));
                custOrder.setStatusName(this.staticDataService.getCodeName("ORDER_STATUS", custOrder.getStatus()));
                custOrder.setTypeName(this.staticDataService.getCodeName("ORDER_TYPE", custOrder.getType()));
                custOrder.setHouseLayoutName(this.staticDataService.getCodeName("HOUSE_MODE", custOrder.getHouseLayout()));
                Long housesId = custOrder.getHousesId();
                if (housesId != null) {
                    custOrder.setHousesName(this.housesService.queryHouseName(housesId));

                }
            }
        }
        return result;
    }

    /**
     * 查询员工订单任务
     * @param condition
     * @return
     */
    @Override
    public List<OrderTaskDTO> queryOrderTasks(OrderTaskQueryDTO condition) {
        List<Role> roles = WebContextUtils.getUserContext().getRoles();
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        if (ArrayUtils.isEmpty(roles)) {
            return null;
        }

        List<OrderTaskDTO> tasks = new ArrayList<>();
        List<OrderStatusCfg> statusCfgs = new ArrayList<>();
        for (Role role : roles) {
            List<RoleAttentionStatusCfg> roleAttentionStatusCfgs = this.roleAttentionStatusCfgService.queryByRoleId(role.getId());
            if (ArrayUtils.isEmpty(roleAttentionStatusCfgs)) {
                continue;
            }

            List<String> statuses = new ArrayList<>();
            for (RoleAttentionStatusCfg attentionStatusCfg : roleAttentionStatusCfgs) {
                Long statusId = attentionStatusCfg.getAttentionStatusId();
                OrderStatusCfg statusCfg = this.orderStatusCfgService.getById(statusId);
                statuses.add(statusCfg.getOrderStatus());
                statusCfgs.add(statusCfg);
            }

            QueryWrapper<OrderTaskQueryDTO> wrapper = new QueryWrapper<>();
            wrapper.apply("b.cust_id = a.cust_id ");
            wrapper.like(StringUtils.isNotBlank(condition.getCustName()), "b.cust_name", condition.getCustName());
            wrapper.eq(StringUtils.isNotBlank(condition.getOrderStatus()), "a.status", condition.getOrderStatus());
            wrapper.eq(condition.getHousesId() != null, "a.houses_id", condition.getHousesId());
            wrapper.eq(StringUtils.isNotBlank(condition.getMobileNo()), "b.mobile_no", condition.getMobileNo());
            wrapper.exists("select 1 from order_worker c where c.order_id = a.order_id and c.employee_id = " + employeeId + " and c.role_id = " + role.getId() + " and c.end_date > now() ");
            wrapper.in("a.status", statuses);
            wrapper.orderByAsc("a.status", "a.create_time");

            List<OrderTaskDTO> temp = this.orderBaseMapper.queryOrderTaskInConsole(wrapper);
            if (ArrayUtils.isNotEmpty(temp)) {
                tasks.addAll(temp);
            }
        }


        if (ArrayUtils.isEmpty(tasks)) {
            return tasks;
        }

        tasks.forEach(task -> {
            task.setHouseLayoutName(this.staticDataService.getCodeName("HOUSE_MODE", task.getHouseLayout()));
            task.setTypeName(this.staticDataService.getCodeName("ORDER_TYPE", task.getType()));
            OrderStatusCfg statusCfg = this.findOrderStatusCfg(task.getType(), task.getStatus(), statusCfgs);
            if (statusCfg != null) {
                task.setStatusName(statusCfg.getStatusName());
                task.setPageUrl(statusCfg.getPageUrl());
            }

            if (task.getHousesId() != null) {
                Houses house = this.housesService.getHouse(task.getHousesId());
                if (house != null) {
                    task.setHousesName(house.getName());
                }
            }

        });
        return tasks;
    }

    /**
     * 查找订单状态配置
     * @param orderType
     * @param orderStatus
     * @param statusCfgs
     * @return
     */
    private OrderStatusCfg findOrderStatusCfg(String orderType, String orderStatus, List<OrderStatusCfg> statusCfgs) {
        if (ArrayUtils.isEmpty(statusCfgs)) {
            return null;
        }

        for (OrderStatusCfg statusCfg : statusCfgs) {
            if (StringUtils.equals(orderType, statusCfg.getType()) && StringUtils.equals(orderStatus, statusCfg.getOrderStatus())) {
                return statusCfg;
            }
        }
        return null;
    }

    /**
     * 获取经常用于展示的订单工作人员数据
     * @param orderId
     * @return
     */
    @Override
    public UsualOrderWorkerDTO getUsualOrderWorker(Long orderId) {
        UsualOrderWorkerDTO usualWorker = new UsualOrderWorkerDTO();
        List<OrderWorkerDTO> orderWorkers = this.queryOrderWorkers(orderId);
        if (ArrayUtils.isEmpty(orderWorkers)) {
            return usualWorker;
        }

        orderWorkers.forEach(worker -> {
            Long roleId = worker.getRoleId();
            String name = worker.getName();
            if (roleId.equals(3L)) {
                usualWorker.setCounselorName(name);
            } else if (roleId.equals(15L)) {
                usualWorker.setAgentName(name);
            } else if (roleId.equals(30L)) {
                usualWorker.setDesignerName(name);
            } else if (roleId.equals(45L)) {
                usualWorker.setCabinetDesignerName(name);
            } else if (roleId.equals(46L)) {
                usualWorker.setMaterialName(name);
            } else if (roleId.equals(47L)) {
                usualWorker.setCabinetName(name);
            } else if (roleId.equals(555L)) {
                usualWorker.setReportName(name);
            } else if (roleId.equals(33L)) {
                usualWorker.setProjectManagerName(name);
            }
        });

        return usualWorker;
    }

    /**
     * 常用费用查询
     * @param orderId
     * @param orderType
     * @return
     */
    @Override
    public UsualFeeDTO getUsualOrderFee(Long orderId, String orderType) {
        UsualFeeDTO usualFee = new UsualFeeDTO();
        List<OrderFee> orderFees = this.orderFeeService.queryByOrderId(orderId);
        if (ArrayUtils.isEmpty(orderFees)) {
            return usualFee;
        }
        List<OrderFeeItem> orderFeeItems = this.orderFeeItemService.queryByOrderId(orderId);
        List<OrderPayItem> orderPayItems = this.orderPayItemService.queryByOrderId(orderId);
        List<OrderPayNo> orderPayNos = this.orderPayNoService.queryByOrderId(orderId);

        if (ArrayUtils.isNotEmpty(orderPayItems)) {
            OrderPayItem designPayItem = this.findPayItem(new ArrayList<Long>() {{
                this.add(1L);
                this.add(2L);
                this.add(3L);
                this.add(4L);
                this.add(5L);
                this.add(6L);
            }}, null, orderPayItems);
            if (designPayItem != null) {
                OrderPayNo payNo = this.findPayNo(designPayItem.getPayNo(), orderPayNos);
                if (payNo != null) {
                    usualFee.setPayDesignDate(payNo.getPayDate());
                }
            }

            OrderPayItem firstPayItem = this.findPayItem(new ArrayList<Long>() {{
                this.add(7L);
            }}, 1, orderPayItems);

            if (firstPayItem != null) {
                OrderPayNo payNo = this.findPayNo(firstPayItem.getPayNo(), orderPayNos);
                if (payNo != null) {
                    usualFee.setPayDate(payNo.getPayDate());
                }
            }
            OrderPayItem secondPayItem = this.findPayItem(new ArrayList<Long>() {{
                this.add(7L);
            }}, 2, orderPayItems);

            if (secondPayItem != null) {
                OrderPayNo payNo = this.findPayNo(secondPayItem.getPayNo(), orderPayNos);
                if (payNo != null) {
                    usualFee.setSecondPayDate(payNo.getPayDate());
                }
            }
            OrderPayItem settlementPayItem = this.findPayItem(new ArrayList<Long>() {{
                this.add(7L);
            }}, 3, orderPayItems);

            if (settlementPayItem != null) {
                OrderPayNo payNo = this.findPayNo(settlementPayItem.getPayNo(), orderPayNos);
                if (payNo != null) {
                    usualFee.setSettlementPayDate(payNo.getPayDate());
                }
            }

        }

        orderFees.forEach(orderFee -> {
            String type = orderFee.getType();
            Long originalTotalFee = orderFee.getTotalFee();
            Long originalNeedPay = orderFee.getNeedPay();
            Long originalPayed = orderFee.getPay();
            Integer period = orderFee.getPeriods();

            Double totalFee = null;
            Double needPay = null;
            Double payed = null;
            if (originalTotalFee != null) {
                totalFee = originalTotalFee /100d;
            }
            if (originalNeedPay != null) {
                needPay = originalNeedPay / 100d;
            }
            if (originalPayed != null) {
                payed = originalPayed / 100d;
            }

            if (StringUtils.equals("1", type)) {
                //设计费
                usualFee.setDesignFee(totalFee);
                usualFee.setDesignNeedPay(needPay);
                usualFee.setDesignPayed(payed);

            } else if (StringUtils.equals("2", type)) {
                //工程款
                List<OrderFeeItem> feeItems = this.findFeeItems(orderFee.getFeeNo(), orderFeeItems);
                if (ArrayUtils.isEmpty(feeItems)) {
                    return;
                }

                Double basicFee = null;
                Double doorFee = null;
                Double furnitureFee = null;
                Double backDesignFee = null;
                for (OrderFeeItem feeItem : feeItems) {
                    Long originalFee = feeItem.getFee();
                    Double fee = null;
                    if (originalFee != null) {
                        fee = originalFee / 100d;
                    }
                    if (feeItem.getFeeItemId().equals(5L)) {
                        basicFee = fee;
                    } else if (feeItem.getFeeItemId().equals(7L)) {
                        doorFee = fee;
                    } else if (feeItem.getFeeItemId().equals(8L)) {
                        furnitureFee = fee;
                    } else if (feeItem.getFeeItemId().equals(14L)) {
                        backDesignFee = fee;
                    }
                }
                usualFee.setBackDesignFee(backDesignFee);
                if (period.equals(1)) {
                    //首期
                    usualFee.setBasicFee(basicFee);
                    usualFee.setDoorFee(doorFee);
                    usualFee.setFurnitureFee(furnitureFee);
                    usualFee.setContractFee(totalFee);
                    usualFee.setNeedPay(needPay);
                    usualFee.setPayed(payed);

                    if (totalFee != null && backDesignFee != null) {
                        Double totalContractFee = totalFee + Math.abs(backDesignFee);
                        usualFee.setTotalContractFee(totalContractFee);
                    } else if (totalFee != null) {
                        usualFee.setTotalContractFee(totalFee);
                    }

                } else if (period.equals(2) && StringUtils.equals(OrderConst.ORDER_TYPE_HOME, orderType)) {
                    //二期
                    usualFee.setSecondBasicFee(basicFee);
                    usualFee.setSecondDoorFee(doorFee);
                    usualFee.setSecondFurnitureFee(furnitureFee);
                    usualFee.setSecondContractFee(totalFee);
                    usualFee.setSecondNeedPay(needPay);
                    usualFee.setSecondPayed(payed);
                } else if (period.equals(3) || (period.equals(2) && StringUtils.equals(OrderConst.ORDER_TYPE_WOOD, orderType))) {
                    usualFee.setSettlementBasicFee(basicFee);
                    usualFee.setSettlementDoorFee(doorFee);
                    usualFee.setSettlementFurnitureFee(furnitureFee);
                    usualFee.setSettlementContractFee(totalFee);
                    usualFee.setSettlementNeedPay(needPay);
                    usualFee.setSettlementPayed(payed);
                }
            } else if (StringUtils.equals("3", type)) {
                //主材款
                usualFee.setMaterialFee(totalFee);
            } else if (StringUtils.equals("4", type)) {
                usualFee.setCabinetFee(totalFee);
            }
        });

        return usualFee;
    }

    private List<OrderFeeItem> findFeeItems(Long feeNo, List<OrderFeeItem> feeItems) {
        if (ArrayUtils.isEmpty(feeItems)) {
            return null;
        }

        List<OrderFeeItem> result = new ArrayList<>();
        feeItems.forEach(feeItem -> {
            if (feeNo.equals(feeItem.getFeeNo())) {
                result.add(feeItem);
            }
        });

        return result;
    }

    /**
     * 查找满足条件的付款记录
     * @param payItemIds
     * @param payItems
     * @return
     */
    private OrderPayItem findPayItem(List<Long> payItemIds, Integer periods, List<OrderPayItem> payItems) {
        if (ArrayUtils.isEmpty(payItems)) {
            return null;
        }

        for (OrderPayItem payItem : payItems) {
            if (periods == null) {
                if (payItemIds.contains(payItem.getPayItemId())) {
                    return payItem;
                }
            } else {
                if (payItemIds.contains(payItem.getPayItemId()) && periods.equals(payItem.getPeriods())) {
                    return payItem;
                }
            }
        }

        return null;
    }

    /**
     * 根据付款流水找到付款记录
     * @param payNo
     * @param orderPayNos
     * @return
     */
    private OrderPayNo findPayNo(Long payNo, List<OrderPayNo> orderPayNos) {
        if (ArrayUtils.isEmpty(orderPayNos)) {
            return null;
        }

        for (OrderPayNo orderPayNo : orderPayNos) {
            if (payNo.equals(orderPayNo.getPayNo())) {
                return orderPayNo;
            }
        }

        return null;
    }

    /**
     * 员工业绩统计
     * @param queryCond
     * @return
     */
    @Override
    public List<EmployeeResultsDTO> queryEmployeeResults(EmployeeResultsQueryDTO queryCond) {
        List<EmployeeResultsDTO> list = new ArrayList<>();
        if(queryCond.getEmployeeId() != null && queryCond.getEmployeeId() > 0) {
            list.add(this.getEmployeeResultsByEmployee(queryCond.getEmployeeId(), queryCond.getOrderCreateStartDate(), queryCond.getOrderCreateEndDate()));
        } else if(queryCond.getRoleId() != null && queryCond.getRoleId() > 0) {
            //先根据角色找到员工，再循环
            EmployeeSelectDTO employeeSelectDTO = new EmployeeSelectDTO();
            employeeSelectDTO.setMode("priv");
            employeeSelectDTO.setRoleId(queryCond.getRoleId());
            List<SimpleEmployeeDTO> simpleEmployeeDTOList = employeeService.queryEmployeeBySelectMode(employeeSelectDTO);
            for(SimpleEmployeeDTO simpleEmployeeDTO : simpleEmployeeDTOList) {
                list.add(this.getEmployeeResultsByEmployee(simpleEmployeeDTO.getEmployeeId(), queryCond.getOrderCreateStartDate(), queryCond.getOrderCreateEndDate()));
            }
        }
        return list;
    }

    private EmployeeResultsDTO getEmployeeResultsByEmployee(Long employeeId, LocalDateTime startDate, LocalDateTime endDate) {
        EmployeeResultsDTO employeeResultsDTO = new EmployeeResultsDTO();
        List<OrderBase> orderBaseList = orderBaseMapper.queryOrderBaseByEmployee(employeeId, startDate, endDate);
        employeeResultsDTO.setEmployeeName(employeeService.getEmployeeNameEmployeeId(employeeId));
        employeeResultsDTO.setOrderCount(orderBaseList.size());

        //遍历累计合同总金额及费用明细金额
        for(OrderBase orderBase : orderBaseList) {
            UsualFeeDTO usualFeeDTO = this.getUsualOrderFee(orderBase.getOrderId(), OrderConst.ORDER_TYPE_HOME);
            OrderDetailDTO orderDetailDTO = this.getOrderDetail(orderBase.getOrderId());
            employeeResultsDTO.setContractTotalFee(employeeResultsDTO.getContractTotalFee() + (usualFeeDTO.getContractFee() == null ? 0d : usualFeeDTO.getContractFee()));
            employeeResultsDTO.setDoorFee(employeeResultsDTO.getDoorFee() + (usualFeeDTO.getDoorFee() == null ? 0d : usualFeeDTO.getDoorFee()));
            employeeResultsDTO.setFurnitureFee(employeeResultsDTO.getFurnitureFee() + (usualFeeDTO.getFurnitureFee() == null ? 0d : usualFeeDTO.getFurnitureFee()));
            employeeResultsDTO.setMainMaterialFee(employeeResultsDTO.getMainMaterialFee() + (usualFeeDTO.getMaterialFee() == null ? 0d : usualFeeDTO.getMaterialFee()));
            employeeResultsDTO.setCupboardFee(employeeResultsDTO.getCupboardFee() + (usualFeeDTO.getCabinetFee() == null ? 0d : usualFeeDTO.getCabinetFee()));

//            if(employeeResultsDTO.getContractTotalFee() == null) {
//                employeeResultsDTO.setContractTotalFee(orderDetailDTO.getTotalActFee());
//            } else {
//                employeeResultsDTO.setContractTotalFee(employeeResultsDTO.getContractTotalFee() + orderDetailDTO.getTotalActFee());
//            }
        }

        return employeeResultsDTO;
    }
}
