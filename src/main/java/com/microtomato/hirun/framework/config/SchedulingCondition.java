package com.microtomato.hirun.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author Steven
 * @date 2020-01-09
 */
@Slf4j
public class SchedulingCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String enabled = context.getEnvironment().getProperty("scheduling.enabled");
        log.info("是否开启任务调度: {}", enabled);
        return Boolean.valueOf(enabled);
    }

}
