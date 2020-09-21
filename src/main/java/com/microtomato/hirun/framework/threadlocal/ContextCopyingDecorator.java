package com.microtomato.hirun.framework.threadlocal;

import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;

/**
 * 把 Context 传递到线程中
 *
 * @author Steven
 * @date 2020-05-15
 */
public class ContextCopyingDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        RequestAttributes context = RequestContextHolder.currentRequestAttributes();
        LocalDateTime requestTime = RequestTimeHolder.getRequestTime();
        return () -> {
            try {
                RequestContextHolder.setRequestAttributes(context);
                RequestTimeHolder.addRequestTime(requestTime);
                runnable.run();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        };
    }

}
