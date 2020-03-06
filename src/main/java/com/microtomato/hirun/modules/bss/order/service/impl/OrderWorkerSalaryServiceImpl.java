package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerSalaryDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWorkerSalary;
import com.microtomato.hirun.modules.bss.order.mapper.OrderWorkerSalaryMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerSalaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-03-01
 */
@Slf4j
@Service
public class OrderWorkerSalaryServiceImpl extends ServiceImpl<OrderWorkerSalaryMapper, OrderWorkerSalary> implements IOrderWorkerSalaryService {

    @Autowired
    private IOrderDomainService domainService;

    @Override
    public List<OrderWorkerSalaryDTO> queryOrderWorkerSalary(Integer periods, Long orderId) {
        List<OrderWorkerSalary> list = this.baseMapper.selectList(new QueryWrapper<OrderWorkerSalary>().lambda()
                .eq(periods != null, OrderWorkerSalary::getPeriods, periods)
                .eq(OrderWorkerSalary::getOrderId, orderId));
        if (list.size() <= 0) {
            return null;
        }
        List<OrderWorkerSalaryDTO> dtoList = new ArrayList<>();
        for (OrderWorkerSalary orderWorkerSalary : list) {
            OrderWorkerSalaryDTO dto = new OrderWorkerSalaryDTO();
            BeanUtils.copyProperties(orderWorkerSalary, dto);
            dto.setHydropowerSalary(orderWorkerSalary.getHydropowerSalary().doubleValue() / 100);
            dto.setWoodworkerSalary(orderWorkerSalary.getWoodworkerSalary().doubleValue() / 100);
            dto.setTilerSalary(orderWorkerSalary.getTilerSalary().doubleValue() / 100);
            dto.setPainterSalary(orderWorkerSalary.getPainterSalary().doubleValue() / 100);
            dto.setWallworkerSalary(orderWorkerSalary.getWallworkerSalary().doubleValue() / 100);
            dtoList.add(dto);
        }
        return dtoList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void closeWorkerSalary(Integer periods, OrderWorkerSalaryDTO dto) {
        this.updateWorkerSalary(periods, dto);
        OrderWorkerSalary orderWorkerSalary = new OrderWorkerSalary();
        orderWorkerSalary.setId(dto.getId());
        orderWorkerSalary.setStatus("2");
        this.baseMapper.updateById(orderWorkerSalary);
        domainService.orderStatusTrans(dto.getOrderId(), "NEXT");
    }

    @Override
    public void updateWorkerSalary(Integer periods, OrderWorkerSalaryDTO dto) {
        OrderWorkerSalary orderWorkerSalary = new OrderWorkerSalary();
        BeanUtils.copyProperties(dto, orderWorkerSalary);
        BigDecimal hySalary = new BigDecimal(dto.getHydropowerSalary() + "");
        BigDecimal wdSalary = new BigDecimal(dto.getWoodworkerSalary() + "");
        BigDecimal tlSalary = new BigDecimal(dto.getTilerSalary() + "");
        BigDecimal ptSalary = new BigDecimal(dto.getPainterSalary() + "");
        BigDecimal wlSalary = new BigDecimal(dto.getWallworkerSalary() + "");

        BigDecimal b = new BigDecimal("100");

        orderWorkerSalary.setWallworkerSalary(wlSalary.multiply(b).longValue());
        orderWorkerSalary.setHydropowerSalary(hySalary.multiply(b).longValue());
        orderWorkerSalary.setTilerSalary(tlSalary.multiply(b).longValue());
        orderWorkerSalary.setPainterSalary(ptSalary.multiply(b).longValue());
        orderWorkerSalary.setWoodworkerSalary(wdSalary.multiply(b).longValue());
        orderWorkerSalary.setStatus("1");
        orderWorkerSalary.setPeriods(periods);

        OrderWorkerSalary existWorkerSalary = this.baseMapper.selectOne(new QueryWrapper<OrderWorkerSalary>().lambda().
                eq(OrderWorkerSalary::getOrderId, dto.getOrderId()).eq(OrderWorkerSalary::getPeriods, periods));
        if (existWorkerSalary == null) {
            this.baseMapper.insert(orderWorkerSalary);
        } else {
            this.baseMapper.update(orderWorkerSalary, new UpdateWrapper<OrderWorkerSalary>().lambda().eq(OrderWorkerSalary::getOrderId, dto.getOrderId())
                    .eq(OrderWorkerSalary::getPeriods,periods));
        }
    }
}
