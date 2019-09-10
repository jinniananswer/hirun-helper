package com.microtomato.hirun.framework.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 工具类
 *
 * @author Steven
 * @date 2019-09-09
 */
public final class MD5Util {

	/**
	 * 生成 MD5 摘要
	 *
	 * @param text
	 * @return
	 */
	public static String hexdigest(String text) {
		return hexdigest(text.getBytes());
	}

	/**
	 * 生成 MD5 摘要
	 *
	 * @param bytes
	 * @return
	 */
	public static String hexdigest(byte[] bytes) {
		MessageDigest alg = null;
		try {
			alg = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		alg.update(bytes);
		return new BigInteger(1, alg.digest()).toString(32);
	}

	public static void main(String[] args) throws Exception {
		System.out.println(hexdigest("123456"));
	}
}
