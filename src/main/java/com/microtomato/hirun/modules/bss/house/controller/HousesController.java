package com.microtomato.hirun.modules.bss.house.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.house.entity.po.Houses;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
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

    @Autowired
    private IStaticDataService staticDataService;

    @GetMapping("queryHouse")
    @RestResult
    public List<Houses> queryHouse(){
        List<Houses> housesList=housesServiceImpl.list();
        if(ArrayUtils.isNotEmpty(housesList)){
            for(Houses houses:housesList){
                String natureName=staticDataService.getCodeName("HOUSE_NATURE",houses.getNature()+"");
                houses.setName(houses.getName()+"【"+natureName+"】");
            }
        }
        return housesList;
    }

}
