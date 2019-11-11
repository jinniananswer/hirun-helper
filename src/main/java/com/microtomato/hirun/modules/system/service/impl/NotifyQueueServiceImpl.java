package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.system.entity.po.Notify;
import com.microtomato.hirun.modules.system.entity.po.NotifyQueue;
import com.microtomato.hirun.modules.system.entity.po.NotifySubscribe;
import com.microtomato.hirun.modules.system.mapper.NotifyQueueMapper;
import com.microtomato.hirun.modules.system.service.INotifyQueueService;
import com.microtomato.hirun.modules.system.service.INotifyService;
import com.microtomato.hirun.modules.system.service.INotifySubscribeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private INotifyService notifyServiceImpl;

    @Autowired
    private INotifyQueueService notifyQueueServiceImpl;

    @Autowired
    private INotifySubscribeService notifySubscribeServiceImpl;

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

    @Override
    public void enqueue() {

        List<Notify> rtn = new ArrayList<>(32);

        Long userId = WebContextUtils.getUserContext().getUserId();
        LocalDateTime localDateTime = getNewestTimeByUserId();

        // 查询用户的订阅信息
        List<NotifySubscribe> notifySubscribes = notifySubscribeServiceImpl.queryNotifySubscribeByUserId();
        for (NotifySubscribe notifySubscribe : notifySubscribes) {
            // 根据订阅信息和时间戳，查对应的消息数据
            List<Notify> notifies = notifyServiceImpl.queryNotifyByNotifySubscribe(notifySubscribe, localDateTime);
            rtn.addAll(notifies);
        }

        // 消息数据的入队操作
        for (Notify notify : rtn) {
            NotifyQueue notifyQueue = new NotifyQueue();
            notifyQueue.setRead(false);
            notifyQueue.setUserId(userId);
            notifyQueue.setNotifyId(notify.getId());
            notifyQueue.setCreateTime(notify.getCreateTime());
            notifyQueueServiceImpl.save(notifyQueue);
        }
    }

    /**
     * 查用户的未读消息
     *
     * @return 未读消息列表
     */
    @Override
    public List<NotifyQueue> queryUnReadNotifyFromQueue() {

        Long userId = WebContextUtils.getUserContext().getUserId();

        LambdaQueryWrapper<NotifyQueue> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(NotifyQueue::getRead, false);
        lambdaQueryWrapper.eq(NotifyQueue::getUserId, userId);

        return notifyQueueMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 标记消息已读
     *
     * @param notifyId 消息ID
     */
    @Override
    public void markReadByNotifyId(Long notifyId) {
        Long userId = WebContextUtils.getUserContext().getUserId();

        LambdaQueryWrapper<NotifyQueue> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(NotifyQueue::getNotifyId, notifyId);
        lambdaQueryWrapper.eq(NotifyQueue::getUserId, userId);

        NotifyQueue entity = new NotifyQueue();
        entity.setRead(true);
        notifyQueueMapper.update(entity, lambdaQueryWrapper);
    }

}
