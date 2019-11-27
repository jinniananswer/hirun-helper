package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.system.service.INotifyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/sendAnnounce")
    @RestResult
    public void sendAnnounce(@RequestBody String content) {
        log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.error(content);

        content = StringEscapeUtils.unescapeJava(content);
        content = StringUtils.strip(content, "\"");
        log.error(content);
        notifyServiceImpl.sendAnnounce(content);
    }

    @GetMapping("/sendMessage")
    @RestResult
    public void sendMessage() {
        notifyServiceImpl.sendMessage(1473L, "这是一条测试消息");
    }

}
