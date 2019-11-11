package com.microtomato.hirun.modules.system.service.impl;

import com.microtomato.hirun.modules.system.entity.po.Notify;
import com.microtomato.hirun.modules.system.mapper.NotifyMapper;
import com.microtomato.hirun.modules.system.service.INotifyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

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

}
