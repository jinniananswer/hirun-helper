package com.microtomato.hirun.modules.system.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.framework.websocket.ServerWebSocket;
import com.microtomato.hirun.modules.system.entity.po.Notify;
import com.microtomato.hirun.modules.system.service.INotifyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Steven
 * @date 2019-11-28
 */
@Slf4j
@Component
public class WebSocketTask {

    @Autowired
    private INotifyService notifyServiceImpl;

    //@Scheduled(initialDelay = 10000, fixedRate = 20000)
    public void scheduled() {
        List<Notify> list = notifyServiceImpl.list(Wrappers.<Notify>lambdaQuery().in(Notify::getId, 31, 32, 44, 45));
        Notify notify = list.get(RandomUtils.nextInt(0, list.size()));
        log.error("{}", notify.getContent());
        ServerWebSocket.sendMessage(1L, notify.getContent());
    }

}
