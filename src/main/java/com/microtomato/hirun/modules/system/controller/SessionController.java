package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.framework.security.TokenContext;
import com.microtomato.hirun.framework.data.Result;
import com.microtomato.hirun.framework.util.ResultUtils;
import com.microtomato.hirun.modules.user.entity.po.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证会话 ID 的有效性
 * <p>
 * {"code":-1,"message":"认证无效！"}
 * {"code":0,"rows":{"username":"13723885094"}}
 *
 * @author Steven
 * @date 2019-12-01
 */
@RestController
@Slf4j
@RequestMapping("api/system/session/")
public class SessionController {

    private static final int IS_NOT_AUTHENTICATED_LEN = 1;
    private static final int IS_AUTHENTICATED_LEN = 2;

    @GetMapping("/authentication/{hirunToken}")
    public Result authentication(@PathVariable String hirunToken) {

        String username = TokenContext.authentication(hirunToken);
        if (null == username) {
            String msg = hirunToken + " 认证无效!";
            log.info(msg);
            return ResultUtils.failure(msg);
        }

        String[] split = StringUtils.split(username, TokenContext.TOKEN_SPLIT_CHAR);

        if (IS_NOT_AUTHENTICATED_LEN == split.length) {
            log.info("{} 认证有效！登录帐号：{}", hirunToken, username);
            return ResultUtils.success(User.builder().username(username).build());
        } else if (IS_AUTHENTICATED_LEN == split.length) {
            String msg = hirunToken + " 已失效,上次认证时间: " + split[1];
            log.info(msg);
            return ResultUtils.failure(msg);
        } else {
            return ResultUtils.failure("认证无效!");
        }

    }

}
