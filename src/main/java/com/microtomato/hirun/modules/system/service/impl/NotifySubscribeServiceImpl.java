package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.system.entity.po.NotifySubscribe;
import com.microtomato.hirun.modules.system.mapper.NotifySubscribeMapper;
import com.microtomato.hirun.modules.system.service.INotifySubscribeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.microtomato.hirun.modules.system.service.INotifyService.Action;
import static com.microtomato.hirun.modules.system.service.INotifyService.TargetType;

/**
 * <p>
 * 消息订阅表 服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-11-11
 */
@Slf4j
@Service
public class NotifySubscribeServiceImpl extends ServiceImpl<NotifySubscribeMapper, NotifySubscribe> implements INotifySubscribeService {

    @Autowired
    private NotifySubscribeMapper notifySubscribeMapper;

    /**
     * 查某个用户的所有订阅数据
     *
     * @return
     */
    @Override
    public List<NotifySubscribe> queryNotifySubscribeByUserId() {
        Long userId = WebContextUtils.getUserContext().getUserId();

        List<NotifySubscribe> notifySubscribes = notifySubscribeMapper.selectList(
            Wrappers.<NotifySubscribe>lambdaQuery()
                .eq(NotifySubscribe::getUserId, userId)
                .eq(NotifySubscribe::isEnable, true)
        );
        return notifySubscribes;
    }

    /**
     * 新增订阅信息
     *
     * @param targetId   目标 Id
     * @param targetType 目标类型
     * @param action     动作类型
     */
    @Override
    public void addSubscribe(long targetId, TargetType targetType, Action action) {
        Long userId = WebContextUtils.getUserContext().getUserId();

        NotifySubscribe notifySubscribe = NotifySubscribe.builder()
            .targetId(targetId)
            .targetType(targetType.value())
            .action(action.value())
            .userId(userId)
            .enable(true)
            .build();

        notifySubscribeMapper.insert(notifySubscribe);
    }

}
