package com.microtomato.hirun.framework.security;

import com.microtomato.hirun.framework.utils.EncryptorUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 自定义密码加密实现
 *
 * @author Steven
 * @date 2019-09-09
 */
@Component("customPasswordEncoder")
public class CustomPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return EncryptorUtil.encryptMd5(String.valueOf(rawPassword));
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String password = EncryptorUtil.encryptMd5(String.valueOf(rawPassword));
		if (password.equals(encodedPassword)) {
			return true;
		} else {
			return false;
		}
	}

}
