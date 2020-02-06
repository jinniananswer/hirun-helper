package com.microtomato.hirun.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.system.entity.po.UploadFile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    /**
     * 上传单个文件
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    Long uploadOne(MultipartFile multipartFile) throws IOException;

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
     * @param batchId
     */
    void confirmUpload(String batchId);

    /**
     * 根据 batchId 查文件清单
     *
     * @param batchId
     * @return
     */
    List<UploadFile> listByBatchId(String batchId);

    /**
     * 根据Id，删除上传文件
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据 batchId，删除上传文件
     *
     * @param batchId
     */
    void deleteByBatchId(String batchId);
}
