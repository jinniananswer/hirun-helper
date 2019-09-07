package com.microtomato.hirun.framework.utils;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptorUtil {

    public static String encryptMd5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            return new BigInteger(1, md.digest()).toString(32);
        } catch(NoSuchAlgorithmException e) {

        }
        return null;
    }

    public static void main(String[] args) throws Exception{
        System.out.println(encryptMd5("123456"));
    }
}
