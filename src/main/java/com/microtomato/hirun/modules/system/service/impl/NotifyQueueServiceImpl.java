package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.system.entity.po.NotifyQueue;
import com.microtomato.hirun.modules.system.mapper.NotifyQueueMapper;
import com.microtomato.hirun.modules.system.service.INotifyQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Autowired
    private NotifyQueueMapper notifyQueueMapper;

    /**
     * 从队列里获取当前用户最新数据的时间
     *
     * @return
     */
    @Override
    public LocalDateTime getNewestTimeByUserId() {
        UserContext userContext = WebContextUtils.getUserContext();
        Long userId = userContext.getUserId();

        LambdaQueryWrapper<NotifyQueue> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(NotifyQueue::getUserId, userId);
        lambdaQueryWrapper.orderByDesc(NotifyQueue::getCreateTime);
        NotifyQueue notifyQueue = notifyQueueMapper.selectOne(lambdaQueryWrapper);

        return notifyQueue.getCreateTime();
    }
}
