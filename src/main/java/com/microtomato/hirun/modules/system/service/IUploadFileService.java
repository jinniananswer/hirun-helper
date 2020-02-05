package com.microtomato.hirun.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.system.entity.po.UploadFile;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 * 上传文件 服务类
 * </p>
 *
 * @author Steven
 * @since 2020-02-05
 */
public interface IUploadFileService extends IService<UploadFile> {

    String uploadOne(MultipartFile multipartFile) throws IOException;
    String uploadMulti(MultipartFile[] multipartFiles) throws IOException;
}
