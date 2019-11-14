package com.microtomato.hirun.modules.user.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
     * 删除过期时间小于当前时间的，以及过期时间大于当前时间 30 天的异常数据。
     */
    @Scheduled(cron="0 10 5 * * ?")
    public void scheduled() {
        LambdaQueryWrapper<MenuTemp> lambdaQueryWrapper = Wrappers.<MenuTemp>lambdaQuery()
            .lt(MenuTemp::getExpireDate, LocalDateTime.now())
            .or()
            .gt(MenuTemp::getExpireDate, LocalDateTime.now().plusDays(30));

        List<MenuTemp> list = menuTempServiceImpl.list(lambdaQueryWrapper);
        if (0 == list.size()) {
            log.info("未发现需要清理的过期临时菜单权限");
            return;
        } else {
            log.info("发现过期的临时菜单权限 {} 条。", list.size());
        }

        menuTempServiceImpl.remove(lambdaQueryWrapper);
        for (MenuTemp menuTemp : list) {
            log.info("清理过期的临时菜单权限: {}", menuTemp);
        }
    }

}
