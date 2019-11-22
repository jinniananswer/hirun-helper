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
     * @param funcId 权限编码，对应表 ins_func_role.func_id
     * @return true: 有权限, false: 无权限
     */
    public static final boolean hasFuncId(String funcId) {

        UserContext userContext = WebContextUtils.getUserContext();
        if (userContext.isAdmin()) {
            return true;
        }

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(funcId);
        return userContext.getAuthorities().contains(simpleGrantedAuthority);
    }

    /**
     * 拥有任意一个权限，即返回 true
     *
     * @param funcIds
     * @return
     */
    public static final boolean hasAnyFuncId(String... funcIds) {

        UserContext userContext = WebContextUtils.getUserContext();
        if (userContext.isAdmin()) {
            return true;
        }

        Collection<GrantedAuthority> grantedAuthorities = userContext.getGrantedAuthorities();
        for (String funcId : funcIds) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(funcId);
            if (grantedAuthorities.contains(simpleGrantedAuthority)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 拥有列出的所有权限，才返回 true
     *
     * @param funcIds
     * @return
     */
    public static final boolean hasAllFuncId(String... funcIds) {

        UserContext userContext = WebContextUtils.getUserContext();
        if (userContext.isAdmin()) {
            return true;
        }

        Collection<GrantedAuthority> grantedAuthorities = userContext.getGrantedAuthorities();
        for (String funcId : funcIds) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(funcId);
            if (!grantedAuthorities.contains(simpleGrantedAuthority)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 批量判断权限
     *
     * @param funcIds 权限编码集合，对应表 ins_func_role.func_id
     * @return
     */
    public static final Set<String> filter(Collection<String> funcIds) {

        String[] funcIdArray = funcIds.toArray(new String[funcIds.size()]);
        return filter(funcIdArray);
    }

    /**
     * 批量判断权限
     */
    public static final Set<String> filter(String... funcIds) {

        Set<String> rtn = new HashSet<>();

        UserContext userContext = WebContextUtils.getUserContext();
        if (userContext.isAdmin()) {
            rtn.addAll(Arrays.asList(funcIds));
            return rtn;
        }

        Collection<GrantedAuthority> grantedAuthorities = WebContextUtils.getUserContext().getGrantedAuthorities();

        for (String funcId : funcIds) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(funcId);
            if (grantedAuthorities.contains(simpleGrantedAuthority)) {
                rtn.add(funcId);
            }
        }

        return rtn;
    }

}
