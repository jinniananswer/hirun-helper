package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderMaterialContractDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.QueryMaterialCondDTO;
import com.microtomato.hirun.modules.bss.order.mapper.OrderMaterialContractMapper;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderMaterialContract;
import com.microtomato.hirun.modules.bss.order.service.IOrderDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderMaterialContractService;
import com.microtomato.hirun.modules.bss.service.entity.dto.QueryComplainCondDTO;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * (OrderMaterialContract)表服务实现类
 *
 * @author
 * @version 1.0.0
 * @date 2020-09-24 00:05:42
 */
@Service("orderMaterialContractService")
public class OrderMaterialContractServiceImpl extends ServiceImpl<OrderMaterialContractMapper, OrderMaterialContract> implements IOrderMaterialContractService {

    @Autowired
    private OrderMaterialContractMapper orderMaterialContractMapper;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IOrderDomainService orderDomainService;

    @Override
    public List<OrderMaterialContractDTO> queryMaterialContracts(QueryMaterialCondDTO condDTO) {

        QueryWrapper<QueryComplainCondDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(condDTO.getCustName()),"a.cust_name",condDTO.getCustName());
        queryWrapper.eq(condDTO.getHouseId()!=null,"b.houses_id",condDTO.getHouseId());

        queryWrapper.apply("a.cust_id=b.cust_id and b.order_id=c.order_id");

        queryWrapper.apply(condDTO.getAgentEmployeeId()!=null,
                " EXISTS (select 1 from order_worker d where d.order_id=b.order_id and end_date > now() " +
                        " and role_id='15' and d.employee_id="+condDTO.getAgentEmployeeId()+")");

        queryWrapper.apply(condDTO.getDesignEmployeeId()!=null,
                " EXISTS (select 1 from order_worker e where e.order_id=b.order_id and end_date > now() " +
                        " and role_id='30' and e.employee_id="+condDTO.getDesignEmployeeId()+")");

        List<OrderMaterialContractDTO> list=this.baseMapper.queryMaterialContracts(queryWrapper);
        if(ArrayUtils.isEmpty(list)){
            return null;
        }
        for(OrderMaterialContractDTO dto:list){
            dto.setAgentName(orderDomainService.getUsualOrderWorker(dto.getOrderId()).getAgentName());
            dto.setDesignName(orderDomainService.getUsualOrderWorker(dto.getOrderId()).getDesignerName());
        }
        return list;
    }
}