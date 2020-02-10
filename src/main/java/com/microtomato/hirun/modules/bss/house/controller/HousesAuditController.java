package com.microtomato.hirun.modules.bss.house.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.house.service.IHousesAuditService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2020-02-11
 */
@RestController
@Slf4j
@RequestMapping("/api/house/houses-audit")
public class HousesAuditController {

    @Autowired
    private IHousesAuditService housesAuditServiceImpl;



}
