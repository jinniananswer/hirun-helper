package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.WebContextUtils;
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
import java.io.FileNotFoundException;
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

    @Override
    public String toAbsolutePath(String relativePath) {
        String absolutePath = null;

        try {
            absolutePath = ResourceUtils.getURL("classpath:").getPath() + "../../" + relativePath;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return absolutePath;
    }

    private UploadFile prepare(File destDir, MultipartFile multipartFile) throws IOException {

        long fileSize = multipartFile.getSize();
        String fileName = multipartFile.getOriginalFilename();
        String uniqueId = UUID.randomUUID().toString();

        String newFileName = uniqueId + '.' + FilenameUtils.getExtension(fileName);
        File destFile = new File(destDir, newFileName);
        multipartFile.transferTo(destFile);

        String filePath = "upload" + StringUtils.substringAfterLast(destFile.getAbsolutePath(), "upload");
        log.debug("文件路径: {}", filePath);

        UploadFile uploadFile = UploadFile.builder()
            .fileName(fileName)
            .filePath(filePath)
            .fileSize(fileSize)
            .enabled(false)
            .createEmployeeId(WebContextUtils.getUserContext().getEmployeeId())
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

    private List<Long> convert(String ids) {
        String[] split = StringUtils.split(ids, ',');

        List<Long> idList = new ArrayList<>();
        for (String s : split) {
            idList.add(Long.parseLong(s));
        }
        return idList;
    }

    /**
     * 获取文件上传基础目录
     *
     * @return
     */
    private String getDestPath() {
        String destPath = null;
        try {
            destPath = ResourceUtils.getURL("classpath:").getPath() + "../../upload";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return destPath;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public Long uploadOne(MultipartFile multipartFile) throws IOException {
        String destPath = getDestPath();
        File destDir = makesureFolderExist(destPath);
        UploadFile uploadFile = prepare(destDir, multipartFile);
        this.save(uploadFile);

        return uploadFile.getId();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public String uploadMulti(MultipartFile[] multipartFiles) throws IOException {

        String destPath = getDestPath();
        File destDir = makesureFolderExist(destPath);

        List<Long> idList = new ArrayList();
        for (MultipartFile multipartFile : multipartFiles) {
            UploadFile uploadFile = prepare(destDir, multipartFile);
            this.save(uploadFile);
            idList.add(uploadFile.getId());
        }

        return StringUtils.join(idList, ',');
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void confirmUpload(String ids) {
        List<Long> idList = convert(ids);
        this.update(
            Wrappers.<UploadFile>lambdaUpdate().
                setSql("enabled = true")
                .in(UploadFile::getId, idList)
        );
    }

    @Override
    public List<UploadFile> listByIds(String ids) {

        List<Long> idList = convert(ids);
        List<UploadFile> uploadFiles = this.list(
            Wrappers.<UploadFile>lambdaQuery()
                .in(UploadFile::getId, idList)
                .eq(UploadFile::getEnabled, true)
        );
        return uploadFiles;
    }

    @Override
    public void deleteById(Long id) {
        this.update(
            Wrappers.<UploadFile>lambdaUpdate()
                .setSql("enabled = false")
                .eq(UploadFile::getId, id)
        );
    }

}
