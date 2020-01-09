package com.microtomato.hirun;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

/**
 * 注解 ServletComponentScan 用于扫描过滤器 Filter 并自动装配，Filter 类需标注 @WebFilter 注解。
 * Servlet、Filter、Listener 可以直接通过 @WebServlet、@WebFilter、@WebListener 注解自动注册，无需其他代码。
 *
 * @author Steven
 */
@ServletComponentScan(basePackages = "com.microtomato.hirun.framework.web")
@SpringBootApplication(exclude = {MybatisPlusAutoConfiguration.class})
public class HirunHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(HirunHelperApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }
}