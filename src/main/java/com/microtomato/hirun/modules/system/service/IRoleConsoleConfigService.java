package com.microtomato.hirun.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.framework.security.Role;
import com.microtomato.hirun.modules.system.entity.dto.PendingTaskDTO;
import com.microtomato.hirun.modules.system.entity.po.RoleConsoleConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * 角色控制台配置(RoleConsoleConfig)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-27 22:04:30
 */
public interface IRoleConsoleConfigService extends IService<RoleConsoleConfig> {

    RoleConsoleConfig getRoleConsole(Long roleId);

    @Cacheable(value="role-console-cfg-roleIds")
    List<RoleConsoleConfig> queryRoleConsole(List<Long> roleIds);

    List<RoleConsoleConfig> queryRoleConsoles(List<Role> roles);

    List<PendingTaskDTO> queryPendingTasks();
}