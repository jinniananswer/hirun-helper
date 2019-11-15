package com.microtomato.hirun.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;

/**
 * 后台任务 和 前端应用 应分开部署:<p>
 * 前端应用不启用 @EnableScheduling
 * 后台任务需启用 @EnableScheduling
 * <p>
 * 启用 @EnableScheduling 的方法两种任选其一：
 * 1. 配置文件里添加 scheduling.enabled: true
 * 2. 启动命令行添加 --scheduling.enabled=true
 *
 * @author Steven
 * @date 2019-11-15
 */
@Slf4j
@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = "scheduling", name = "enabled", havingValue = "true")
public class SchedulingConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
    }

    /**
     * 实现接口 SchedulingConfigurer，是为了引入线程池，提高延时任务的并发处理能力
     *
     * @return
     */
    @Bean
    public Executor taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(20);
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        taskScheduler.setThreadNamePrefix("Task-scheduling-pool:");
        taskScheduler.setRemoveOnCancelPolicy(true);
        taskScheduler.setErrorHandler(t -> log.error("Error occurs", t));
        return taskScheduler;
    }

}