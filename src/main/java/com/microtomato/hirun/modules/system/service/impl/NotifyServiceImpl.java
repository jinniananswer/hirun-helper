package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.system.entity.po.Notify;
import com.microtomato.hirun.modules.system.entity.po.NotifyQueue;
import com.microtomato.hirun.modules.system.mapper.NotifyMapper;
import com.microtomato.hirun.modules.system.service.INotifyQueueService;
import com.microtomato.hirun.modules.system.service.INotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 消息表 服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-11-11
 */
@Slf4j
@Service
public class NotifyServiceImpl extends ServiceImpl<NotifyMapper, Notify> implements INotifyService {

    @Autowired
    private NotifyMapper notifyMapper;

    @Autowired
    private INotifyQueueService notifyQueueServiceImpl;

    /**
     * 发送公告
     *
     * @param content 公告内容
     */
    @Override
    public void sendAnnounce(String content) {
        long employeeId = WebContextUtils.getUserContext().getEmployeeId();

        Notify notify = new Notify();
        notify.setContent(content);
        notify.setNotifyType(NotifyType.ANNOUNCE.value());
        notify.setSenderId(employeeId);

        notifyMapper.insert(notify);
    }

    /**
     * 删除公告
     *
     * @param idList
     */
    @Override
    public void deleteAnnounce(List<Long> idList) {
        notifyMapper.deleteBatchIds(idList);
        notifyQueueServiceImpl.remove(Wrappers.<NotifyQueue>lambdaUpdate().in(NotifyQueue::getNotifyId, idList));
    }

    /**
     * 查未读公告
     *
     * @return
     */
    @Override
    public List<Notify> queryUnreadAnnounce() {
        long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        return queryUnreadAnnounce(employeeId);
    }

    /**
     * 查未读公告
     *
     * @param userId
     * @return
     */
    @Override
    public List<Notify> queryUnreadAnnounce(Long userId) {
        LocalDateTime latest = notifyQueueServiceImpl.getLatestTime(userId);
        List<Notify> announceList = notifyMapper.selectList(
            Wrappers.<Notify>lambdaQuery()
                .select(Notify::getId)
                .eq(Notify::getNotifyType, NotifyType.ANNOUNCE.value())
                .gt(Notify::getCreateTime, latest)
        );
        return announceList;
    }

    /**
     * 发送私信
     *
     * @param toEmployeeId 目标用户Id
     * @param content 私信内容
     */
    @Override
    public void sendMessage(Long toEmployeeId, String content) {
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        sendMessage(toEmployeeId, content, employeeId);
    }

    /**
     * 发送私信
     *
     * @param toEmployeeId 给谁发私信
     * @param content 私信内容
     * @param fromEmployeeId 谁发的私信
     */
    @Override
    public void sendMessage(Long toEmployeeId, String content, Long fromEmployeeId) {
        Notify notify = new Notify();
        notify.setContent(content);
        notify.setNotifyType(NotifyType.MESSAGE.value());
        notify.setTargetId(toEmployeeId);
        notify.setSenderId(fromEmployeeId);
        notifyMapper.insert(notify);

        NotifyQueue notifyQueue = new NotifyQueue();
        notifyQueue.setReaded(false);
        notifyQueue.setEmployeeId(toEmployeeId);
        notifyQueue.setNotifyId(notify.getId());
        notifyQueueServiceImpl.save(notifyQueue);
    }

    /**
     * 查未读私信
     *
     * @return
     */
    @Override
    public List<Notify> queryUnreadMessage() {
        long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        return queryUnreadMessage(employeeId);
    }

    /**
     * 查未读私信
     *
     * @param employeeId 雇员Id
     * @return
     */
    @Override
    public List<Notify> queryUnreadMessage(Long employeeId) {
        LocalDateTime latest = notifyQueueServiceImpl.getLatestTime(employeeId);
        List<Notify> messageList = notifyMapper.selectList(
            Wrappers.<Notify>lambdaQuery()
                .select(Notify::getId)
                .eq(Notify::getNotifyType, NotifyType.MESSAGE.value())
                .gt(Notify::getCreateTime, latest)
        );
        return messageList;
    }

}
