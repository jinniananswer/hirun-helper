package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.order.entity.po.NormalPayMoney;
import com.microtomato.hirun.modules.bss.order.mapper.NormalPayMoneyMapper;
import com.microtomato.hirun.modules.bss.order.service.INormalPayMoneyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 非主营付款类型明细表 服务实现类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-26
 */
@Slf4j
@Service
@DataSource(DataSourceKey.INS)
public class NormalPayMoneyServiceImpl extends ServiceImpl<NormalPayMoneyMapper, NormalPayMoney> implements INormalPayMoneyService {

}
