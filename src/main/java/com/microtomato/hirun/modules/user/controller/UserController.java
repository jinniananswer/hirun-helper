package com.microtomato.hirun.modules.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.exception.PasswordException;
import com.microtomato.hirun.modules.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jinnian
 * @since 2019-09-05
 */
@RestController
@Slf4j
@RequestMapping("api/user/user")
public class UserController {

    @Autowired
    private IUserService userServiceImpl;

    @PostMapping("/login")
    @RestResult
    public User login(String username, String password) {
        User user = userServiceImpl.login(username, password);
        return user;
    }

    @GetMapping("/list")
    @RestResult
    public List<User> list() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("LIMIT 50");
        List<User> userList = userServiceImpl.list(queryWrapper);
        log.info(userList.toString());
        return userList;
    }

    @PostMapping("/changePassword")
    @RestResult
    public boolean changePassword(String oldPassword, String password, String repassword) {
        if (!StringUtils.equals(password, repassword)) {
            throw new PasswordException("新密码与确认输入不一致，请重新输入。");
        }
        UserContext userContext = WebContextUtils.getUserContext();
        Long userId = userContext.getUserId();

        boolean result = userServiceImpl.changePassword(userId, oldPassword, repassword);

        return result;
    }

    @PostMapping("/resetPassword")
    @RestResult
    public boolean resetPassword(Long employeeId) {
        return userServiceImpl.resetPassword(employeeId);
    }
}
