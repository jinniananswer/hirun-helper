package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.system.entity.dto.UnReadedDTO;
import com.microtomato.hirun.modules.system.service.INotifyQueueService;
import com.microtomato.hirun.modules.system.service.INotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 获取公告列表
     *
     * @return
     */
    @GetMapping("announce-list")
    @RestResult
    public List<UnReadedDTO> announceList() {
        List<UnReadedDTO> unreadedDTOS = notifyQueueServiceImpl.queryUnreadAnnounce();
        return unreadedDTOS;
    }

    @GetMapping("notice-list")
    @RestResult
    public List<UnReadedDTO> noticeList() {
        List<UnReadedDTO> unreadedDTOS = notifyQueueServiceImpl.queryUnreadNotice();
        return unreadedDTOS;
    }

    /**
     * 获取私信列表
     *
     * @return
     */
    @GetMapping("message-list")
    @RestResult
    public List<UnReadedDTO> messageList() {
        List<UnReadedDTO> messageDTOS = notifyQueueServiceImpl.queryUnreadMessage();
        return messageDTOS;
    }

    @PostMapping("markReaded")
    @RestResult
    public void markReaded(@RequestBody List<Long> list) {
        notifyQueueServiceImpl.markReaded(list);
    }

    @GetMapping("markReadedAll/{notifyType}")
    @RestResult
    public void markReadedAll(@PathVariable Integer notifyType) {
        if (notifyType == INotifyService.NotifyType.ANNOUNCE.value()) {
            notifyQueueServiceImpl.markReadedAll(INotifyService.NotifyType.ANNOUNCE);
        } else if (notifyType == INotifyService.NotifyType.NOTICE.value()) {
            notifyQueueServiceImpl.markReadedAll(INotifyService.NotifyType.NOTICE);
        } else if (notifyType == INotifyService.NotifyType.MESSAGE.value()) {
            notifyQueueServiceImpl.markReadedAll(INotifyService.NotifyType.MESSAGE);
        }
    }

    @GetMapping("announceEnqueue")
    @RestResult
    public void announceEnqueue() {
        notifyQueueServiceImpl.announceEnqueue();
    }

    /**
     * 首页 console 查看未读消息，不分类型
     *
     * @return
     */
    @GetMapping("query-unread-all")
    @RestResult
    public List<UnReadedDTO> queryUnreadAll() {
        List<UnReadedDTO> unReadedDTOS = notifyQueueServiceImpl.queryUnreadAll();
        return unReadedDTOS;
    }

}
