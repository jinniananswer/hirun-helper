package com.microtomato.hirun.modules.user.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.user.service.IMenuRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色下挂菜单 前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@RestController
@Slf4j
@RequestMapping("api/user/menu-role")
public class MenuRoleController {

    @Autowired
    private IMenuRoleService menuRoleServiceImpl;

    @PostMapping("updateMenuRole/{roleId}")
    @RestResult
    public void updateMenuRole(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
        menuRoleServiceImpl.updateMenuRole(roleId, menuIds);
    }

}
