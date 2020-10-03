package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.UsualOrderWorkerDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.factory.FactoryOrderDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.factory.FactoryOrderFollowDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.factory.FactoryOrderInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.factory.QueryFactoryOrderDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderContract;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFactoryOrder;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFactoryOrderFollow;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWorker;
import com.microtomato.hirun.modules.bss.order.mapper.OrderFactoryOrderMapper;
import com.microtomato.hirun.modules.bss.order.service.*;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * (OrderFactoryOrder)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-09-26 16:32:41
 */
@Service
@Slf4j
@DataSource(DataSourceKey.INS)
public class OrderFactoryOrderServiceImpl extends ServiceImpl<OrderFactoryOrderMapper, OrderFactoryOrder> implements IOrderFactoryOrderService {

    @Autowired
    private OrderFactoryOrderMapper orderFactoryOrderMapper;

    @Autowired
    private IOrderBaseService orderBaseService;

    @Autowired
    private IOrderDomainService orderDomainService;

    @Autowired
    private IOrderContractService orderContractService;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IOrderFactoryOrderFollowService orderFactoryOrderFollowService;

    @Autowired
    private IOrderWorkerService orderWorkerService;

    /**
     * 查询工厂订单
     * @param condition
     * @return
     */
    @Override
    public IPage<FactoryOrderInfoDTO> queryFactoryOrders(QueryFactoryOrderDTO condition) {
        IPage<QueryFactoryOrderDTO> request = new Page<>(condition.getPage(), condition.getLimit());
        QueryWrapper<QueryFactoryOrderDTO> wrapper = new QueryWrapper<>();
        wrapper.apply(" b.cust_id = a.cust_id ");
        wrapper.like(StringUtils.isNotBlank(condition.getCustName()), "b.cust_name", condition.getCustName());
        wrapper.like(StringUtils.isNotBlank(condition.getMobileNo()), "b.mobile_no", condition.getMobileNo());

        String shopIds = condition.getShopIds();
        if (StringUtils.isNotBlank(shopIds)) {
            List<String> shopIdArray = Arrays.asList(StringUtils.split(shopIds, ","));
            wrapper.in("a.shop_id", shopIdArray);
        }

        if (condition.getAgent() != null) {
            wrapper.apply("exists(select 1 from order_worker e where e.order_id = a.order_id and e.employee_id = {0} and e.role_id = 15 and e.end_date > now()) ", condition.getAgent());
        }

        if (condition.getDesigner() != null) {
            wrapper.apply("exists(select 1 from order_worker e where e.order_id = a.order_id and e.employee_id = {0} and e.role_id = 30 and e.end_date > now()) ", condition.getDesigner());
        }

        if (condition.getProjectManager() != null) {
            wrapper.apply("exists(select 1 from order_worker e where e.order_id = a.order_id and e.employee_id = {0} and e.role_id = 33 and e.end_date > now()) ", condition.getProjectManager());
        }

        if (condition.getProjectCharger() != null) {
            wrapper.apply("exists(select 1 from order_worker e where e.order_id = a.order_id and e.employee_id = {0} and e.role_id = 48 and e.end_date > now()) ", condition.getProjectCharger());
        }

        if (condition.getFactoryOrderManager() != null) {
            wrapper.apply("exists(select 1 from order_worker e where e.order_id = a.order_id and e.employee_id = {0} and e.role_id = 59 and e.end_date > now()) ", condition.getFactoryOrderManager());
        }

        wrapper.eq(condition.getHousesId() != null, "a.houses_id", condition.getHousesId());

        IPage<FactoryOrderInfoDTO> pageOrders = this.orderFactoryOrderMapper.queryFactoryOrders(request, wrapper);

        List<FactoryOrderInfoDTO> factoryOrders = pageOrders.getRecords();

        //设置订单参与人
        if (ArrayUtils.isNotEmpty(factoryOrders)) {
            factoryOrders.forEach(factoryOrder -> {
                UsualOrderWorkerDTO usualOrderWorkerDTO = this.orderDomainService.getUsualOrderWorker(factoryOrder.getOrderId());
                factoryOrder.setUsualWorker(usualOrderWorkerDTO);
                List<OrderContract> contracts = orderContractService.queryByOrderId(factoryOrder.getOrderId());
                if (ArrayUtils.isNotEmpty(contracts)) {
                    factoryOrder.setContractStartDate(contracts.get(0).getStartDate());
                }

                factoryOrder.setOrderStatusName(this.staticDataService.getCodeName("ORDER_STATUS", factoryOrder.getStatus()));

                if (StringUtils.isNotBlank(factoryOrder.getFactoryStatus())) {
                    factoryOrder.setFactoryStatusName(this.staticDataService.getCodeName("FACTORY_ORDER_STATUS", factoryOrder.getFactoryStatus()));
                }
            });
        }

        return pageOrders;
    }

    /**
     * 获取展示工厂订单信息及工单信息
     * @param orderId
     * @return
     */
    @Override
    public FactoryOrderDTO getFactoryOrderDTO(Long orderId) {
        OrderFactoryOrder factoryOrder = this.getFactoryOrder(orderId);
        FactoryOrderDTO data = new FactoryOrderDTO();

        if (factoryOrder != null) {
            BeanUtils.copyProperties(factoryOrder, data);
            OrderWorker worker = this.orderWorkerService.getOneOrderWorkerByOrderIdRoleId(orderId, 59L);
            if (worker != null) {
                data.setFactoryOrderManager(worker.getEmployeeId());
            }

            Long factoryOrderId = factoryOrder.getId();
            List<OrderFactoryOrderFollow> orderFollows = this.orderFactoryOrderFollowService.queryByFactoryOrderId(factoryOrderId);
            if (ArrayUtils.isNotEmpty(orderFollows)) {
                List<FactoryOrderFollowDTO> follows = new ArrayList<>();
                orderFollows.forEach(orderFollow -> {
                    FactoryOrderFollowDTO follow = new FactoryOrderFollowDTO();
                    BeanUtils.copyProperties(orderFollow, follow);
                    follows.add(follow);
                });

                data.setFollows(follows);
            }
        }

        data.setOrderId(orderId);
        return data;
    }

    /**
     * 根据订单ID查询工厂订单信息
     * @param orderId
     * @return
     */
    @Override
    public OrderFactoryOrder getFactoryOrder(Long orderId) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.getOne(Wrappers.<OrderFactoryOrder>lambdaQuery()
                .eq(OrderFactoryOrder::getOrderId, orderId)
                .ge(OrderFactoryOrder::getEndDate, now), false);
    }

    /**
     * 保存工厂订单信息
     * @param data
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void saveFactoryOrder(FactoryOrderDTO data) {
        OrderFactoryOrder factoryOrder = new OrderFactoryOrder();
        BeanUtils.copyProperties(data, factoryOrder);

        LocalDateTime now = RequestTimeHolder.getRequestTime();
        if (factoryOrder.getId() == null) {
            factoryOrder.setStartDate(now);
            factoryOrder.setEndDate(TimeUtils.getForeverTime());
            factoryOrder.setStatus("0");
            this.save(factoryOrder);
        } else {
            this.updateById(factoryOrder);
        }

        Long orderManager = data.getFactoryOrderManager();
        if (orderManager != null) {
            this.orderWorkerService.updateOrderWorker(data.getOrderId(), 59L, orderManager);
        } else {
            OrderWorker orderWorker = this.orderWorkerService.getOneOrderWorkerByOrderIdRoleId(data.getOrderId(), 59L);
            if (orderWorker != null) {
                Long workerId = orderWorker.getId();
                this.orderWorkerService.deleteOrderWorker(new ArrayList<Long>() {{
                    add(workerId);
                }});
            }
        }

        List<FactoryOrderFollowDTO> follows = data.getFollows();
        if (ArrayUtils.isNotEmpty(follows)) {
            follows.forEach(follow -> {
                follow.setFactoryOrderId(factoryOrder.getId());
            });
            this.saveFollows(data.getFollows());
        }
    }

    /**
     * 关闭单据
     * @param data
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void closeFactoryOrder(FactoryOrderDTO data) {
        OrderFactoryOrder factoryOrder = new OrderFactoryOrder();
        factoryOrder.setStatus("1");
        factoryOrder.setId(data.getId());
        this.updateById(factoryOrder);
    }

    /**
     * 保存跟单信息
     * @param follows
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void saveFollows(List<FactoryOrderFollowDTO> follows) {
        if (ArrayUtils.isEmpty(follows)) {
            return;
        }
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        List<OrderFactoryOrderFollow> createFollows = new ArrayList<>();
        List<OrderFactoryOrderFollow> updateFollows = new ArrayList<>();

        follows.forEach(follow -> {
            OrderFactoryOrderFollow orderFollow = new OrderFactoryOrderFollow();
            BeanUtils.copyProperties(follow, orderFollow);

            if (orderFollow.getId() == null) {
                orderFollow.setStartDate(now);
                orderFollow.setEndDate(TimeUtils.getForeverTime());
                createFollows.add(orderFollow);
            } else {
                updateFollows.add(orderFollow);
            }
        });

        if (ArrayUtils.isNotEmpty(createFollows)) {
            this.orderFactoryOrderFollowService.saveBatch(createFollows);
        }

        if (ArrayUtils.isNotEmpty(updateFollows)) {
            this.orderFactoryOrderFollowService.updateBatchById(updateFollows);
        }
    }
}