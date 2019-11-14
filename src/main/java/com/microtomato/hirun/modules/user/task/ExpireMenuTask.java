package com.microtomato.hirun.modules.user.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.modules.user.entity.po.MenuTemp;
import com.microtomato.hirun.modules.user.service.IMenuTempService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 清理过期的临时菜单权限
 *
 * @author Steven
 * @date 2019-11-14
 */
@Slf4j
@Component
public class ExpireMenuTask {

    @Autowired
    private IMenuTempService menuTempServiceImpl;

    /**
     * 每天凌晨 5:10 开始执行。
     */
    @Scheduled(cron="0 10 5 * * ?")
    public void scheduled() {
        log.info("开始清理过期的临时菜单权限");
        // 为了打印日志，因此没有直接 delete。
        List<MenuTemp> list = menuTempServiceImpl.list(Wrappers.<MenuTemp>lambdaQuery().lt(MenuTemp::getExpireDate, LocalDateTime.now()));
        for (MenuTemp menuTemp : list) {
            menuTempServiceImpl.removeById(menuTemp.getId());
            log.info("清理: {}", menuTemp);
        }
    }

}
