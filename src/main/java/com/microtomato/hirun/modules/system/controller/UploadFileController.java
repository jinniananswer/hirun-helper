package com.microtomato.hirun.modules.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.system.entity.po.UploadFile;
import com.microtomato.hirun.modules.system.service.IUploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Value("${hirun.data.path}")
    private String dataPath;

    /**
     * 根据文件后缀，来做 mime 类型映射。
     */
    private static Map<String, String> mimeMap = new HashMap(16) {{
        put(".pdf", "application/pdf");
        put(".doc", "application/msword");
        put(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        put(".xls", "application/vnd.ms-excel");
        put(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        put(".ppt", "application/vnd.ms-powerpoint");
        put(".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
    }};


    @PostMapping("uploadOne")
    public String uploadOne(@RequestParam("file") MultipartFile multipartFile) throws IOException {

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

    @GetMapping("confirmUpload/{ids}")
    @RestResult
    public void confirmUpload(@PathVariable("ids") String ids) {
        uploadFileService.confirmUpload(ids);
    }

    @GetMapping("listByIds/{ids}")
    @RestResult
    public List<UploadFile> listByIds(@PathVariable("ids") String ids) {
        return uploadFileService.listByIds(ids);
    }

    @GetMapping("deleteById/{id}")
    @RestResult
    public void deleteById(@PathVariable("id") String id) {
        uploadFileService.deleteById(id);
    }

    /**
     * 通过浏览器显示文件
     *
     * @param id
     * @param response
     * @throws IOException
     */
    //@GetMapping("/display/{id}")
    public void display_bak(@PathVariable("id") String id, HttpServletResponse response) throws IOException {

        UploadFile uploadFile = uploadFileService.getOne(Wrappers.<UploadFile>lambdaQuery().eq(UploadFile::getId, id));
        Assert.notNull(uploadFile, "展示失败,无法找到对应的文件,Id:" + id);

        String filePath = StringUtils.removeStart(uploadFile.getFilePath(), "/");
        String realPath = FilenameUtils.concat(dataPath, filePath);

        FileInputStream fileInputStream = FileUtils.openInputStream(new File(realPath));
        String extension = "." + FilenameUtils.getExtension(filePath);
        String contentType = mimeMap.get(extension);

        Assert.notNull(contentType, "根据文件后缀: " + extension + "，无法识别正确的 mime 类型");

        response.setHeader("Content-Type", contentType);
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copy(fileInputStream, outputStream);
        outputStream.flush();

    }

    /**
     * 通过浏览器显示文件
     *
     * @param id
     * @param response
     * @throws IOException
     */
    @GetMapping("/display/{id}")
    public void display(@PathVariable("id") String id, HttpServletResponse response) throws IOException {


        UploadFile uploadFile = uploadFileService.getOne(Wrappers.<UploadFile>lambdaQuery().eq(UploadFile::getId, id));
        Assert.notNull(uploadFile, "展示失败,无法找到对应的文件,Id:" + id);

        InputStream inputStream = uploadFileService.getInputStream(uploadFile);
        String extension = "." + FilenameUtils.getExtension(uploadFile.getFilePath());
        String contentType = mimeMap.get(extension);
        Assert.notNull(contentType, "根据文件后缀: " + extension + "，无法识别正确的 mime 类型");

        response.setHeader("Content-Type", contentType);
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copy(inputStream, outputStream);
        outputStream.flush();

    }

    /**
     * 下载文件
     *
     * @param id
     * @param response
     * @throws IOException
     */
    //@GetMapping("/download/{id}")
    public void download_bak(@PathVariable("id") String id, HttpServletResponse response) throws IOException {

        UploadFile uploadFile = uploadFileService.getOne(Wrappers.<UploadFile>lambdaQuery().eq(UploadFile::getId, id));
        Assert.notNull(uploadFile, "下载失败,无法找到对应的文件,Id:" + id);

        String filePath = StringUtils.removeStart(uploadFile.getFilePath(), "/");
        String realPath = FilenameUtils.concat(dataPath, filePath);

        FileInputStream fileInputStream = FileUtils.openInputStream(new File(realPath));

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", uploadFile.getFileName()));
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copy(fileInputStream, outputStream);
        outputStream.flush();

    }

    /**
     * 下载文件
     *
     * @param id
     * @param response
     * @throws IOException
     */
    public void download(@PathVariable("id") String id, HttpServletResponse response) throws IOException {

        UploadFile uploadFile = uploadFileService.getOne(Wrappers.<UploadFile>lambdaQuery().eq(UploadFile::getId, id));
        Assert.notNull(uploadFile, "下载失败,无法找到对应的文件,Id:" + id);

        InputStream inputStream = uploadFileService.getInputStream(uploadFile);

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", uploadFile.getFileName()));
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copy(inputStream, outputStream);
        outputStream.flush();

    }
}
