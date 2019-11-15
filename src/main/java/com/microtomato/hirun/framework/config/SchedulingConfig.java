package com.microtomato.hirun.framework.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 后台任务 和 前端应用 应分开部署:<p>
 * 前端应用不启用 @EnableScheduling
 * 后台任务需启用 @EnableScheduling
 * <p>
 * 启用 @EnableScheduling 的方法两种任选其一：
 * 1. application.yml 添加 scheduling.enabled: true
 * 2. 启动命令行添加 --scheduling.enabled=true
 *
 * @author Steven
 * @date 2019-11-15
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = "scheduling", name = "enabled", havingValue = "true")
public class SchedulingConfig {
}
