package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.modules.system.service.IFuncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@RestController
@Slf4j
@RequestMapping("/api/system/func")
public class FuncController {

    @Autowired
    private IFuncService funcServiceImpl;

}
