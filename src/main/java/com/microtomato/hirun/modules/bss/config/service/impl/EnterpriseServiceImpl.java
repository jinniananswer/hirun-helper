package com.microtomato.hirun.modules.bss.config.service.impl;

import com.microtomato.hirun.modules.bss.config.entity.po.Enterprise;
import com.microtomato.hirun.modules.bss.config.mapper.EnterpriseMapper;
import com.microtomato.hirun.modules.bss.config.service.IEnterpriseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-10
 */
@Slf4j
@Service
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseMapper, Enterprise> implements IEnterpriseService {

}
