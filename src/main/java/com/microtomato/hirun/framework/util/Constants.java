package com.microtomato.hirun.framework.util;

import java.time.format.DateTimeFormatter;

/**
 * 通用常量
 *
 * @author Steven
 * @date 2019-09-22
 */
public final class Constants {
	public static final String STATUS_OK = "0";
	public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

	/**
	 * 默认角色Id
	 */
	public static final Long DEFAULT_ROLE_ID = 2L;

	/**
	 * 默认用户Id，主要用于后台任务
	 */
	public static final Long DEFAULT_USER_ID = 1L;

	/**
	 * 超级管理员的角色ID
	 */
	public static final Long SUPER_ROLE_ID = 1L;

	public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
}
