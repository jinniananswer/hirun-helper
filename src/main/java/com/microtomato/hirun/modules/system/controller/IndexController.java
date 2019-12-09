package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.modules.system.service.INotifyQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Steven
 * @date 2019-12-09
 */
@RestController
@Slf4j
@RequestMapping("api/system/index")
public class IndexController {

    @Autowired
    private INotifyQueueService notifyQueueServiceImpl;

    /**
     * 查询是否有未读消息
     *
     * @return
     */
    @GetMapping("messageHint")
    public String messageHint() {
        boolean hint = notifyQueueServiceImpl.messageHint();
        return String.format("{\"code\": 0, \"hint\": %b}", hint);
    }

}
