package com.microtomato.hirun.modules.system.service.impl;

import com.microtomato.hirun.modules.system.entity.po.NotifyQueue;
import com.microtomato.hirun.modules.system.mapper.NotifyQueueMapper;
import com.microtomato.hirun.modules.system.service.INotifyQueueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 用户消息队列 服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-11-11
 */
@Slf4j
@Service
public class NotifyQueueServiceImpl extends ServiceImpl<NotifyQueueMapper, NotifyQueue> implements INotifyQueueService {

}
