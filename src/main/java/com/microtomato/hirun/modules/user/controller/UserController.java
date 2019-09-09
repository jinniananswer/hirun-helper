package com.microtomato.hirun.modules.user.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jinnian
 * @since 2019-09-05
 */
@RestController
@Slf4j
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    private IUserService userServiceImpl;

    @PostMapping("/login")
    @RestResult
    public User login(String username, String password) {
        User user = userServiceImpl.login(username, password);
        return user;
    }
}
