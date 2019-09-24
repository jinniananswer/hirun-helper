package com.microtomato.hirun.framework.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 常用加密算法工具类
 *
 * @author Steven
 * @date 2019-09-09
 */
public final class EncryptUtils {

    /**
     * 明文摘要
     *
     * @param text
     * @return
     */
    public static String passwordEncode(String text) {

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        digest.update(text.getBytes());
        return new BigInteger(1, digest.digest()).toString(32);

    }

    public static void main(String[] args) {
        System.out.println(passwordEncode("820311"));
    }
}
