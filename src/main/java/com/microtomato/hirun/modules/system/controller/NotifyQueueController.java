package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.system.entity.dto.AnnounceDTO;
import com.microtomato.hirun.modules.system.entity.po.NotifyQueue;
import com.microtomato.hirun.modules.system.service.INotifyService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.system.service.INotifyQueueService;

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


    @GetMapping("announce-list")
    @RestResult
    public List<AnnounceDTO> announceList() {
        List<AnnounceDTO> announceDTOS = notifyQueueServiceImpl.queryUnreadAnnounce();
        return announceDTOS;
    }

    @GetMapping("message-list")
    @RestResult
    public List<NotifyQueue> messageList() {
        List<NotifyQueue> notifyQueues = notifyQueueServiceImpl.queryUnread();
        return notifyQueues;
    }

    @PostMapping("markReaded")
    @RestResult
    public void markReaded(@RequestBody List<Long> list) {
        notifyQueueServiceImpl.markReaded(list);
    }

    @GetMapping("markReadedAll")
    @RestResult
    public void markReadedAll() {
        notifyQueueServiceImpl.markReadedAll();
    }

}
