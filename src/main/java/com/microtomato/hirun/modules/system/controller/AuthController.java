package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.jwt.JwtConstants;
import com.microtomato.hirun.framework.security.Role;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.modules.system.entity.dto.AuthResultDTO;
import com.microtomato.hirun.modules.system.entity.po.Func;
import com.microtomato.hirun.modules.system.service.IAuthService;
import com.microtomato.hirun.modules.user.entity.dto.UserDTO;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.entity.po.UserRole;
import com.microtomato.hirun.modules.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steven
 * @date 2020-09-26
 */
@RestController
@Slf4j
@RequestMapping("/api/system/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IAuthService authService;

    /**
     * curl -v -d 'username=13787235663' -d 'password=243118' -XPOST http://127.0.0.1:8080/hirun/api/system/auth/login
     *
     * @param username 13723885094
     * @param password 820311
     */
    @RestResult
    @PostMapping("login")
    public AuthResultDTO login(@RequestParam("username") String username,
                               @RequestParam("password") String password) {

        log.debug("登录认证，username: {}", username);

        // 1: 密码校验
        User user = authService.checkPassword(username, password);

        // 2: 查角色，设置 主角色Id 到 token 中
        UserContext userContext = new UserContext();
        List<UserRole> userRoles = authService.queryUserRoles(userContext, user);

        // 3: 查角色操作权限
        List<Func> funcList = authService.queryFuncSet(user.getUserId(), userRoles);

        List<Role> roleList = new ArrayList<>();

        // 4: 设置用户角色
        for (UserRole userRole : userRoles) {
            roleList.add(new Role(userRole.getRoleId(), null));
            if (userRole.getRoleId() == 1) {
                // 超级管理员
                userContext.setAdmin(true);
            }
        }

        // 5: 查登录用户的 orgId, employeeId
        UserDTO userDTO = userService.queryRelatInfoByUserId(user.getUserId());

        userContext.setOrgId(userDTO.getOrgId());
        userContext.setEmployeeId(userDTO.getEmployeeId());
        userContext.setName(userDTO.getName());
        userContext.setRoles(roleList);
        BeanUtils.copyProperties(user, userContext);
        userContext.setPassword("");

        String token = authService.createJwt(userContext);

        log.info("token: {}", token);

        List<String> funcCodes = new ArrayList();
        funcList.forEach(func -> funcCodes.add(func.getFuncCode()));

        AuthResultDTO authResultDTO = new AuthResultDTO();
        authResultDTO.setJwt(JwtConstants.HEAD_AUTHORIZATION_BEARER + token);
        authResultDTO.setFuncCodes(funcCodes);
        return authResultDTO;
    }


}
