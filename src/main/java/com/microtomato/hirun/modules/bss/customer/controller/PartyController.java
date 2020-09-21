package com.microtomato.hirun.modules.bss.customer.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.customer.service.IPartyService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 客户信息 前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2020-04-20
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.customer/party")
public class PartyController {

    @Autowired
    private IPartyService partyServiceImpl;

}
