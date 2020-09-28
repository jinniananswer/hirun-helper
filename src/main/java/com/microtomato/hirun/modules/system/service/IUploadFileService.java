package com.microtomato.hirun.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.system.entity.po.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 上传文件 服务类
 * </p>
 *
 * @author Steven
 * @since 2020-02-05
 */
public interface IUploadFileService extends IService<UploadFile> {

    String toAbsolutePath(String relativePath);

    InputStream getInputStream(UploadFile uploadFile);

    /**
     * 上传单个文件
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    String uploadOne(MultipartFile multipartFile) throws IOException;

    /**
     * 上传多个文件
     *
     * @param multipartFiles
     * @return
     * @throws IOException
     */
    String uploadMulti(MultipartFile[] multipartFiles) throws IOException;

    /**
     * 确认上传文件
     *
     * @param ids
     */
    void confirmUpload(String ids);

    /**
     * 根据 ids 查文件清单
     *
     * @param ids
     * @return
     */
    List<UploadFile> listByIds(String ids);

    /**
     * 根据Id，删除上传文件
     *
     * @param id
     */
    void deleteById(String id);

    UploadFile selectById(String id);
}
