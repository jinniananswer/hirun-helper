package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderDiscountItemDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderDiscountItem;
import com.microtomato.hirun.modules.bss.order.mapper.OrderDiscountItemMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderDiscountItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.organization.service.impl.EmployeeServiceImpl;
import com.microtomato.hirun.modules.system.entity.po.StaticData;
import com.microtomato.hirun.modules.system.service.impl.StaticDataServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
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

        this.saveBatch(list);
    }
}
