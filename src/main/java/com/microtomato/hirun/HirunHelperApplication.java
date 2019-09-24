package com.microtomato.hirun;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Steven
 */
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
public class HirunHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(HirunHelperApplication.class, args);
    }

}
