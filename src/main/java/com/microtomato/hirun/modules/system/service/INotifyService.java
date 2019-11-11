package com.microtomato.hirun.modules.system.service;

import com.microtomato.hirun.modules.system.entity.po.Notify;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.system.entity.po.NotifySubscribe;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 消息表 服务类
 * </p>
 *
 * @author Steven
 * @since 2019-11-11
 */
public interface INotifyService extends IService<Notify> {

    /**
     * 根据订阅信息，查询消息数据
     *
     * @param notifySubscribe 订阅信息
     * @param createTime 时间
     * @return 消息
     */
    List<Notify> queryNotifyByNotifySubscribe(NotifySubscribe notifySubscribe, LocalDateTime createTime);

}
