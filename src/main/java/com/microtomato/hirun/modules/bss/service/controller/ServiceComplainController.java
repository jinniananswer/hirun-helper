package com.microtomato.hirun.modules.bss.service.controller;



import com.microtomato.hirun.modules.bss.service.service.IServiceComplainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * (ServiceComplain)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
@RestController
@RequestMapping("/api/bss.service/ServiceComplain")
public class ServiceComplainController {

    /**
     * 服务对象
     */
    @Autowired
    private IServiceComplainService serviceComplainService;


}