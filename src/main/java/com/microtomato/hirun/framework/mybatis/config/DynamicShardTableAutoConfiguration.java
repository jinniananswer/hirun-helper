package com.microtomato.hirun.framework.mybatis.config;

import com.microtomato.hirun.framework.mybatis.aop.DynamicShardTableAnnotationAdvisor;
import com.microtomato.hirun.framework.mybatis.aop.DynamicShardTableSwitchInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author Steven
 * @date 2020-03-20
 */
@Slf4j
@Configuration
public class DynamicShardTableAutoConfiguration {

    /**
     * 业务侧自动化配置给出 dynamicDataSourcePolicy 策略链，这里根据做具体的切面增强。
     *
     * @param
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DynamicShardTableAnnotationAdvisor dynamicShardTableAnnotationAdvisor() {
        DynamicShardTableSwitchInterceptor interceptor = new DynamicShardTableSwitchInterceptor();
        DynamicShardTableAnnotationAdvisor advisor = new DynamicShardTableAnnotationAdvisor(interceptor);
        advisor.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return advisor;
    }

}
