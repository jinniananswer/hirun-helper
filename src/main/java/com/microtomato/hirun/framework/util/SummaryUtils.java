package com.microtomato.hirun.framework.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 常用摘要 工具类
 *
 * @author Steven
 * @date 2019-09-09
 */
public final class SummaryUtils {

    /**
     * 生成 MD5 摘要
     *
     * @param text
     * @return
     */
    public static final String md5sum(String text) {
        return DigestUtils.md5Hex(text);
    }

    /**
     * 生成 MD5 摘要
     *
     * @param bytes
     * @return
     */
    public static final String md5sum(byte[] bytes) {
        return DigestUtils.md5Hex(bytes);
    }

    /**
     * 生成 SHA1摘要
     *
     * @param text
     * @return
     */
    public static String sha1sum(String text) {
        return DigestUtils.sha1Hex(text);
    }

    /**
     * 生成 SHA1 摘要
     *
     * @param bytes
     * @return
     */
    public static String sha1sum(byte[] bytes) {
        return DigestUtils.sha1Hex(bytes);
    }

    public static void main(String[] args) {
        System.out.println(SummaryUtils.md5sum("123456"));
        System.out.println(SummaryUtils.sha1sum("123456"));
    }
}
