package com.microtomato.hirun.modules.user.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.user.service.IFuncRoleService;

import java.util.List;

/**
 * <p>
 * 角色权限关系表 前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@RestController
@Slf4j
@RequestMapping("api/user/func-role")
public class FuncRoleController {

    @Autowired
    private IFuncRoleService funcRoleServiceImpl;

    @PostMapping("updateFuncRole/{roleId}")
    @RestResult
    public void updateMenuRole(@PathVariable Long roleId, @RequestBody List<Long> funcIds) {
        funcRoleServiceImpl.updateFuncRole(roleId, funcIds);
    }

}
