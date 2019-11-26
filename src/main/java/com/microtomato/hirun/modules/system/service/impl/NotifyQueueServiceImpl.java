package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.system.entity.dto.AnnounceDTO;
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
    public LocalDateTime getLatestTime() {
        UserContext userContext = WebContextUtils.getUserContext();
        Long employeeId = userContext.getEmployeeId();
        return getLatestTime(employeeId);
    }

    /**
     * 从队列里获取当前用户最新数据的时间
     *
     * @param employeeId 用户 Id
     * @return
     */
    @Override
    public LocalDateTime getLatestTime(Long employeeId) {
        NotifyQueue notifyQueue = notifyQueueMapper.selectOne(
            Wrappers.<NotifyQueue>lambdaQuery()
                .eq(NotifyQueue::getEmployeeId, employeeId)
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
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        LocalDateTime now = LocalDateTime.now();

        for (Notify notify : list) {
            NotifyQueue notifyQueue = new NotifyQueue();
            notifyQueue.setNotifyId(notify.getId());
            notifyQueue.setEmployeeId(employeeId);
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
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        return notifyQueueMapper.selectList(
            Wrappers.<NotifyQueue>lambdaQuery()
                .eq(NotifyQueue::getReaded, false)
                .eq(NotifyQueue::getEmployeeId, employeeId)
        );
    }

    /**
     * 查未读公告
     *
     * @return
     */
    @Override
    public List<AnnounceDTO> queryUnreadAnnounce() {
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        return notifyQueueMapper.queryUnreadAnnounce(INotifyService.NotifyType.REMIND.value(), employeeId);
    }

    /**
     * 标记消息已读
     *
     * @param notifyId 消息Id
     */
    @Override
    public void markReaded(Long notifyId) {
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        markReaded(notifyId, employeeId);
    }

    /**
     * 标记消息已读
     *
     * @param idList
     */
    @Override
    public void markReaded(List<Long> idList) {
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        NotifyQueue entity = new NotifyQueue();
        entity.setReaded(true);
        notifyQueueMapper.update(entity, Wrappers.<NotifyQueue>lambdaUpdate()
            .eq(NotifyQueue::getEmployeeId, employeeId)
            .in(NotifyQueue::getId, idList)
        );

    }

    /**
     * 标记消息已读
     *
     * @param notifyId   消息Id
     * @param employeeId 雇员Id
     */
    @Override
    public void markReaded(Long notifyId, Long employeeId) {
        LambdaQueryWrapper<NotifyQueue> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(NotifyQueue::getNotifyId, notifyId);
        lambdaQueryWrapper.eq(NotifyQueue::getEmployeeId, employeeId);

        NotifyQueue entity = new NotifyQueue();
        entity.setReaded(true);
        notifyQueueMapper.update(entity, lambdaQueryWrapper);
    }

    /**
     * 标记全部已读
     */
    @Override
    public void markReadedAll() {
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        NotifyQueue entity = new NotifyQueue();
        entity.setReaded(true);

        notifyQueueMapper.update(entity, Wrappers.<NotifyQueue>lambdaUpdate().eq(NotifyQueue::getEmployeeId, employeeId));
    }

}
