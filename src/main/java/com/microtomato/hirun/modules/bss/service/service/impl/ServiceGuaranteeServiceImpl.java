package com.microtomato.hirun.modules.bss.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerService;
import com.microtomato.hirun.modules.bss.service.entity.dto.GuaranteeDTO;
import com.microtomato.hirun.modules.bss.service.entity.po.ServiceGuarantee;
import com.microtomato.hirun.modules.bss.service.mapper.ServiceGuaranteeMapper;
import com.microtomato.hirun.modules.bss.service.service.IServiceGuaranteeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (ServiceGuarantee)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
@Service
@Slf4j
public class ServiceGuaranteeServiceImpl extends ServiceImpl<ServiceGuaranteeMapper, ServiceGuarantee> implements IServiceGuaranteeService {

    @Autowired
    private ServiceGuaranteeMapper serviceGuaranteeMapper;

    @Autowired
    private IOrderWorkerService workerService;


    @Override
    public GuaranteeDTO queryCustomerGuaranteeInfo(Long orderId) {
        ServiceGuarantee serviceGuarantee=this.baseMapper.selectOne(new QueryWrapper<ServiceGuarantee>().lambda()
                .eq(ServiceGuarantee::getOrderId,orderId));
        GuaranteeDTO guaranteeDTO=new GuaranteeDTO();

        if(serviceGuarantee==null){
            guaranteeDTO.setOrderId(orderId);
            return guaranteeDTO;
        }
        BeanUtils.copyProperties(serviceGuarantee,guaranteeDTO);
        return guaranteeDTO;
    }

    @Override
    public void saveGuaranteeInfo(GuaranteeDTO dto) {
        ServiceGuarantee serviceGuarantee=new ServiceGuarantee();
        BeanUtils.copyProperties(dto,serviceGuarantee);

        if(dto.getId()==null){
            this.baseMapper.insert(serviceGuarantee);
        }else{
           this.baseMapper.updateById(serviceGuarantee);
        }
    }
}