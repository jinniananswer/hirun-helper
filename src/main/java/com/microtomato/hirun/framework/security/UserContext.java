package com.microtomato.hirun.framework.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 登录后的用户上下文
 *
 * @author Steven
 * @date 2019-09-10
 */
@Data
public class UserContext implements UserDetails {

	/**
	 * 用户 Id
	 */
	private Integer userId;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 拥有的角色集
	 */
	private List<Role> roles;

	/**
	 * 用户密码
	 */
	private String password;

	/**
	 * 手机号码
	 */
	private String mobileNo;

	/**
	 * 用户状态
	 */
	private String status;

	/**
	 * 权限，角色
	 */
	private Collection<GrantedAuthority> grantedAuthorities;

	/**
	 * 拥有的菜单 url
	 */
	private Set<String> menuUrls;

	/**
	 * 账户是否没过期
	 *
	 * @return
	 */
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 账户是否没被锁定
	 *
	 * @return
	 */
	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 账户密码是否没过期
	 *
	 * @return
	 */
	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 账户是否可用
	 *
	 * @return
	 */
	@Override
	public boolean isEnabled() {
		return "0".equals(status);
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

}
