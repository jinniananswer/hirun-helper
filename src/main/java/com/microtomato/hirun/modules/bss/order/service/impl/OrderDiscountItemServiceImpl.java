package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderDiscountItemDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderDiscountItem;
import com.microtomato.hirun.modules.bss.order.mapper.OrderDiscountItemMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderDiscountItemService;
import com.microtomato.hirun.modules.organization.service.impl.EmployeeServiceImpl;
import com.microtomato.hirun.modules.system.entity.po.StaticData;
import com.microtomato.hirun.modules.system.service.impl.StaticDataServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单优惠项 服务实现类
 * </p>
 *
 * @author anwx
 * @since 2020-02-26
 */
@Slf4j
@Service
@DataSource(DataSourceKey.INS)
public class OrderDiscountItemServiceImpl extends ServiceImpl<OrderDiscountItemMapper, OrderDiscountItem> implements IOrderDiscountItemService {

    @Autowired
    private StaticDataServiceImpl staticDataService;

    @Autowired
    private EmployeeServiceImpl employeeService;

    @Override
    public List<OrderDiscountItemDTO> list(Long orderId) {
        List<StaticData> staticDataList = staticDataService.getStaticDatas("DISCOUNT_ITEM");
        List<OrderDiscountItem> orderDiscountItemList = baseMapper.selectList((new QueryWrapper<OrderDiscountItem>().lambda()
                .eq(OrderDiscountItem::getOrderId, orderId)));

        List<OrderDiscountItemDTO> dtoList = new ArrayList<>();
        for(StaticData staticData : staticDataList) {
            boolean isFound = false;
            OrderDiscountItemDTO dto = new OrderDiscountItemDTO();
            for(OrderDiscountItem orderDiscountItem : orderDiscountItemList) {
                if(staticData.getCodeValue().equals(orderDiscountItem.getDiscountItem())) {
                    BeanUtils.copyProperties(orderDiscountItem, dto);
                    isFound = true;
                    break;
                }
            }
            if(!isFound) {
                dto.setOrderId(orderId);
                dto.setEmployeeId(WebContextUtils.getUserContext().getEmployeeId());
                dto.setDiscountItem(staticData.getCodeValue());
                dto.setContractDiscountFee(0);
                dto.setSettleDiscountFee(0);
                dto.setStatus("1");
                dto.setCreateTime(LocalDateTime.now());
            }

            //转成中文描述
            dto.setEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getEmployeeId()));
            dto.setStatusName(staticDataService.getCodeName("DISCOUNT_ITEM_STATUS", dto.getStatus()));
            if(dto.getApproveEmployeeId() != null && dto.getApproveEmployeeId() > 0) {
                dto.setApproveEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getApproveEmployeeId()));
            }
            dto.setDiscountItemName(staticData.getCodeName());
            dtoList.add(dto);

        }

        return dtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(List<OrderDiscountItemDTO> dtoList) {
        List<OrderDiscountItem> list = new ArrayList<>();
        for(OrderDiscountItemDTO dto : dtoList) {
            OrderDiscountItem orderDiscountItem = null;
            if(dto.getId() != null && dto.getId() > 0) {
                orderDiscountItem = baseMapper.selectById(dto.getId());
            } else {
                orderDiscountItem = new OrderDiscountItem();
            }
            BeanUtils.copyProperties(dto, orderDiscountItem);
            list.add(orderDiscountItem);
        }

        this.saveOrUpdateBatch(list);
    }

    @Override
    public List<OrderDiscountItemDTO> queryByOrderId(Long orderId) {
        List<OrderDiscountItem> orderDiscountItemList = baseMapper.selectList((new QueryWrapper<OrderDiscountItem>().lambda()
                .eq(OrderDiscountItem::getOrderId, orderId)));
        if (ArrayUtils.isEmpty(orderDiscountItemList)) {
            return null;
        }

        List<OrderDiscountItemDTO> discountItems = new ArrayList<>();
        orderDiscountItemList.forEach(orderDiscountItem -> {
            OrderDiscountItemDTO discountItem = new OrderDiscountItemDTO();
            BeanUtils.copyProperties(orderDiscountItem, discountItem);
            discountItem.setEmployeeName(employeeService.getEmployeeNameEmployeeId(discountItem.getEmployeeId()));
            discountItem.setStatusName(staticDataService.getCodeName("DISCOUNT_ITEM_STATUS", discountItem.getStatus()));
            if(discountItem.getApproveEmployeeId() != null && discountItem.getApproveEmployeeId() > 0) {
                discountItem.setApproveEmployeeName(employeeService.getEmployeeNameEmployeeId(discountItem.getApproveEmployeeId()));
            }
            discountItem.setDiscountItemName(this.staticDataService.getCodeName("DISCOUNT_ITEM", discountItem.getDiscountItem()));
            discountItems.add(discountItem);
        });
        return discountItems;
    }
}
