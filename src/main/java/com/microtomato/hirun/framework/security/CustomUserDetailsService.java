package com.microtomato.hirun.framework.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 自定义用户加载服务
 *
 * @author Steven
 * @date 2019-09-09
 */
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// 1. 查询用户
		User userFromDatabase = userServiceImpl.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));
		if (null == userFromDatabase) {
			throw new UsernameNotFoundException("User " + username + " was not found in database!");
			//这里找不到必须抛异常
		}

		// 2. 设置角色
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
		grantedAuthorities.add(new SimpleGrantedAuthority("USER"));

		return new org.springframework.security.core.userdetails.User(username,
			userFromDatabase.getPassword(),
			true,
			true,
			true,
			true,
			grantedAuthorities);
	}
}