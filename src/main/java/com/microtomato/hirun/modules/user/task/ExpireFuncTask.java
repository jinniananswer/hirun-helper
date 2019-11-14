package com.microtomato.hirun.modules.user.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.modules.user.entity.po.FuncTemp;
import com.microtomato.hirun.modules.user.service.IFuncTempService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 清理过期的临时操作权限
 *
 * @author Steven
 * @date 2019-11-14
 */
@Slf4j
@Component
public class ExpireFuncTask {

    @Autowired
    private IFuncTempService funcTempServiceImpl;

    /**
     * 每天凌晨 5:00 开始执行。
     */
    @Scheduled(cron="0 0 5 * * ?")
    public void scheduled() {
        LambdaQueryWrapper<FuncTemp> lambdaQueryWrapper = Wrappers.<FuncTemp>lambdaQuery().lt(FuncTemp::getExpireDate, LocalDateTime.now());
        List<FuncTemp> list = funcTempServiceImpl.list(lambdaQueryWrapper);
        if (0 == list.size()) {
            log.info("未发现需要清理的过期临时操作权限");
            return;
        } else {
            log.info("发现过期的临时操作权限 {} 条。", list.size());
        }

        funcTempServiceImpl.remove(lambdaQueryWrapper);
        for (FuncTemp funcTemp : list) {
            log.info("清理过期的临时操作权限: {}", funcTemp);
        }
    }
}
