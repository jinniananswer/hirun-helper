package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.system.entity.po.Notify;
import com.microtomato.hirun.modules.system.entity.po.NotifySubscribe;
import com.microtomato.hirun.modules.system.mapper.NotifyMapper;
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
     * 发送提醒
     *
     * @param content 提醒内容，可以为空
     * @param targetId 目标 ID
     * @param targetType 目标类型
     * @param action 提醒的动作类型
     *
     * @see INotifyService.NotifyType 消息类型
     * @see INotifyService.Action 提醒的动作类型枚举类
     */
    @Override
    public void sendRemind(String content, long targetId, TargetType targetType, Action action) {
        long userId = WebContextUtils.getUserContext().getUserId();

        Notify notify = new Notify();
        notify.setNotifyType(NotifyType.REMIND.value());
        notify.setContent(content);
        notify.setTargetId(targetId);
        notify.setTargetType(targetType.value());
        notify.setAction(action.value());
        notify.setSenderId(userId);

        notifyMapper.insert(notify);
    }

    @Override
    public List<Notify> queryNotifyByNotifySubscribe(NotifySubscribe notifySubscribe, LocalDateTime createTime) {
        LambdaQueryWrapper<Notify> lambdaQueryWrapper = Wrappers.lambdaQuery();

        lambdaQueryWrapper.eq(Notify::getTargetId, notifySubscribe.getTargetId());
        lambdaQueryWrapper.eq(Notify::getTargetType, notifySubscribe.getTargetType());
        lambdaQueryWrapper.eq(Notify::getAction, notifySubscribe.getAction());
        lambdaQueryWrapper.gt(Notify::getCreateTime, createTime);

        return notifyMapper.selectList(lambdaQueryWrapper);
    }

}
