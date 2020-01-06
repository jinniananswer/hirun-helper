package com.microtomato.hirun.modules.demo.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.modules.demo.service.IDemoService;
import com.microtomato.hirun.modules.user.entity.po.FuncTemp;
import com.microtomato.hirun.modules.user.service.IFuncTempService;
import jdk.nashorn.internal.runtime.regexp.joni.WarnCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-10-30
 */
@RestController
@Slf4j
@RequestMapping("/api/demo/demo")
public class DemoController {

    @Autowired
    private IDemoService demoServiceImpl;

    @Autowired
    private IFuncTempService funcTempService;

    @GetMapping("/save")
    public void testSave() {
        demoServiceImpl.testSave();
    }

    @GetMapping("update")
    public void testUpdate() {
        demoServiceImpl.testUpdate();
    }

    @GetMapping("saveOrUpdate")
    public void testSaveOrUpdate() {
        demoServiceImpl.testSaveOrUpdate();
    }

    @GetMapping("testInsertAndSelect")
    public void testInsertAndSelect() {
        FuncTemp funcTemp = FuncTemp.builder().funcId(1L).createTime(LocalDateTime.now()).createUserId(1L).expireDate(LocalDateTime.now().plusDays(20)).userId(1L).build();
        log.error("插入前 PO 对象: {}", funcTemp);
        funcTempService.save(funcTemp);
        FuncTemp one = funcTempService.getOne(Wrappers.<FuncTemp>lambdaQuery().eq(FuncTemp::getId, funcTemp.getId()));
        log.error("从表里查出的 PO 对象: {}", one);
    }

}
