package com.microtomato.hirun.framework.security;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.framework.util.Constants;
import com.microtomato.hirun.modules.system.entity.po.Func;
import com.microtomato.hirun.modules.system.service.IFuncService;
import com.microtomato.hirun.modules.user.entity.dto.UserDTO;
import com.microtomato.hirun.modules.user.entity.po.FuncRole;
import com.microtomato.hirun.modules.user.entity.po.FuncTemp;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.entity.po.UserRole;
import com.microtomato.hirun.modules.user.service.IFuncRoleService;
import com.microtomato.hirun.modules.user.service.IFuncTempService;
import com.microtomato.hirun.modules.user.service.IUserRoleService;
import com.microtomato.hirun.modules.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 自定义用户加载服务
 *
 * @author Steven
 * @date 2019-09-09
 */
@Slf4j
@Component
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserService userServiceImpl;

    @Autowired
    private IFuncService funcServiceImpl;

    @Autowired
    private IFuncTempService funcTempServiceImpl;

    @Autowired
    private IUserRoleService userRoleServiceImpl;

    @Autowired
    private IFuncRoleService funcRoleServiceImpl;

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
        User user = userServiceImpl.queryUser(username);
        if (null == user) {
            // 这里找不到必须抛异常
            throw new UsernameNotFoundException("用户名不存在：" + username);
        }

        // 查用户角色
        List<UserRole> userRoles = userRoleServiceImpl.queryUserRole(user);

        // 根据角色查操作权限
        List<Func> funcList = queryFuncSet(user.getUserId(), userRoles);

        // 加载操作权限
        Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();
        funcList.forEach(func -> grantedAuthorities.add(new SimpleGrantedAuthority(func.getFuncCode())));

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
        userContext.setName(userDTO.getName());
        userContext.setRoles(roleList);
        userContext.setGrantedAuthorities(grantedAuthorities);
        BeanUtils.copyProperties(user, userContext);

        return userContext;
    }

    /**
     * 查询操作权限
     *
     * @param userId
     * @param userRoles
     * @return
     */
    private List<Func> queryFuncSet(Long userId, List<UserRole> userRoles) {

        Set<Long> funcIdSet = new HashSet<>();

        // 查用户临时操作权限
        List<FuncTemp> funcTempList = funcTempServiceImpl.queryFuncTemp(userId);
        funcTempList.forEach(funcTemp -> funcIdSet.add(funcTemp.getFuncId()));

        // 查用户角色对应的操作权限
        List<Long> roleIdList = new ArrayList<>();
        roleIdList.add(Constants.DEFAULT_ROLE_ID);
        userRoles.forEach(userRole -> roleIdList.add(userRole.getRoleId()));
        List<FuncRole> funcRoleList = funcRoleServiceImpl.list(
            Wrappers.<FuncRole>lambdaQuery()
                .select(FuncRole::getFuncId)
                .in(FuncRole::getRoleId, roleIdList)
        );
        funcRoleList.forEach(funcRole -> funcIdSet.add(funcRole.getFuncId()));

        if (funcIdSet.size() > 0) {
            List<Func> funcList = funcServiceImpl.list(
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

}