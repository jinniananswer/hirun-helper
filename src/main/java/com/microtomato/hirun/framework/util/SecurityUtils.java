package com.microtomato.hirun.framework.util;

import com.microtomato.hirun.framework.security.UserContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 权限判断工具类
 *
 * @author Steven
 * @date 2019-11-13
 */
public final class SecurityUtils {
    private SecurityUtils() {
    }

    /**
     * 是否有某个权限
     *
     * @param funcCode 权限编码，对应表 sys_func.func_code
     * @return true: 有权限, false: 无权限
     */
    public static final boolean hasFuncId(String funcCode) {

        UserContext userContext = WebContextUtils.getUserContext();
        if (userContext.isAdmin()) {
            return true;
        }

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(funcCode);
        return userContext.getAuthorities().contains(simpleGrantedAuthority);
    }

    /**
     * 拥有任意一个权限，即返回 true
     *
     * @param funcCodes
     * @return
     */
    public static final boolean hasAnyFuncId(String... funcCodes) {

        UserContext userContext = WebContextUtils.getUserContext();
        if (userContext.isAdmin()) {
            return true;
        }

        Collection<GrantedAuthority> grantedAuthorities = userContext.getGrantedAuthorities();
        for (String funcCode : funcCodes) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(funcCode);
            if (grantedAuthorities.contains(simpleGrantedAuthority)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 拥有列出的所有权限，才返回 true
     *
     * @param funcCodes
     * @return
     */
    public static final boolean hasAllFuncId(String... funcCodes) {

        UserContext userContext = WebContextUtils.getUserContext();
        if (userContext.isAdmin()) {
            return true;
        }

        Collection<GrantedAuthority> grantedAuthorities = userContext.getGrantedAuthorities();
        for (String funcCode : funcCodes) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(funcCode);
            if (!grantedAuthorities.contains(simpleGrantedAuthority)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 批量判断权限
     *
     * @param funcCodes 权限编码集合，对应表 sys_func.func_code
     * @return
     */
    public static final Set<String> filter(Collection<String> funcCodes) {

        String[] funcCodeArray = funcCodes.toArray(new String[funcCodes.size()]);
        return filter(funcCodeArray);
    }

    /**
     * 批量判断权限
     */
    public static final Set<String> filter(String... funcCodes) {

        Set<String> rtn = new HashSet<>();

        UserContext userContext = WebContextUtils.getUserContext();
        if (userContext.isAdmin()) {
            rtn.addAll(Arrays.asList(funcCodes));
            return rtn;
        }

        Collection<GrantedAuthority> grantedAuthorities = WebContextUtils.getUserContext().getGrantedAuthorities();

        for (String funcCode : funcCodes) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(funcCode);
            if (grantedAuthorities.contains(simpleGrantedAuthority)) {
                rtn.add(funcCode);
            }
        }

        return rtn;
    }

}
