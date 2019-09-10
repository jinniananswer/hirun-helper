package com.microtomato.hirun.framework.security;

import com.microtomato.hirun.framework.utils.MD5Util;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 自定义密码加密实现
 *
 * @author Steven
 * @date 2019-09-09
 */
@Component
public class CustomPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		String password = String.valueOf(rawPassword);
		return MD5Util.hexdigest(password);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String password = MD5Util.hexdigest(String.valueOf(rawPassword));
		if (password.equals(encodedPassword)) {
			return true;
		} else {
			return false;
		}
	}

}
