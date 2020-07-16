package com.microtomato.hirun.modules.bss.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.service.entity.po.ServiceComplain;
import com.microtomato.hirun.modules.bss.service.mapper.ServiceComplainMapper;
import com.microtomato.hirun.modules.bss.service.service.IServiceComplainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (ServiceComplain)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
@Service
@Slf4j
public class ServiceComplainServiceImpl extends ServiceImpl<ServiceComplainMapper, ServiceComplain> implements IServiceComplainService {

    @Autowired
    private ServiceComplainMapper serviceComplainMapper;
    

}