package com.microtomato.hirun.framework.util;

import com.microtomato.hirun.framework.security.Role;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: hirun-helper
 * @description:
 * @author: jinnian
 * @create: 2020-10-02 22:58
 **/
public class UserContextUtils {

    public static UserContext getUserContext() {
        UserContext userContext = new UserContext();
        userContext.setEmployeeId(1L);
        userContext.setOrgId(5L);
        userContext.setMainRoleId(1L);
        List<Role> roles = new ArrayList<Role>();
        Role role = new Role(1L, "超级管理员");
        roles.add(role);
        userContext.setRoles(roles);
        userContext.setAdmin(true);
        userContext.setName("金念");
        userContext.setUserId(1L);
        RequestTimeHolder.addRequestTime(LocalDateTime.now());
        return userContext;
    }
}
