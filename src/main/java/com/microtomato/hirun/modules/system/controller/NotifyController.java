package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.system.service.INotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 消息表 前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-11-11
 */
@RestController
@Slf4j
@RequestMapping("/api/system/notify")
public class NotifyController {

    @Autowired
    private INotifyService notifyServiceImpl;

    @GetMapping("/testSendAnnounce")
    @RestResult
    public void sendAnnounce() {
        notifyServiceImpl.sendAnnounce("公告：上线在即，请各位小伙伴快马加鞭。");
    }

    @GetMapping("/testSendMessage")
    @RestResult
    public void sendMessage() {
        notifyServiceImpl.sendMessage(1473L, "这是一条测试消息");
    }

}
