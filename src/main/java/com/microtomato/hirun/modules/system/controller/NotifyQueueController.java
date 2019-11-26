package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.system.entity.po.NotifyQueue;
import com.microtomato.hirun.modules.system.service.INotifyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.system.service.INotifyQueueService;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户消息队列 前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-11-11
 */
@RestController
@Slf4j
@RequestMapping("/api/system/notify-queue")
public class NotifyQueueController {

    @Autowired
    private INotifyQueueService notifyQueueServiceImpl;

    @Autowired
    private INotifyService notifyServiceImpl;

    @GetMapping("message-list")
    @RestResult
    public List<NotifyQueue> test() {
        List<NotifyQueue> notifyQueues = notifyQueueServiceImpl.queryUnread();
        return notifyQueues;
    }

}
