package com.microtomato.hirun.modules.bss.config.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.config.service.ICollectionItemCfgService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 非主营收款项配置表 前端控制器
 * </p>
 *
 * @author sunxin
 * @since 2020-03-09
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.config/collection-item-cfg")
public class CollectionItemCfgController {

    @Autowired
    private ICollectionItemCfgService collectionItemCfgServiceImpl;



}
