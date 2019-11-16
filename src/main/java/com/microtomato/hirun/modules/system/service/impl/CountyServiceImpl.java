package com.microtomato.hirun.modules.system.service.impl;

import com.microtomato.hirun.modules.system.entity.po.County;
import com.microtomato.hirun.modules.system.mapper.CountyMapper;
import com.microtomato.hirun.modules.system.service.ICountyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-11-15
 */
@Slf4j
@Service
public class CountyServiceImpl extends ServiceImpl<CountyMapper, County> implements ICountyService {

}
