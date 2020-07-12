package com.microtomato.hirun.modules.bss.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.service.entity.po.ServiceRepairOrder;
import com.microtomato.hirun.modules.bss.service.mapper.ServiceRepairOrderMapper;
import com.microtomato.hirun.modules.bss.service.service.IServiceRepairOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (ServiceRepairOrder)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
@Service
@Slf4j
public class ServiceRepairOrderServiceImpl extends ServiceImpl<ServiceRepairOrderMapper, ServiceRepairOrder> implements IServiceRepairOrderService {

    @Autowired
    private ServiceRepairOrderMapper serviceRepairOrderMapper;
    

}