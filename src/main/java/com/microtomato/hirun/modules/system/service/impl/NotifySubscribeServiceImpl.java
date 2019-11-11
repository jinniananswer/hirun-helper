package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.system.entity.po.NotifySubscribe;
import com.microtomato.hirun.modules.system.mapper.NotifySubscribeMapper;
import com.microtomato.hirun.modules.system.service.INotifySubscribeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

        LambdaQueryWrapper<NotifySubscribe> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(NotifySubscribe::getUserId, userId);
        lambdaQueryWrapper.eq(NotifySubscribe::isEnable, true);

        List<NotifySubscribe> notifySubscribes = notifySubscribeMapper.selectList(lambdaQueryWrapper);
        return notifySubscribes;
    }

}
