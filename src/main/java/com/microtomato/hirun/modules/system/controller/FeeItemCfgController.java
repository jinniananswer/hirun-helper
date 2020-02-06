package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.system.entity.po.FeeItemCfg;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.system.service.IFeeItemCfgService;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 费用项配置表 前端控制器
 * </p>
 *
 * @author sunxin
 * @since 2020-02-06
 */
@RestController
@Slf4j
@RequestMapping("/api/system/fee-item-cfg")
public class FeeItemCfgController {

    @Autowired
    private IFeeItemCfgService feeItemCfgServiceImpl;


    @RequestMapping("/getFeeItemDatas")
    @RestResult
    public List<FeeItemCfg> getFeeItemDatas(String codeType) {
        return feeItemCfgServiceImpl.getFeeItemDatas(codeType);
    }

}
