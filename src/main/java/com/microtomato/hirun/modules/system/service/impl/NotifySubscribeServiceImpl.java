package com.microtomato.hirun.modules.system.service.impl;

import com.microtomato.hirun.modules.system.entity.po.NotifySubscribe;
import com.microtomato.hirun.modules.system.mapper.NotifySubscribeMapper;
import com.microtomato.hirun.modules.system.service.INotifySubscribeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 消息订阅表 服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-11-11
 */
@Slf4j
@Service
public class NotifySubscribeServiceImpl extends ServiceImpl<NotifySubscribeMapper, NotifySubscribe> implements INotifySubscribeService {

}
