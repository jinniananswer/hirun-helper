package com.microtomato.hirun.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.TaskManagementConfigUtils;

/**
 * 后台任务 和 前端应用 应分开部署:<p>
 * 前端应用不启用 任务调度
 * 后台任务需启用 任务调度
 * <p>
 * 启用 任务调度 的方法两种任选其一：
 * 1. 配置文件里添加 scheduling.enabled: true
 * 2. 启动命令行添加 --scheduling.enabled=true
 *
 * @author Steven
 * @date 2020-01-09
 */
@Slf4j
@Configuration
public class SchedulerConfig {

    @Conditional(SchedulerCondition.class)
    @Bean(name = TaskManagementConfigUtils.SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ScheduledAnnotationBeanPostProcessor scheduledAnnotationProcessor() {
        return new ScheduledAnnotationBeanPostProcessor();
    }

    static class SchedulerCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            String enabled = context.getEnvironment().getProperty("scheduling.enabled");
            log.info("是否开启任务调度: {}", enabled);
            return Boolean.valueOf(enabled);
        }

    }

}
