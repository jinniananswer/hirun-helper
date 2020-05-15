package com.microtomato.hirun.modules.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.mybatis.sequence.impl.CustIdMaxCycleSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.modules.system.entity.po.City;
import com.microtomato.hirun.modules.system.service.ICityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.demo.service.IStevenService;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("list")
    public void list() {

        for (int i = 0; i < 5; i++) {
            Page<City> page = cityService.page(new Page<>(i + 1, 5));
            List<City> records = page.getRecords();
            records.forEach(System.out::println);
            System.out.println("====================================");
        }

    }

}
