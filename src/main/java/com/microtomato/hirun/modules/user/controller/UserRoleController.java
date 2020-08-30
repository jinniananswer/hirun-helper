package com.microtomato.hirun.modules.user.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.Constants;
import com.microtomato.hirun.modules.user.entity.dto.GrantUserRoleDTO;
import com.microtomato.hirun.modules.user.entity.po.Role;
import com.microtomato.hirun.modules.user.entity.po.UserRole;
import com.microtomato.hirun.modules.user.service.IRoleService;
import com.microtomato.hirun.modules.user.service.IUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@RestController
@Slf4j
@RequestMapping("api/user/user-role")
public class UserRoleController {

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleService roleService;

    @PostMapping("grantRole")
    @RestResult
    public void grantRole(@RequestBody GrantUserRoleDTO grantUserRoleDTO) {
        log.debug("角色分配: {}", grantUserRoleDTO);

        List<Long> userIds = grantUserRoleDTO.getUserIds();
        List<Long> roleIds = grantUserRoleDTO.getRoleIds();

        userRoleService.grantRole(userIds, roleIds);
    }

    @PostMapping("revokeRole")
    @RestResult
    public void revokeRole(@RequestBody GrantUserRoleDTO grantUserRoleDTO) {
        log.debug("角色回收: {}", grantUserRoleDTO);

        List<Long> userIds = grantUserRoleDTO.getUserIds();
        List<Long> roleIds = grantUserRoleDTO.getRoleIds();

        userRoleService.revokeRole(userIds, roleIds);
    }


    @GetMapping("listRole")
    @RestResult
    public List<Role> listRole(String userId) {
        LocalDateTime now = LocalDateTime.now();
        List<UserRole> userRoleList = userRoleService.list(
            Wrappers.<UserRole>lambdaQuery()
                .select(UserRole::getRoleId)
                .eq(UserRole::getUserId, userId)
                .lt(UserRole::getStartDate, now)
                .gt(UserRole::getEndDate, now)
        );

        List<Role> roleList = new ArrayList<>();
        for (UserRole userRole : userRoleList) {
            Long roleId = userRole.getRoleId();
            Role role = roleService.getOne(
                Wrappers.<Role>lambdaQuery()
                    .select(Role::getRoleId, Role::getRoleName)
                    .eq(Role::getRoleId, roleId)
            );

            if (null != role) {
                roleList.add(role);
            }
        }

        // 添加默认权限
        roleList.add(Role.builder().roleId(Constants.DEFAULT_ROLE_ID).roleName("默认角色").build());
        return roleList;
    }

}
