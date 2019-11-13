package com.microtomato.hirun.framework.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author Steven
 * @date 2019-11-13
 */
public final class SecurityUtils {
    private SecurityUtils() {}

    public boolean hasAuth(String authCode) {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authCode);
        return WebContextUtils.getUserContext().getAuthorities().contains(simpleGrantedAuthority);
    }

}
