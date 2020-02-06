package com.microtomato.hirun.modules.system.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.modules.system.entity.po.UploadFile;
import com.microtomato.hirun.modules.system.service.IUploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Steven
 * @date 2020-02-06
 */
@Slf4j
@Component
public class UploadFileTask {

    @Autowired
    private IUploadFileService uploadFileService;

    /**
     * 每天凌晨 6:00 开始执行。
     */
    @Scheduled(cron = "0 0 6 * * ?")
    public void scheduled() {

        log.info("开始清理未被确认的上传文件...");

        List<UploadFile> list = uploadFileService.list(
            Wrappers.<UploadFile>lambdaQuery()
                .eq(UploadFile::getEnabled, false)
                .lt(UploadFile::getUpdateTime, LocalDateTime.now().minusHours(24))
        );

        for (UploadFile uploadFile : list) {
            String filePath = uploadFileService.toAbsolutePath(uploadFile.getFilePath());
            log.info("删除未被确认的上传文件: {}", filePath);
            FileUtils.deleteQuietly(new File(filePath));
            uploadFileService.removeById(uploadFile.getId());
        }

    }
}
