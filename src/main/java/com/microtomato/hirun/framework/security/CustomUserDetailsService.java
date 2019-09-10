package com.microtomato.hirun.framework.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.entity.po.UserFunc;
import com.microtomato.hirun.modules.user.service.impl.UserFuncServiceImpl;
import com.microtomato.hirun.modules.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * 自定义用户加载服务
 *
 * @author Steven
 * @date 2019-09-09
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	private UserFuncServiceImpl userFuncServiceImpl;

	/**
	 * 在 Security 中，角色和权限共用 GrantedAuthority 接口，唯一的不同角色就是多了个前缀 "ROLE_"
	 *
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// 1. 查询用户
		User userFromDatabase = userServiceImpl.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));
		if (null == userFromDatabase) {
			throw new UsernameNotFoundException("User " + username + " was not found in database!");
			//这里找不到必须抛异常
		}

		// 加载户权限
		List<UserFunc> userFuncList = userFuncServiceImpl.list(new QueryWrapper<UserFunc>().lambda().eq(UserFunc::getUserId, userFromDatabase.getUserId()));

		Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for (UserFunc userFunc : userFuncList) {
			grantedAuthorities.add(new SimpleGrantedAuthority(userFunc.getFuncId().toString()));
		}

		// TODO: 查用户角色
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		return new org.springframework.security.core.userdetails.User(username,
			userFromDatabase.getPassword(),
			true,
			true,
			true,
			true,
			grantedAuthorities);
	}
}