package com.microtomato.hirun.framework.config;

import com.microtomato.hirun.framework.threadlocal.ContextCopyingDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Steven
 * @date 2020-05-15
 */
@Configuration
@EnableAsync
public class AsyncThreadPoolConfig {

    private static final int cpu = Runtime.getRuntime().availableProcessors();
    private static final int corePoolSize = cpu;
    private static final int maxPoolSize = cpu * 2;
    private static final int keepAliveTime = 60;
    private static final int queueCapacity = 200;
    private static final String threadNamePrefix = "taskExecutor-";

    /**
     * bean 的名称，默认为首字母小写的方法名
     *
     * @return
     */
    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setTaskDecorator(new ContextCopyingDecorator());
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setThreadNamePrefix(threadNamePrefix);

        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 初始化
        executor.initialize();
        return executor;
    }

}
