package com.microtomato.hirun.modules.system.controller;


import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.system.entity.dto.PendingTaskDTO;
import com.microtomato.hirun.modules.system.service.IRoleConsoleConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色控制台配置(RoleConsoleConfig)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-27 22:04:30
 */
@RestController
@RequestMapping("/api/system/role-console-config")
public class RoleConsoleConfigController {

    /**
     * 服务对象
     */
    @Autowired
    private IRoleConsoleConfigService roleConsoleConfigService;

    @RestResult
    @PostMapping("/queryPendingTasks")
    public List<PendingTaskDTO> queryPendingTasks() {
        return this.roleConsoleConfigService.queryPendingTasks();
    }
}