package com.microtomato.hirun.modules.bss.config.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.config.entity.po.PayItemCfg;
import com.microtomato.hirun.modules.bss.config.service.IPayItemCfgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 收款项配置表 前端控制器
 * </p>
 *
 * @author jinnian
 * @since 2020-02-23
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.config/pay-item-cfg")
public class PayItemCfgController {

    @Autowired
    private IPayItemCfgService payItemCfgServiceImpl;


    @GetMapping("/queryAll")
    @RestResult
    public List<PayItemCfg> queryAll() {
        return this.payItemCfgServiceImpl.queryAll();
    }
}
