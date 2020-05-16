package com.microtomato.hirun.modules.demo.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.mybatis.sequence.impl.CustIdMaxCycleSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.modules.demo.service.IStevenService;
import com.microtomato.hirun.modules.system.entity.po.City;
import com.microtomato.hirun.modules.system.service.ICityService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-12-19
 */
@RestController
@Slf4j
@RequestMapping("/api/sysdemo/steven")
public class StevenController {

    @Autowired
    private IStevenService stevenServiceImpl;

    @Autowired
    private IDualService dualService;

    @Autowired
    private ICityService cityService;

    @RestResult
    @GetMapping("nextval")
    public Long nextval() {
        return dualService.nextval(CustIdMaxCycleSeq.class);
    }

    @RestResult
    @GetMapping("nextval/{sequenceName}")
    public Long nextval(@PathVariable("sequenceName") String sequenceName) {
        return dualService.nextval(sequenceName);
    }

    private static int[] xx = {
        110100,
        120100,
        130100,
        130200,
        130300,
        130400,
        130500,
        130600,
        130700
    };

    @SneakyThrows
    @GetMapping("list")
    public void list() {

        log.info("=========================");
        log.info("=========================");
        log.info("=========================");
        log.info("RequestTimeHolder.getRequestTime(): {}", RequestTimeHolder.getRequestTime());

        Future<City> cityId_110100 = cityService.getCityByIdAsync(110100);
        Future<City> cityId_120100 = cityService.getCityByIdAsync(120100);
        Future<City> cityId_130100 = cityService.getCityByIdAsync(130100);
        Future<City> cityId_130200 = cityService.getCityByIdAsync(130200);

        log.info("{}", cityId_110100.get());
        log.info("{}", cityId_120100.get());
        log.info("{}", cityId_130100.get());
        log.info("{}", cityId_130200.get());

    }

}
