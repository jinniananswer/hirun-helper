package com.microtomato.hirun.modules.user.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.user.entity.po.Role;
import com.microtomato.hirun.modules.user.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色表
 * (归属组织的角色，职级的角色等) 前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@RestController
@Slf4j
@RequestMapping("api/user/role")
public class RoleController {

    @Autowired
    private IRoleService roleServiceImpl;

    /**
     * 前端加载所有角色数据
     *
     * @return
     */
    @GetMapping("/loadRole")
    @RestResult
    public List<Role> loadRole() {
        return roleServiceImpl.loadRole();
    }

    /**
     * 根据角色名查询
     *
     * @param rolename 角色名
     * @return
     */
    @GetMapping("role-list")
    @RestResult
    public List<Role> roleList(String rolename) {

        // 超级工号不展示
        List<Role> roleList = roleServiceImpl.queryRole(rolename);
        return roleList;

    }

    @GetMapping("delete-role/{roleId}")
    @RestResult
    public void deleteRole(@PathVariable Long roleId) {
        roleServiceImpl.deleteRole(roleId);
    }

    @GetMapping("active-role/{roleId}")
    @RestResult
    public void activeRole(@PathVariable Long roleId) {
        roleServiceImpl.activeRole(roleId);
    }

    @PostMapping("create")
    @RestResult
    public void createRole(@RequestBody Role role) {
        log.debug("创建新的角色：{}", role);
        role.setEnabled(true);
        roleServiceImpl.createRole(role);
    }

    @PostMapping("edit")
    @RestResult
    public void editRole(@RequestBody Role role) {
        log.debug("修改角色：{}", role);
        role.setEnabled(true);
        roleServiceImpl.editRole(role);
    }

}
