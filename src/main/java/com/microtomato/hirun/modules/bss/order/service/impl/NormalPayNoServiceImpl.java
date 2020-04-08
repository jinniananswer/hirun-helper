package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.order.entity.po.NormalPayNo;
import com.microtomato.hirun.modules.bss.order.mapper.NormalPayNoMapper;
import com.microtomato.hirun.modules.bss.order.service.INormalPayNoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 非主营支付流水表 服务实现类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-26
 */
@Slf4j
@Service
@DataSource(DataSourceKey.INS)
public class NormalPayNoServiceImpl extends ServiceImpl<NormalPayNoMapper, NormalPayNo> implements INormalPayNoService {

}
