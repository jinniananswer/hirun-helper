package com.microtomato.hirun.modules.system.service;

import com.microtomato.hirun.modules.system.entity.po.NotifyQueue;
import com.baomidou.mybatisplus.extension.service.IService;

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
    LocalDateTime getNewestTimeByUserId();

    /**
     * 消息入队操作
     */
    void enqueue();

    /**
     * 查用户的未读消息
     * @return 未读消息列表
     */
    List<NotifyQueue> queryUnReadNotifyFromQueue();

    /**
     * 标记消息已读
     *
     * @param notifyId 消息ID
     */
    void markReadByNotifyId(Long notifyId);
}
