package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.security.Role;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.OrderRoleCfg;
import com.microtomato.hirun.modules.bss.config.entity.po.OrderStatusCfg;
import com.microtomato.hirun.modules.bss.config.entity.po.OrderStatusTransCfg;
import com.microtomato.hirun.modules.bss.config.entity.po.RoleAttentionStatusCfg;
import com.microtomato.hirun.modules.bss.config.service.IOrderRoleCfgService;
import com.microtomato.hirun.modules.bss.config.service.IOrderStatusCfgService;
import com.microtomato.hirun.modules.bss.config.service.IOrderStatusTransCfgService;
import com.microtomato.hirun.modules.bss.config.service.IRoleAttentionStatusCfgService;
import com.microtomato.hirun.modules.bss.house.service.IHousesService;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.*;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.microtomato.hirun.modules.bss.order.exception.OrderException;
import com.microtomato.hirun.modules.bss.order.mapper.OrderBaseMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderBaseService;
import com.microtomato.hirun.modules.bss.order.service.IOrderDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderOperLogService;
import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private IHousesService housesService;

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

        OrderStatusCfg orderStatusCfg = this.orderStatusCfgService.getCfgByStatus(order.getStatus());

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
        OrderStatusCfg statusCfg = this.orderStatusCfgService.getCfgByStatus(status);
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
}
