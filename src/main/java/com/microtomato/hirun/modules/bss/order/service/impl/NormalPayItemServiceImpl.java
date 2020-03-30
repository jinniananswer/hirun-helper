package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.modules.bss.order.entity.po.NormalPayItem;
import com.microtomato.hirun.modules.bss.order.mapper.NormalPayItemMapper;
import com.microtomato.hirun.modules.bss.order.service.INormalPayItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 非主营支付项明细表 服务实现类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-30
 */
@Slf4j
@Service
public class NormalPayItemServiceImpl extends ServiceImpl<NormalPayItemMapper, NormalPayItem> implements INormalPayItemService {

}
