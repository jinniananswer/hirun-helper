package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFile;
import com.microtomato.hirun.modules.bss.order.service.IOrderFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 订单文件 前端控制器
 * </p>
 *
 * @author Steven
 * @since 2020-02-06
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-file")
public class OrderFileController {

    @Autowired
    private IOrderFileService orderFileService;

    @PostMapping("uploadOne")
    public void uploadOne(Long orderId, Integer stage, @RequestParam("file") MultipartFile multipartFile) throws IOException {

        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("上传失败，请先选择文件。");
        }

        orderFileService.uploadOne(orderId, stage, multipartFile);
    }

    @GetMapping("confirmUpload/{ids}")
    @RestResult
    public void confirmUpload(@PathVariable("ids") String ids) {
        orderFileService.confirmUpload(ids);
    }

    @GetMapping("listByIds/{ids}")
    @RestResult
    public List<OrderFile> listByIds(@PathVariable("ids") String ids) {
        return orderFileService.listByIds(ids);
    }

    @GetMapping("deleteById/{id}")
    @RestResult
    public void deleteById(@PathVariable("id") Long id) {
        orderFileService.deleteById(id);
    }

    @RequestMapping("download/{orderId}/{stage}")
    public ResponseEntity<InputStreamResource> download(@PathVariable("orderId") Long orderId, @PathVariable("stage") Integer stage) throws IOException {
        OrderFile orderFile = orderFileService.getOrderFileAbsolutePath(orderId, stage);
        FileSystemResource file = new FileSystemResource(orderFile.getFilePath());
        String filename = new String(orderFile.getFileName().getBytes("UTF-8"),"ISO-8859-1");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
            .ok()
            .headers(headers)
            .contentLength(file.contentLength())
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(new InputStreamResource(file.getInputStream()));
    }

}
