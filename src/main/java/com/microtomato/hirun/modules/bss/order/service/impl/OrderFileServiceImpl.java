package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFileDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFile;
import com.microtomato.hirun.modules.bss.order.mapper.OrderFileMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderFileService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
 * 订单文件 服务实现类
 * </p>
 *
 * @author Steven
 * @since 2020-02-06
 */
@Slf4j
@Service
public class OrderFileServiceImpl extends ServiceImpl<OrderFileMapper, com.microtomato.hirun.modules.bss.order.entity.po.OrderFile> implements IOrderFileService {

    private static final String UPLOAD = "upload";

    @Autowired
    private Environment env;

    @Autowired
    private IStaticDataService staticDataService;

    @Override
    public String toAbsolutePath(String relativePath) {
        String absolutePath = null;

        try {
            absolutePath = env.getProperty("upload.path");
            if (null == absolutePath) {
                absolutePath = ResourceUtils.getURL("classpath:").getPath() + "../..";
            }
            absolutePath = absolutePath + "/" + relativePath;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return absolutePath;
    }

    /**
     * 获取文件上传基础目录
     *
     * @return
     */
    private String getDestPath() {
        String destPath = null;
        try {
            destPath = env.getProperty("upload.path");
            if (null == destPath) {
                destPath = ResourceUtils.getURL("classpath:").getPath() + "../..";
            }
            destPath = destPath + "/" + UPLOAD + "/order";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return destPath;
    }

    private OrderFile prepare(File destDir, MultipartFile multipartFile, Long orderId, Integer stage) throws IOException {

        long fileSize = multipartFile.getSize();
        String fileName = multipartFile.getOriginalFilename();
        String uniqueId = UUID.randomUUID().toString();

        String newFileName = uniqueId + '.' + FilenameUtils.getExtension(fileName);
        File destFile = new File(destDir, newFileName);
        multipartFile.transferTo(destFile);

        String filePath = UPLOAD + StringUtils.substringAfterLast(destFile.getAbsolutePath(), UPLOAD);
        log.debug("文件路径: {}", filePath);

        OrderFile orderFile = OrderFile.builder()
                .orderId(orderId)
                .stage(stage)
                .fileName(fileName)
                .filePath(filePath)
                .fileSize(fileSize)
                .enabled(true)
                .build();
        return orderFile;
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

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void uploadOne(Long orderId, Integer stage, MultipartFile multipartFile) throws IOException {
        String destPath = getDestPath();
        File destDir = makesureFolderExist(destPath);
        OrderFile orderFile = prepare(destDir, multipartFile, orderId, stage);

        this.update(
                Wrappers.<OrderFile>lambdaUpdate()
                        .setSql("enabled = false")
                        .eq(OrderFile::getOrderId, orderId)
                        .eq(OrderFile::getStage, stage)
        );

        this.save(orderFile);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void confirmUpload(String ids) {
        List<Long> idList = convert(ids);
        this.update(
                Wrappers.<OrderFile>lambdaUpdate().
                        setSql("enabled = true")
                        .in(OrderFile::getId, idList)
        );
    }

    @Override
    public List<OrderFile> listByIds(String ids) {

        List<Long> idList = convert(ids);
        List<OrderFile> orderFiles = this.list(
                Wrappers.<OrderFile>lambdaQuery()
                        .in(OrderFile::getId, idList)
                        .eq(OrderFile::getEnabled, true)
        );
        return orderFiles;
    }

    @Override
    public void deleteById(Long id) {
        this.update(
                Wrappers.<OrderFile>lambdaUpdate()
                        .setSql("enabled = false")
                        .eq(OrderFile::getId, id)
        );
    }

    @Override
    public OrderFile getOrderFile(Long orderId, Integer stage) {
        OrderFile orderFile = this.getOne(
                Wrappers.<OrderFile>lambdaQuery()
                        .eq(OrderFile::getOrderId, orderId)
                        .eq(OrderFile::getStage, stage)
                        .eq(OrderFile::getEnabled, true)
                        .orderByDesc(OrderFile::getCreateTime)
        );
        return orderFile;
    }

    @Override
    public List<OrderFileDTO> queryOrderFiles(Long orderId) {
        List<OrderFile> files = this.list(Wrappers.<OrderFile>lambdaQuery()
                .eq(OrderFile::getOrderId, orderId)
                .eq(OrderFile::getEnabled, true)
                .orderByAsc(OrderFile::getCreateTime));

        if (ArrayUtils.isEmpty(files)) {
            return null;
        }

        List<OrderFileDTO> orderFiles = new ArrayList<>();
        for (OrderFile file : files) {
            OrderFileDTO orderFile = new OrderFileDTO();
            BeanUtils.copyProperties(file, orderFile);
            orderFile.setStageName(this.staticDataService.getCodeName("FILE_STAGE", file.getStage() + ""));
            orderFiles.add(orderFile);
        }
        return orderFiles;
    }
}
