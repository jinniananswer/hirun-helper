package com.microtomato.hirun.modules.bss.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.service.entity.po.ServiceGuarantee;
import com.microtomato.hirun.modules.bss.service.mapper.ServiceGuaranteeMapper;
import com.microtomato.hirun.modules.bss.service.service.IServiceGuaranteeService;
import lombok.extern.slf4j.Slf4j;
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
    

}