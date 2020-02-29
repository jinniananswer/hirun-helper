package com.microtomato.hirun.framework.mybatis.config;

import com.microtomato.hirun.framework.mybatis.aop.DynamicDataSourceAnnotationAdvisor;
import com.microtomato.hirun.framework.mybatis.aop.DynamicDataSourceSwitchInterceptor;
import com.microtomato.hirun.framework.mybatis.processor.DataSourceProcessor;
import com.microtomato.hirun.framework.mybatis.processor.impl.DataSourceHeaderProcessor;
import com.microtomato.hirun.framework.mybatis.processor.impl.DataSourceSessionProcessor;
import com.microtomato.hirun.framework.mybatis.processor.impl.DataSourceSpelProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author Steven
 * @date 2020-02-29
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "spring.datasource.dynamic", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DataSourceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DataSourceProcessor dataSourceProcessor() {
        DataSourceHeaderProcessor dataSourceHeaderProcessor = new DataSourceHeaderProcessor();
        DataSourceSessionProcessor dataSourceSessionProcessor = new DataSourceSessionProcessor();
        DataSourceSpelProcessor spelExpressionProcessor = new DataSourceSpelProcessor();

        dataSourceHeaderProcessor.setNextProcessor(dataSourceSessionProcessor);
        dataSourceSessionProcessor.setNextProcessor(spelExpressionProcessor);
        return dataSourceHeaderProcessor;
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceAnnotationAdvisor dynamicDatasourceAnnotationAdvisor(DataSourceProcessor dataSourceProcessor) {
        DynamicDataSourceSwitchInterceptor interceptor = new DynamicDataSourceSwitchInterceptor();
        interceptor.setDataSourceProcessor(dataSourceProcessor);
        DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(interceptor);
        advisor.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return advisor;
    }

}
