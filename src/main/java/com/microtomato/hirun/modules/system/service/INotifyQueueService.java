package com.microtomato.hirun.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.system.entity.po.Notify;
import com.microtomato.hirun.modules.system.entity.po.NotifyQueue;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户消息队列 服务类
 * </p>
 *
 * @author Steven
 * @since 2019-11-11
 */
public interface INotifyQueueService extends IService<NotifyQueue> {

    /**
     * 获取用户队列里最新的时间戳
     *
     * @return
     */
    LocalDateTime getLatestTime();

    /**
     * 获取用户队列里最新的时间戳
     *
     * @return
     */
    LocalDateTime getLatestTime(Long employeeId);

    /**
     * 公告入队的操作
     */
    void announceEnqueue();

    /**
     * 私信的入队操作
     */
    void messageEnqueue();

    /**
     * 查用户的未读信息（包括：公告/私信/提醒）
     *
     * @return 未读消息列表
     */
    List<NotifyQueue> queryUnread();

    /**
     * 标记消息已读
     *
     * @param notifyId 消息Id
     */
    void markReaded(Long notifyId);

    /**
     * 标记消息已读
     *
     * @param idList
     */
    void markReaded(List<Long> idList);

    /**
     * 标记消息已读
     *
     * @param notifyId 消息Id
     * @param userId 用户Id
     */
    void markReaded(Long notifyId, Long userId);
}
