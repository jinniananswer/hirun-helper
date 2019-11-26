package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.system.entity.po.Notify;
import com.microtomato.hirun.modules.system.entity.po.NotifyQueue;
import com.microtomato.hirun.modules.system.mapper.NotifyQueueMapper;
import com.microtomato.hirun.modules.system.service.INotifyQueueService;
import com.microtomato.hirun.modules.system.service.INotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private INotifyQueueService notifyQueueServiceImpl;

    private INotifyService notifyServiceImpl;

    /**
     * 从队列里获取当前用户最新数据的时间
     *
     * @return
     */
    @Override
    public LocalDateTime getLatestTimeByUserId() {
        UserContext userContext = WebContextUtils.getUserContext();
        Long userId = userContext.getUserId();
        return getLatestTimeByUserId(userId);
    }

    /**
     * 从队列里获取当前用户最新数据的时间
     *
     * @param userId 用户 Id
     * @return
     */
    @Override
    public LocalDateTime getLatestTimeByUserId(Long userId) {
        NotifyQueue notifyQueue = notifyQueueMapper.selectOne(
            Wrappers.<NotifyQueue>lambdaQuery()
                .eq(NotifyQueue::getUserId, userId)
                .orderByDesc(NotifyQueue::getCreateTime)
        );

        return notifyQueue.getCreateTime();
    }

    /**
     * 公告的入队操作
     */
    @Override
    public void announceEnqueue() {
        List<Notify> list = notifyServiceImpl.queryUnreadAnnounce();
        enqueue(list);
    }

    /**
     * 消息的入队操作
     */
    @Override
    public void messageEnqueue() {
        List<Notify> list = notifyServiceImpl.queryUnreadMessage();
        enqueue(list);
    }

    private void enqueue(List<Notify> list) {
        Long userId = WebContextUtils.getUserContext().getUserId();
        LocalDateTime now = LocalDateTime.now();

        for (Notify notify : list) {
            NotifyQueue notifyQueue = new NotifyQueue();
            notifyQueue.setNotifyId(notify.getId());
            notifyQueue.setUserId(userId);
            notifyQueue.setReaded(false);
            notifyQueue.setCreateTime(now);
            notifyQueueServiceImpl.save(notifyQueue);
        }
    }

    /**
     * 查用户的未读消息（包括：公告/私信/提醒）
     *
     * @return 未读消息列表
     */
    @Override
    public List<NotifyQueue> queryUnread() {
        Long userId = WebContextUtils.getUserContext().getUserId();
        return notifyQueueMapper.selectList(
            Wrappers.<NotifyQueue>lambdaQuery()
                .eq(NotifyQueue::getReaded, false)
                .eq(NotifyQueue::getUserId, userId)
        );
    }

    /**
     * 标记消息已读
     *
     * @param notifyId 消息Id
     */
    @Override
    public void markRead(Long notifyId) {
        Long userId = WebContextUtils.getUserContext().getUserId();
        markRead(notifyId, userId);
    }

    /**
     * 标记消息已读
     *
     * @param notifyId 消息Id
     * @param userId   用户Id
     */
    @Override
    public void markRead(Long notifyId, Long userId) {
        LambdaQueryWrapper<NotifyQueue> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(NotifyQueue::getNotifyId, notifyId);
        lambdaQueryWrapper.eq(NotifyQueue::getUserId, userId);

        NotifyQueue entity = new NotifyQueue();
        entity.setReaded(true);
        notifyQueueMapper.update(entity, lambdaQueryWrapper);
    }

}
