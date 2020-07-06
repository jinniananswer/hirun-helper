package com.microtomato.hirun.modules.bss.house.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.house.entity.po.Houses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.house.service.IHousesService;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2020-02-11
 */
@RestController
@Slf4j
@RequestMapping("/api/house/houses")
public class HousesController {

    @Autowired
    private IHousesService housesServiceImpl;

    @GetMapping("queryHouse")
    @RestResult
    public List<Houses> queryHouse(){
        List<Houses> housesList=housesServiceImpl.list();
        return housesList;
    }

}
