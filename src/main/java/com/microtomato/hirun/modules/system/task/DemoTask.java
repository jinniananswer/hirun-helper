package com.microtomato.hirun.modules.system.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Steven
 * @date 2020-01-09
 */
@Slf4j
@Component
public class DemoTask {

    @Scheduled(initialDelay = 5000, fixedRate = 2000)
    public void scheduled() {
        log.info("DemoTask.scheduled...");
    }
}
