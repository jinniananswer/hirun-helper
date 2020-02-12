package com.microtomato.hirun.modules.bss.config.controller;

import com.microtomato.hirun.modules.bss.config.service.IRoleAttentionStatusCfgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色关注的订单状态配置表 前端控制器
 * </p>
 *
 * @author jinnian
 * @since 2020-02-11
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.config/role-attention-status-cfg")
public class RoleAttentionStatusCfgController {

    @Autowired
    private IRoleAttentionStatusCfgService roleAttentionStatusCfgServiceImpl;



}
