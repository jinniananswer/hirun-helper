package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.house.service.IHousesService;
import com.microtomato.hirun.modules.bss.order.QueryInspectCondDTO;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderInspectDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderInspect;
import com.microtomato.hirun.modules.bss.order.mapper.OrderInspectMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderInspectService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * (OrderInspect)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-08-11 18:07:44
 */
@Service
@Slf4j
public class OrderInspectServiceImpl extends ServiceImpl<OrderInspectMapper, OrderInspect> implements IOrderInspectService {

    @Autowired
    private OrderInspectMapper orderInspectMapper;

    @Autowired
    private OrderDomainServiceImpl orderDomainService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IHousesService housesService;

    @Override
    public void save(OrderInspectDTO dto) {

        OrderInspect orderInspect = new OrderInspect();
        BeanUtils.copyProperties(dto, orderInspect);

        if (dto.getId() == null) {
            this.baseMapper.insert(orderInspect);
        } else {
            this.baseMapper.updateById(orderInspect);
        }
    }

    @Override
    public OrderInspectDTO queryOrderInspect(Long orderId) {
        OrderInspect orderInspect = this.orderInspectMapper.selectOne(new QueryWrapper<OrderInspect>().lambda()
                .eq(OrderInspect::getOrderId, orderId));

        OrderInspectDTO dto = new OrderInspectDTO();
        if (orderInspect != null) {
            BeanUtils.copyProperties(orderInspect, dto);
        }
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void nextStep(OrderInspectDTO dto) {
        this.save(dto);
        orderDomainService.orderStatusTrans(dto.getOrderId(), OrderConst.OPER_NEXT_STEP);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void submitToNotReceive(OrderInspectDTO dto) {
        this.save(dto);
        orderDomainService.orderStatusTrans(dto.getOrderId(), OrderConst.OPER_DELAY);
    }

    @Override
    public IPage<OrderInspectDTO> queryOrderInspects(QueryInspectCondDTO condDTO) {
        QueryWrapper<QueryInspectCondDTO> queryWrapper = new QueryWrapper<>();
        Page<QueryInspectCondDTO> page = new Page<>(condDTO.getPage(), condDTO.getSize());

        queryWrapper.like(StringUtils.isNotEmpty(condDTO.getCustName()), "a.cust_name", condDTO.getCustName());
        queryWrapper.eq(condDTO.getHouseId() != null, "c.house_id", condDTO.getHouseId());
        queryWrapper.eq(condDTO.getDesignEmployeeId() != null, "f.design_employee_id", condDTO.getDesignEmployeeId());
        queryWrapper.eq(condDTO.getAgentEmployeeId() != null, "f.cust_service_employee_id", condDTO.getAgentEmployeeId());
        queryWrapper.eq(StringUtils.isNotBlank(condDTO.getCheckStatus()), "b.check_status", condDTO.getCheckStatus());
        queryWrapper.eq(StringUtils.isNotBlank(condDTO.getInstitution()), "b.institution", condDTO.getInstitution());
        queryWrapper.eq(StringUtils.isNotBlank(condDTO.getReceiveStatus()), "b.receive_status", condDTO.getReceiveStatus());
        queryWrapper.eq(StringUtils.isNotBlank(condDTO.getSettleStatus()), "b.settle_status", condDTO.getSettleStatus());

        queryWrapper.apply(" a.cust_id=c.party_id");
        queryWrapper.apply(" a.cust_id=d.cust_id");
        queryWrapper.apply(" d.order_id=b.order_id");

        queryWrapper.orderByDesc("a.create_time");

        IPage<OrderInspectDTO> iPage = this.baseMapper.queryOrderInspects(page, queryWrapper);
        if (iPage.getRecords().size() <= 0) {
            return iPage;
        }
        List<OrderInspectDTO> list = iPage.getRecords();
        for (OrderInspectDTO dto : list) {
            dto.setInstitutionName(staticDataService.getCodeName("environmentalTestingAgency", dto.getInstitution()));
            dto.setCheckStatusName(staticDataService.getCodeName("INSPECT_CHECK_STATUS", dto.getCheckStatus()));
            dto.setReceiveStatusName(staticDataService.getCodeName("INSPECT_RECEIVE_STATUS", dto.getReceiveStatus()));
            dto.setSettleStatusName(staticDataService.getCodeName("ORDER_SETTLE_STATUS", dto.getSettleStatus()));
            dto.setCustomerServiceName(employeeService.getEmployeeNameEmployeeId(dto.getCustServiceEmployeeId()));
            dto.setDesignEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getDesignEmployeeId()));
            dto.setHouseAddress(housesService.queryHouseName(dto.getHouseId()));
        }
        return iPage;
    }
}