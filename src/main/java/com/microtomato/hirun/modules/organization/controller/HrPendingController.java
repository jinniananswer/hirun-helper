package com.microtomato.hirun.modules.organization.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.organization.service.IHrPendingService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2019-10-22
 */
@RestController
@Slf4j
@RequestMapping("/api/organization/hr-pending")
public class HrPendingController {

    @Autowired
    private IHrPendingService hrPendingServiceImpl;



}
