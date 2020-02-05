package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.modules.system.entity.po.UploadFile;
import com.microtomato.hirun.modules.system.service.IUploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
 * 上传文件 前端控制器
 * </p>
 *
 * @author Steven
 * @since 2020-02-05
 */
@Slf4j
@RestController
@RequestMapping("/api/system/file")
public class UploadFileController {

    @Autowired
    private IUploadFileService uploadFileService;

    @Value("${hirun.file.store}")
    private String hirunFileStore;

    @PostMapping("uploadOne")
    public String uploadOne(@RequestParam("fileUpload") MultipartFile multipartFile) throws IOException {

        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("上传失败，请先选择文件。");
        }

        String batchId = uploadFileService.uploadOne(multipartFile);
        return batchId;
    }

    @PostMapping("uploadMulti")
    public String uploadMulti(@RequestParam("fileUploads") MultipartFile[] multipartFiles) throws IOException {

        if (null == multipartFiles || 0 == multipartFiles.length) {
            throw new IllegalArgumentException("上传失败，请先选择文件。");
        }

        String batchId = uploadFileService.uploadMulti(multipartFiles);
        return batchId;
    }



}
