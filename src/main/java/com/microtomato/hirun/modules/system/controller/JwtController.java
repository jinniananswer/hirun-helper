package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.jwt.JwtConstants;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.modules.system.service.IJwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Steven
 * @date 2020-09-26
 */
@RestController
@Slf4j
@RequestMapping("/api/system/jwt")
public class JwtController {

    @Autowired
    private IJwtService jwtService;

    /**
     * curl -v -d 'username=13787235663' -d 'password=1q1w1e1r' -XPOST http://127.0.0.1:8080/hirun/api/system/jwt/login
     *
     * @param response
     * @param username
     * @param password
     */
    @RestResult
    @PostMapping("login")
    public void login(
        HttpServletResponse response,
        @RequestParam("username") String username,
        @RequestParam("password") String password) {

        log.info("=================================================");
        log.info("username: {}", username);
        log.info("password: {}", password);
        log.info("=================================================");

        // TODO: 密码认证
        // TODO: 查角色，设置 主角色Id 到 token 中
        // TODO: 查角色操作权限
        // TODO: 查登录用户的 orgId, employeeId

        // TODO: 过滤器，可以认证 Session 亦可认证 token

        UserContext userContext = UserContext.builder().userId(1L).mobileNo("13787235663").build();
        String token = jwtService.createJwt(userContext);

        log.info("token: {}", token);

        response.addHeader(JwtConstants.KeyAuthorization, JwtConstants.HEAD_AUTHORIZATION_BEARER + token);
    }

}
