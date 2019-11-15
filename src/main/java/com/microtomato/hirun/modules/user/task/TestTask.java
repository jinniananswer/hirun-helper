package com.microtomato.hirun.modules.user.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Steven
 * @date 2019-11-15
 */
@Slf4j
@Component
public class TestTask {

    /**
     * 每天凌晨 5:10 开始执行。
     * 删除过期时间小于当前时间的，以及过期时间大于当前时间 30 天的异常数据。
     */
    @Scheduled(initialDelay = 1000 * 10, fixedRate = 5000)
    public void scheduled() {
        log.info("任务调度！调度任务！");
    }
}
