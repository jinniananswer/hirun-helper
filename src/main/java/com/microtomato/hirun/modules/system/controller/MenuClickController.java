package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.system.entity.po.Menu;
import com.microtomato.hirun.modules.system.service.IMenuClickService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 菜单点击数统计 前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-12-10
 */
@RestController
@Slf4j
@RequestMapping("/api/system/menu-click")
public class MenuClickController {

    @Autowired
    private IMenuClickService menuClickServiceImpl;


    @GetMapping("hostMenus")
    @RestResult
    public List<Menu> hostMenus() {
        Long userId = WebContextUtils.getUserContext().getUserId();
        return menuClickServiceImpl.hostMenus(userId);
    }

}
