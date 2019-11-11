package com.microtomato.hirun.modules.system.service;

import com.microtomato.hirun.modules.system.entity.po.NotifySubscribe;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 消息订阅表 服务类
 * </p>
 *
 * @author Steven
 * @since 2019-11-11
 */
public interface INotifySubscribeService extends IService<NotifySubscribe> {

    /**
     * 查用户的订阅信息
     * @return 订阅信息
     */
    List<NotifySubscribe> queryNotifySubscribeByUserId();

}
