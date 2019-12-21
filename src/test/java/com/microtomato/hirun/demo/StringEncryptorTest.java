package com.microtomato.hirun.demo;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Steven
 * @date 2019-12-21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StringEncryptorTest {

    @Autowired
    private StringEncryptor encryptor;

    @Test
    public void encry() {

        for (int i = 0; i < 3; i++) {
            String rawString = "micro-2019";
            String encptString = encryptor.encrypt(rawString);
            System.out.printf("明文: %s, 密文: %s\n", rawString, encptString);
            System.out.println("解密: " + encryptor.decrypt(encptString));
        }

    }

}
