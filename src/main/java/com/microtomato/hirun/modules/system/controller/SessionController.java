package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.framework.data.Result;
import com.microtomato.hirun.framework.listener.SessionContext;
import com.microtomato.hirun.framework.util.Constants;
import com.microtomato.hirun.framework.util.ResultUtils;
import com.microtomato.hirun.modules.user.entity.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 认证会话 ID 的有效性
 *
 * {"code":400000,"message":"认证无效！"}
 * {"code":0,"rows":{"username":"13723885094"}}
 *
 * @author Steven
 * @date 2019-12-01
 */
@RestController
@Slf4j
@RequestMapping("api/system/session/")
public class SessionController {

    @GetMapping("/authentication/{hirunSid}")
    public Result authentication(@PathVariable String hirunSid) {

        HttpSession session = SessionContext.getSession(hirunSid);
        if (null == session) {
            return ResultUtils.failure(hirunSid + " 认证无效!");
        }

        SecurityContextImpl context = (SecurityContextImpl) session.getAttribute(Constants.SPRING_SECURITY_CONTEXT);
        UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
        log.info("{} 认证有效！登录帐号：{}", hirunSid, userDetails.getUsername());
        return ResultUtils.success(User.builder().username(userDetails.getUsername()).build());
    }

}
