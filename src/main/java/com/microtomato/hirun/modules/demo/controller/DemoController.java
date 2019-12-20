package com.microtomato.hirun.modules.demo.controller;

import com.microtomato.hirun.modules.demo.service.IDemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-10-30
 */
@RestController
@Slf4j
@RequestMapping("/api/demo/demo")
public class DemoController {

    @Autowired
    private IDemoService demoServiceImpl;

    @GetMapping("/test")
    public void test() {
        demoServiceImpl.testJTA();
    }

}
