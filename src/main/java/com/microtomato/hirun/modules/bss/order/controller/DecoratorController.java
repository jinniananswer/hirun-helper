package com.microtomato.hirun.modules.bss.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.ConstructionDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.DecoratorInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.Decorator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.order.service.IDecoratorService;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 装修工人表 前端控制器
 * </p>
 *
 * @author sunxin
 * @since 2020-03-04
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.order/decorator")
public class DecoratorController {

    @Autowired
    private IDecoratorService decoratorServiceImpl;

    @GetMapping("/selectTypeDecorator")
    @RestResult
    public List<Decorator> selectTypeDecorator(Long type) {
        UserContext userContext = WebContextUtils.getUserContext();
        Long orgId = userContext.getOrgId();
        if (type == null) {
            return  this.decoratorServiceImpl.queryAllInfo();
        }
        else{
            return this.decoratorServiceImpl.queryDecoratorInfo(orgId, type);
        }
    }

    @GetMapping("/queryDecoratorInfo")
    @RestResult
    public IPage<Decorator> queryDecoratorInfo(String name, String identityNo, int page, int size) {
        return this.decoratorServiceImpl.queryDecoratorInfo(name, identityNo, page, size);
    }

}
