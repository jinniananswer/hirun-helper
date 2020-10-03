package com.microtomato.hirun.modules.system.service;


import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.modules.system.entity.po.Func;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.entity.po.UserRole;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.InitializingBean;

import javax.crypto.SecretKey;
import java.util.List;

/**
 * @author Steven
 * @date 2020-03-02
 */
public interface IAuthService extends InitializingBean {

    /**
     * 由字符串生产加密 Key
     *
     * @return
     */
    SecretKey generateKey();

    /**
     * 签发 jwt
     *
     * @return
     */
    String createJwt(UserContext userContext);

    /**
     * 签发 jwt
     *
     * @param issuer   JWT 签发放
     * @param audience JWT 接收方
     * @return
     */
    String createJwt(String issuer, String audience, UserContext userContext);

    /**
     * 解签 jwt
     *
     * @param jsonWebToken
     * @return
     */
    Claims parseJwt(String jsonWebToken);

    /**
     * 刷新令牌
     *
     * @param claims
     * @return
     */
    String refreshJwt(Claims claims);

    /**
     * 根据 Jwt 设置用户上下文
     *
     * @param jsonWebToken
     */
    void setUserContext(String jsonWebToken);

    List<Func> queryFuncSet(Long userId, List<UserRole> userRoles);

    List<UserRole> queryUserRoles(UserContext userContext, User user);

    User checkPassword(String username, String password);
}
