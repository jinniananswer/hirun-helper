package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.system.entity.po.Notify;
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
        long userId = WebContextUtils.getUserContext().getUserId();

        Notify notify = new Notify();
        notify.setContent(content);
        notify.setNotifyType(NotifyType.ANNOUNCE.value());
        notify.setSenderId(userId);

        notifyMapper.insert(notify);
    }

    /**
     * 查未读公告
     *
     * @return
     */
    @Override
    public List<Notify> queryUnreadAnnounce() {
        long userId = WebContextUtils.getUserContext().getUserId();
        return queryUnreadAnnounce(userId);
    }

    /**
     * 查未读公告
     *
     * @param userId
     * @return
     */
    @Override
    public List<Notify> queryUnreadAnnounce(Long userId) {
        LocalDateTime latest = notifyQueueServiceImpl.getLatestTimeByUserId(userId);
        List<Notify> announceList = notifyMapper.selectList(
            Wrappers.<Notify>lambdaQuery()
                .select(Notify::getId)
                .eq(Notify::getNotifyType, NotifyType.ANNOUNCE)
                .gt(Notify::getCreateTime, latest)
        );
        return announceList;
    }

    /**
     * 发送私信
     *
     * @param toUserId 目标用户Id
     * @param content 私信内容
     */
    @Override
    public void sendMessage(Long toUserId, String content) {
        long userId = WebContextUtils.getUserContext().getUserId();
        sendMessage(toUserId, content, userId);
    }

    /**
     * 发送私信
     *
     * @param toUserId 给谁发私信
     * @param content 私信内容
     * @param fromUserId 谁发的私信
     */
    @Override
    public void sendMessage(Long toUserId, String content, Long fromUserId) {
        Notify notify = new Notify();
        notify.setContent(content);
        notify.setNotifyType(NotifyType.MESSAGE.value());
        notify.setTargetId(toUserId);
        notify.setSenderId(fromUserId);
        notifyMapper.insert(notify);
    }

    /**
     * 查未读私信
     *
     * @return
     */
    @Override
    public List<Notify> queryUnreadMessage() {
        long userId = WebContextUtils.getUserContext().getUserId();
        return queryUnreadMessage(userId);
    }

    /**
     * 查未读私信
     *
     * @param userId 用户Id
     * @return
     */
    @Override
    public List<Notify> queryUnreadMessage(Long userId) {
        LocalDateTime latest = notifyQueueServiceImpl.getLatestTimeByUserId(userId);
        List<Notify> messageList = notifyMapper.selectList(
            Wrappers.<Notify>lambdaQuery()
                .select(Notify::getId)
                .eq(Notify::getNotifyType, NotifyType.MESSAGE)
                .gt(Notify::getCreateTime, latest)
        );
        return messageList;
    }

}
