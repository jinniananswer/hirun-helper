package com.microtomato.hirun.modules.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.system.service.INotifySubscribeService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 消息订阅表 前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-11-11
 */
@RestController
@Slf4j
@RequestMapping("/api/system/notify-subscribe")
public class NotifySubscribeController {

    @Autowired
    private INotifySubscribeService notifySubscribeServiceImpl;



}
