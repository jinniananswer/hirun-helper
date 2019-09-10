package com.microtomato.hirun.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Web上下文工具类
 *
 * @author Steven
 * @date 2019-04-29
 */
@Slf4j
public class WebContextUtil {

	/**
	 * 获取当前登录用户信息
	 *
	 * @return
	 */
	private static final UserDetails getUserDetails() {

		UserDetails userDetails = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userDetails = (UserDetails) principal;
		} else {
			log.error("principal is not UserDetails, " + principal);
		}

		return userDetails;
	}

}
