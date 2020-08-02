package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.security.Role;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.service.IOrderBaseService;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeePieStatisticDTO;
import com.microtomato.hirun.modules.organization.service.IHrPendingService;
import com.microtomato.hirun.modules.system.entity.dto.PendingTaskDTO;
import com.microtomato.hirun.modules.system.entity.po.RoleConsoleConfig;
import com.microtomato.hirun.modules.system.mapper.RoleConsoleConfigMapper;
import com.microtomato.hirun.modules.system.service.IRoleConsoleConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色控制台配置(RoleConsoleConfig)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-27 22:04:30
 */
@Service
@Slf4j
public class RoleConsoleConfigServiceImpl extends ServiceImpl<RoleConsoleConfigMapper, RoleConsoleConfig> implements IRoleConsoleConfigService {

    @Autowired
    private RoleConsoleConfigMapper roleConsoleConfigMapper;

    @Autowired
    private IHrPendingService hrPendingService;

    @Autowired
    private IOrderBaseService orderBaseService;

    /**
     * 查询员工角色与控制台的配置
     * @param roleId
     * @return
     */
    @Cacheable(value="role-console-cfg-roleId")
    @Override
    public RoleConsoleConfig getRoleConsole(Long roleId) {
        return this.getOne(Wrappers.<RoleConsoleConfig>lambdaQuery()
                .eq(RoleConsoleConfig::getRoleId, roleId), false);
    }

    /**
     * 根据角色ID列表查询控制台配置
     * @param roleIds
     * @return
     */
    @Cacheable(value="role-console-cfg-roleIds")
    @Override
    public List<RoleConsoleConfig> queryRoleConsole(List<Long> roleIds) {
        return this.list(Wrappers.<RoleConsoleConfig>lambdaQuery()
                .in(RoleConsoleConfig::getRoleId, roleIds));
    }

    /**
     * 根据角色列表查询控制台配置
     * @param roles
     * @return
     */
    @Cacheable(value="role-console-cfg-roles")
    @Override
    public List<RoleConsoleConfig> queryRoleConsoles(List<Role> roles) {
        if (ArrayUtils.isEmpty(roles)) {
            return null;
        }

        List<Long> roleIds = new ArrayList<>();
        roles.forEach(role -> {
            roleIds.add(role.getId());
        });

        IRoleConsoleConfigService configService = SpringContextUtils.getBean(RoleConsoleConfigServiceImpl.class);
        return configService.queryRoleConsole(roleIds);
    }

    /**
     * 查询登陆员工在控制台上的待办任务
     * @return
     */
    @Override
    public List<PendingTaskDTO> queryPendingTasks() {
        List<Role> roles = WebContextUtils.getUserContext().getRoles();

        IRoleConsoleConfigService configService = SpringContextUtils.getBean(RoleConsoleConfigServiceImpl.class);
        List<RoleConsoleConfig> consoleConfigs = configService.queryRoleConsoles(roles);

        if (ArrayUtils.isEmpty(consoleConfigs)) {
            return this.queryHrPendingTasks();
        }

        RoleConsoleConfig consoleConfig = consoleConfigs.get(0);
        String type = consoleConfig.getType();
        if (StringUtils.equals("1", type)) {
            return this.queryHrPendingTasks();
        } else if (StringUtils.equals(OrderConst.ORDER_TYPE_HOME, type) || StringUtils.equals(OrderConst.ORDER_TYPE_WOOD, type)) {
            //订单状态
            String orderStatuses = consoleConfig.getPendingType();
            return this.orderBaseService.queryOrderStatusPendingTasks(orderStatuses, type);
        }

        return null;
    }

    public List<PendingTaskDTO> queryHrPendingTasks() {
        UserContext userContext = WebContextUtils.getUserContext();
        Long employeeId=userContext.getEmployeeId();
        List<EmployeePieStatisticDTO> temps = this.hrPendingService.countPending(employeeId);
        if (ArrayUtils.isEmpty(temps)) {
            return null;
        }

        List<PendingTaskDTO> tasks = new ArrayList<>();
        temps.forEach(temp -> {
            PendingTaskDTO task = new PendingTaskDTO();
            task.setName(temp.getName());
            task.setNum(temp.getNum());
            task.setLink("openUrl?url=modules/organization/hr/pending_manager");
            tasks.add(task);
        });

        return tasks;
    }
}