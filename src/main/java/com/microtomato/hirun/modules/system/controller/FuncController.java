package com.microtomato.hirun.modules.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.system.entity.dto.FuncDTO;
import com.microtomato.hirun.modules.system.entity.po.Func;
import com.microtomato.hirun.modules.system.service.IFuncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@RestController
@Slf4j
@RequestMapping("api/system/func")
public class FuncController {

    @Autowired
    private IFuncService funcServiceImpl;

    @GetMapping("func-list")
    @RestResult
    public List<FuncDTO> funcList(@RequestParam(required = false) Long roleId) {

        Set<Long> funcIds = new HashSet<>();
        if (null != roleId) {
            funcIds = funcServiceImpl.queryFuncId(roleId);
        }

        List<Func> funcList = funcServiceImpl.list(
            Wrappers.<Func>lambdaQuery()
                .select(Func::getFuncId, Func::getFuncCode, Func::getFuncDesc)
                .eq(Func::getType, "1")
        );

        List<FuncDTO> funcDTOS = new ArrayList<>();
        for (Func func : funcList) {
            FuncDTO funcDTO = new FuncDTO();
            funcDTO.setFuncId(func.getFuncId());
            funcDTO.setFuncCode(func.getFuncCode());
            funcDTO.setFuncDesc(func.getFuncDesc());
            funcDTO.setChecked(funcIds.contains(func.getFuncId()));

            funcDTOS.add(funcDTO);
        }

        return funcDTOS;
    }


}
