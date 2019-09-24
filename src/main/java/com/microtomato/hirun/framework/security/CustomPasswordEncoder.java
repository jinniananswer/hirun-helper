package com.microtomato.hirun.framework.security;

import com.microtomato.hirun.framework.util.EncryptUtils;
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
		return EncryptUtils.passwordEncode(password);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String password = EncryptUtils.passwordEncode(String.valueOf(rawPassword));
		if (password.equals(encodedPassword)) {
			return true;
		} else {
			return false;
		}
	}

}
