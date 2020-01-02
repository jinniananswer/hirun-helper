package com.microtomato.hirun.util;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 运行时在命令行里填充
 *
 * @author Steven
 * @date 2019-12-21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StringEncryptorUtils {

    @Autowired
    private StringEncryptor encryptor;

    @Test
    public void encryptor() {

        String[] raws = {"莫咯", "我们哈是撮汤锅子滴！"};

        for (String raw : raws) {
            String enc = encryptor.encrypt(raw);
            System.out.printf("明文: %s, 密文: ENC(%s)，解密后明文: %s\n", raw, enc, encryptor.decrypt(enc));
        }

    }
}
