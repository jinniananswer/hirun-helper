package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.modules.system.service.ICityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jinnian
 * @since 2019-11-17
 */
@RestController
@Slf4j
@RequestMapping("/api/system/city")
public class CityController {

    @Autowired
    private ICityService cityServiceImpl;

}
