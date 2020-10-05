package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microtomato.hirun.framework.jwt.JwtConstants;
import com.microtomato.hirun.framework.security.CustomPasswordEncoder;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.threadlocal.UserContextHolder;
import com.microtomato.hirun.framework.util.Constants;
import com.microtomato.hirun.modules.system.entity.po.Func;
import com.microtomato.hirun.modules.system.service.IAuthService;
import com.microtomato.hirun.modules.system.service.IFuncService;
import com.microtomato.hirun.modules.user.entity.po.FuncRole;
import com.microtomato.hirun.modules.user.entity.po.FuncTemp;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.entity.po.UserRole;
import com.microtomato.hirun.modules.user.service.IFuncRoleService;
import com.microtomato.hirun.modules.user.service.IFuncTempService;
import com.microtomato.hirun.modules.user.service.IUserRoleService;
import com.microtomato.hirun.modules.user.service.IUserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.lang.Assert;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Jwt
 * <p>
 * Header: 是一个 JSON 对象，描述 JWT 的元数据。
 *
 * <p>
 * Payload: 是一个 JSON 对象，用来存放实际需要传递的数据。JWT 默认是不加密的，任何人都可以读到，所以不要把秘密信息放在这个部分。
 * <p>
 * JWT 规定了 7 个官方字段：
 * iss (issuer): 签发人
 * exp (expiration time): 过期时间
 * sub (subject): 主题
 * aud (audience): 受众
 * nbf (Not Before): 生效时间
 * iat (Issued At): 签发时间
 * jti (Jwt Id): 编号
 * <p>
 * 除了官方字段外，还可以定义私有字段！
 *
 * <p>
 * Signature: 是对 Header 和 Payload 两部分的签名，防止数据被篡改。签名时需要指定一个密钥（Secret），这个密钥只有服务器才知道，不能泄露给用户。
 * <p>
 * <p>
 * Base64URL: 注意 Header 和 Payload 部分的数据最终会采用 Base64URL（不同于 Base64） 算法转成字符串，因为 Base64 算法生成的字符串里可能
 * 包含 '/', '=', '+'，导致 token 数据不能以 URL 参数传递，而 Base64URL 算法正好解决了此问题。
 * <p>
 * Claims: 包含您想要签署的任何信息。
 *
 * @author Steven
 * @date 2020-02-24
 */
@Slf4j
@Service
public class AuthServiceImpl implements IAuthService {

    /**
     * 表示令牌的类型，JWT 令牌统一写成 "JWT"
     */
    public static final String KEY_TYPE = "typ";
    public static final String VAL_TYPE = "JWT";

    /**
     * 线程安全
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 密钥，默认值为开发使用。
     */
    @Setter
    @Value("${jwt.secret:hirun@2019}")
    public String jwtSecret;

    /**
     * 超时时间，默认 24 小时
     */
    @Setter
    @Value("${jwt.expiration:86400000}")
    public long jwtExpiration;

    @Autowired
    private IUserService userService;

    @Autowired
    private IFuncService funcService;

    @Autowired
    private IFuncTempService funcTempService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IFuncRoleService funcRoleService;

    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;

    /**
     * 由字符串生产加密 Key
     *
     * @return
     */
    @Override
    public SecretKey generateKey() {
        byte[] decode = Base64.getDecoder().decode(jwtSecret);
        SecretKeySpec key = new SecretKeySpec(decode, 0, decode.length, "AES");
        return key;
    }

    /**
     * 签发 jwt
     *
     * @return
     */
    @Override
    public String createJwt(UserContext userContext) {
        return createJwt("tomato", "tomato", userContext);
    }

    /**
     * 签发 jwt
     *
     * @param issuer   JWT 签发放
     * @param audience JWT 接收方
     * @return
     */
    @Override
    public String createJwt(String issuer, String audience, UserContext userContext) {

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiration = new Date(nowMillis + jwtExpiration);

        Map<String, Object> claims = objectMapper.convertValue(userContext, Map.class);
        LocalDateTime loginTime = userContext.getLoginTime();
        if (null != loginTime) {
            claims.remove("loginTime");
            claims.put("loginTime", loginTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        log.debug("claims: {}", claims);

        // 添加构成 JWT 的参数
        JwtBuilder builder = Jwts.builder()
            .setHeaderParam(KEY_TYPE, VAL_TYPE)
            .setClaims(claims)
            .setIssuer(issuer)
            .setIssuedAt(now)
            .setAudience(audience)
            .setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS256, jwtSecret);

        // 生成 JWT
        String token = builder.compact();
        return token;
    }

    /**
     * 解签 jwt
     *
     * @param jsonWebToken
     * @return
     */
    @Override
    public Claims parseJwt(String jsonWebToken) {

        Claims claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(jsonWebToken)
            .getBody();

        log.debug("parseJwt claims: {}", claims);
        return claims;

    }

    /**
     * 刷新令牌
     *
     * @param claims
     * @return
     */
    @Override
    public String refreshJwt(Claims claims) {

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiration = new Date(nowMillis + jwtExpiration);

        // 添加构成 JWT 的参数
        JwtBuilder builder = Jwts.builder()
            .setHeaderParam(KEY_TYPE, VAL_TYPE)
            .setIssuedAt(now)
            .setExpiration(expiration)
            .setIssuer((String) claims.get(Claims.ISSUER))
            .setAudience((String) claims.get(Claims.AUDIENCE))
            .signWith(SignatureAlgorithm.HS256, jwtSecret);

        // 生成 JWT
        String token = builder.compact();
        log.debug("refreshJwt claims: {}", claims);
        return token;
    }


    @Override
    public void afterPropertiesSet() {
        long start = System.currentTimeMillis();
        createJwt("tomato", "tomato", UserContext.builder().build());
        log.info("预热 JwtBuilder, cost: {} ms", (System.currentTimeMillis() - start));
    }

    /**
     * 根据 Jwt 设置用户上下文
     *
     * @param jsonWebToken
     */
    @Override
    public void setUserContext(String jsonWebToken) {

        if (StringUtils.isBlank(jsonWebToken)) {
            return;
        }

        jsonWebToken = StringUtils.removeStart(jsonWebToken, JwtConstants.HEAD_AUTHORIZATION_BEARER);

        try {
            Claims claims = this.parseJwt(jsonWebToken);
            Long userId = claims.get("userId", Long.class);
            String username = claims.get("username", String.class);
            List roles = claims.get("roles", List.class);
            Boolean admin = claims.get("admin", Boolean.class);
            String mobileNo = claims.get("mobileNo", String.class);
            String status = claims.get("status", String.class);
            Long orgId = claims.get("orgId", Long.class);
            Long employeeId = claims.get("employeeId", Long.class);
            String name = claims.get("name", String.class);
            Long mainRoleId = claims.get("mainRoleId", Long.class);
            Long loginTime = claims.get("loginTime", Long.class);

            UserContext userContext = UserContext.builder()
                .userId(userId)
                .username(username)
                .roles(roles)
                .admin(admin)
                .mobileNo(mobileNo)
                .status(status)
                .orgId(orgId)
                .employeeId(employeeId)
                .name(name)
                .mainRoleId(mainRoleId)
                .loginTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(loginTime), ZoneId.systemDefault()))
                .build();

            UserContextHolder.setUserContext(userContext);

        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            log.error("Token已过期: {} ", claims);
        } catch (UnsupportedJwtException e) {
            log.error("Token格式错误: {} ", e);
        } catch (MalformedJwtException e) {
            log.error("Token没有被正确构造: {} ", e);
        } catch (SignatureException e) {
            log.error("签名失败: {} ", e);
        } catch (IllegalArgumentException e) {
            log.error("非法参数异常: {} ", e);
        }
    }

    /**
     * 查询操作权限
     *
     * @param userId
     * @param userRoles
     * @return
     */
    @Override
    public List<Func> queryFuncSet(Long userId, List<UserRole> userRoles) {

        Set<Long> funcIdSet = new HashSet<>();

        // 判断是否有超级权限
        boolean isAdmin = false;
        for (UserRole userRole : userRoles) {
            if (userRole.getRoleId().equals(Constants.SUPER_ROLE_ID)) {
                isAdmin = true;
            }
        }

        if (isAdmin) {
            // 超级工号，有所有的权限
            List<Func> list = funcService.list(
                Wrappers.<Func>lambdaQuery()
                    .select(Func::getFuncId)
                    .eq(Func::getType, 1)
                    .eq(Func::getStatus, 0)
            );
            list.forEach(func -> funcIdSet.add(func.getFuncId()));
        } else {
            // 普通用户
            // 查用户临时操作权限
            List<FuncTemp> funcTempList = funcTempService.queryFuncTemp(userId);
            funcTempList.forEach(funcTemp -> funcIdSet.add(funcTemp.getFuncId()));

            // 查用户角色对应的操作权限
            List<Long> roleIdList = new ArrayList<>();
            roleIdList.add(Constants.DEFAULT_ROLE_ID);
            userRoles.forEach(userRole -> roleIdList.add(userRole.getRoleId()));
            List<FuncRole> funcRoleList = funcRoleService.list(
                Wrappers.<FuncRole>lambdaQuery()
                    .select(FuncRole::getFuncId)
                    .in(FuncRole::getRoleId, roleIdList)
            );
            funcRoleList.forEach(funcRole -> funcIdSet.add(funcRole.getFuncId()));
        }

        // 根据 func_id 找对应的 func_code
        if (funcIdSet.size() > 0) {
            List<Func> funcList = funcService.list(
                Wrappers.<Func>lambdaQuery()
                    .select(Func::getFuncCode)
                    .eq(Func::getType, "1")
                    .eq(Func::getStatus, "0")
                    .in(Func::getFuncId, funcIdSet)
            );
            return funcList;
        }

        return new ArrayList<Func>();

    }

    /**
     * 查询用户角色
     *
     * @param userContext
     * @param user
     * @return
     */
    @Override
    public List<UserRole> queryUserRoles(UserContext userContext, User user) {
        List<UserRole> userRoles = userRoleService.queryUserRole(user);
        userRoles.add(UserRole.builder().roleId(Constants.DEFAULT_ROLE_ID).isMainRole(Boolean.FALSE).build());
        setMainRoleId(userContext, userRoles);
        return userRoles;
    }

    @Override
    public User checkPassword(String username, String password) {
        User user = userService.queryUser(username);
        Assert.notNull(user, "用户名不存在: " + username);
        Assert.isTrue(customPasswordEncoder.matches(password, user.getPassword()), "密码验证失败!");
        return user;
    }

    /**
     * 设置主角色
     *
     * @param userContext
     * @param userRoles
     */
    public void setMainRoleId(UserContext userContext, List<UserRole> userRoles) {
        for (UserRole userRole : userRoles) {
            if (userRole.getIsMainRole()) {
                userContext.setMainRoleId(userRole.getRoleId());
                return;
            }
        }
    }
}
