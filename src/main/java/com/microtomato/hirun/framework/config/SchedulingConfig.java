package com.microtomato.hirun.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;

/**
 * 后台任务配置文件
 *
 * @author Steven
 * @date 2019-11-15
 */
@Slf4j
@Configuration
@EnableScheduling
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