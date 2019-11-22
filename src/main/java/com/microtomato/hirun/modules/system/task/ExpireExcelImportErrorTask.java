package com.microtomato.hirun.modules.system.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.modules.system.entity.po.ExcelImportError;
import com.microtomato.hirun.modules.system.service.IExcelImportErrorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 清理过期的 Excel 导入异常数据
 *
 * @author Steven
 * @date 2019-11-22
 */
@Slf4j
@Component
public class ExpireExcelImportErrorTask {

    @Autowired
    private IExcelImportErrorService excelImportErrorServiceImpl;

    /**
     * 每天凌晨 5:30 开始执行。
     */
    @Scheduled(cron = "0 30 5 * * ?")
    public void scheduled() {

        LambdaQueryWrapper<ExcelImportError> lambdaQueryWrapper = Wrappers.<ExcelImportError>lambdaQuery()
            .lt(ExcelImportError::getCreateTime, LocalDateTime.now().minusDays(10));

        List<ExcelImportError> list = excelImportErrorServiceImpl.list(lambdaQueryWrapper);
        if (0 == list.size()) {
            log.info("未发现过期的 Excel 导入异常数据。");
            return;
        } else {
            log.info("发现过期的 Excel 导入异常数据 {} 条。", list.size());
        }

        excelImportErrorServiceImpl.remove(lambdaQueryWrapper);
    }

}
