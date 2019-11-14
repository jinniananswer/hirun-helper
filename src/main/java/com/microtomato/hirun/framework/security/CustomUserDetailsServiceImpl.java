package com.microtomato.hirun.framework.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.modules.user.entity.po.FuncRole;
import com.microtomato.hirun.modules.user.entity.po.FuncTemp;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.entity.po.UserRole;
import com.microtomato.hirun.modules.user.entity.po.dto.UserDTO;
import com.microtomato.hirun.modules.user.service.IFuncTempService;
import com.microtomato.hirun.modules.user.service.impl.FuncRoleServiceImpl;
import com.microtomato.hirun.modules.user.service.impl.UserRoleServiceImpl;
import com.microtomato.hirun.modules.user.service.impl.UserServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 自定义用户加载服务
 *
 * @author Steven
 * @date 2019-09-09
 */
@Component
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private IFuncTempService funcTempServiceImpl;

    @Autowired
    private UserRoleServiceImpl userRoleServiceImpl;

    @Autowired
    private FuncRoleServiceImpl funcRoleServiceImpl;

    /**
     * 在 Security 中，角色和权限共用 GrantedAuthority 接口，唯一的不同角色就是多了个前缀 "ROLE_"
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserContext userContext = new UserContext();

        // 查询用户
        User user = userServiceImpl.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));
        if (null == user) {
            // 这里找不到必须抛异常
            throw new UsernameNotFoundException("User " + username + " was not found in database!");
        }

        Set<String> funcSet = new HashSet<>();

        // 查用户临时操作权限
        List<FuncTemp> funcTempList = funcTempServiceImpl.list(
            new QueryWrapper<FuncTemp>().lambda()
                .select(FuncTemp::getFuncId)
                .eq(FuncTemp::getUserId, user.getUserId())
                .lt(FuncTemp::getExpireDate, LocalDateTime.now())
        );
        funcTempList.forEach(funcTemp -> funcSet.add(funcTemp.getFuncId().toString()));

        // 查用户角色
        List<UserRole> userRoles = userRoleServiceImpl.list(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId, user.getUserId()));

        // 查用户角色对应的所有权限
        for (UserRole userRole : userRoles) {
            Long roleId = userRole.getRoleId();
            List<FuncRole> funcRoleList = funcRoleServiceImpl.list(new QueryWrapper<FuncRole>().lambda().eq(FuncRole::getRoleId, roleId));
            funcRoleList.forEach(funcRole -> funcSet.add(funcRole.getFuncId().toString()));
        }

        // 加载用户权限
        Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (String funcId : funcSet) {
            grantedAuthorities.add(new SimpleGrantedAuthority(funcId));
        }

        List<Role> roleList = new ArrayList<>();

        // 设置用户角色
        for (UserRole userRole : userRoles) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRoleId()));
            roleList.add(new Role(userRole.getRoleId(), ""));
            if (userRole.getRoleId() == 1) {
                // 超级管理员
                userContext.setAdmin(true);
            }
        }

        // 查登录用户的 orgId, employeeId
        UserDTO userDTO = userServiceImpl.queryRelatInfoByUserId(user.getUserId());

        userContext.setOrgId(userDTO.getOrgId());
        userContext.setEmployeeId(userDTO.getEmployeeId());
        userContext.setRoles(roleList);
        userContext.setGrantedAuthorities(grantedAuthorities);
        BeanUtils.copyProperties(user, userContext);

        return userContext;

    }


}