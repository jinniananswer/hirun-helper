package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.system.entity.po.UploadFile;
import com.microtomato.hirun.modules.system.service.IUploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    @PostMapping("uploadOne")
    public Long uploadOne(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("上传失败，请先选择文件。");
        }

        return uploadFileService.uploadOne(multipartFile);
    }

    @PostMapping("uploadMulti")
    public String uploadMulti(@RequestParam("file") MultipartFile[] multipartFiles) throws IOException {

        if (null == multipartFiles || 0 == multipartFiles.length) {
            throw new IllegalArgumentException("上传失败，请先选择文件。");
        }

        String batchId = uploadFileService.uploadMulti(multipartFiles);
        return batchId;
    }

    @GetMapping("confirmUpload/{batchId}")
    @RestResult
    public void confirmUpload(@PathVariable("batchId") String batchId) {
        uploadFileService.confirmUpload(batchId);
    }

    @GetMapping("listByBatchId/{batchId}")
    @RestResult
    public List<UploadFile> listByBatchId(@PathVariable("batchId") String batchId) {
        return uploadFileService.listByBatchId(batchId);
    }

    @GetMapping("deleteById/{id}")
    @RestResult
    public void deleteById(@PathVariable("id") Long id) {
        uploadFileService.deleteById(id);
    }

    @GetMapping("deleteByBatchId/{batchId}")
    @RestResult
    public void deleteByBatchId(@PathVariable("batchId") String batchId) {
        uploadFileService.deleteByBatchId(batchId);
    }

}
