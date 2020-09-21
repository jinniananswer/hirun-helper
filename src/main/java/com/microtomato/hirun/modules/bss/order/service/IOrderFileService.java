package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFileDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 订单文件 服务类
 * </p>
 *
 * @author Steven
 * @since 2020-02-06
 */
public interface IOrderFileService extends IService<com.microtomato.hirun.modules.bss.order.entity.po.OrderFile> {

    String toAbsolutePath(String relativePath);

    /**
     * 上传单个文件
     *
     * @param orderId 订单Id
     * @param stage 订单阶段
     * @param multipartFile
     * @return
     * @throws IOException
     */
    void uploadOne(Long orderId, Integer stage, MultipartFile multipartFile) throws IOException;

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
    List<OrderFile> listByIds(String ids);

    /**
     * 根据Id，删除上传文件
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据订单Id，和订单阶段，获取对应的文件
     *
     * @param orderId
     * @param stage
     * @return
     */
    OrderFile getOrderFile(Long orderId, Integer stage);

    /**
     * 查询订单文件信息供前台组件展示
     * @param orderId
     * @return
     */
    List<OrderFileDTO> queryOrderFiles(Long orderId);

    InputStream getInputStream(OrderFile orderFile);
}
