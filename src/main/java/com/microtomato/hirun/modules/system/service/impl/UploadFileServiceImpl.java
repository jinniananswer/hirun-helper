package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.system.entity.po.UploadFile;
import com.microtomato.hirun.modules.system.mapper.UploadFileMapper;
import com.microtomato.hirun.modules.system.service.IUploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 上传文件 服务实现类
 * </p>
 *
 * @author Steven
 * @since 2020-02-05
 */
@Slf4j
@Service
public class UploadFileServiceImpl extends ServiceImpl<UploadFileMapper, UploadFile> implements IUploadFileService {

    private UploadFile prepare(String batchId, File destDir, MultipartFile multipartFile) throws IOException {

        long fileSize = multipartFile.getSize();
        String fileName = multipartFile.getOriginalFilename();
        String uniqueId = UUID.randomUUID().toString();

        String newFileName = uniqueId + '.' + FilenameUtils.getExtension(fileName);
        File destFile = new File(destDir, newFileName);
        multipartFile.transferTo(destFile);

        String filePath = "upload" + StringUtils.substringAfterLast(destFile.getAbsolutePath(), "upload");
        log.debug("文件路径: {}", filePath);

        UploadFile uploadFile = UploadFile.builder()
            .batchId(batchId)
            .fileName(fileName)
            .filePath(filePath)
            .fileSize(fileSize)
            .enabled(false)
            //.createEmployeeId(WebContextUtils.getUserContext().getEmployeeId())
            .createEmployeeId(1L)
            .build();
        return uploadFile;
    }

    private File makesureFolderExist(String filePath) {

        // 分目录存储（按月）
        LocalDateTime localDateTime = LocalDateTime.now();
        String format = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String realPath = StringUtils.stripEnd(filePath, "/\\") + '/' + format;

        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        return folder;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public String uploadOne(MultipartFile multipartFile) throws IOException {
        String batchId = UUID.randomUUID().toString();
        String destPath = ResourceUtils.getURL("classpath:").getPath() + "../../upload";
        File destDir = makesureFolderExist(destPath);
        UploadFile uploadFile = prepare(batchId, destDir, multipartFile);
        this.save(uploadFile);

        log.debug("batchId: {}", batchId);
        return batchId;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @DataSource(DataSourceKey.SYS)
    public String uploadMulti(MultipartFile[] multipartFiles) throws IOException {
        String batchId = UUID.randomUUID().toString();
        String destPath = ResourceUtils.getURL("classpath:").getPath() + "../../upload";
        File destDir = makesureFolderExist(destPath);
        List<UploadFile> uploadFiles = new ArrayList();
        for (MultipartFile multipartFile : multipartFiles) {
            UploadFile uploadFile = prepare(batchId, destDir, multipartFile);
            uploadFiles.add(uploadFile);
        }

        this.saveBatch(uploadFiles);
        log.debug("batchId: {}", batchId);
        return batchId;
    }
}
