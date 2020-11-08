package com.microtomato.hirun.modules.bss.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.DecoratorInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.DecoratorServiceDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.Decorator;
import com.microtomato.hirun.modules.bss.order.service.IDecoratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public IPage<DecoratorServiceDTO> queryDecoratorInfo(String name, String identityNo, String decoratorType, int page, int size) {
        return this.decoratorServiceImpl.queryDecoratorInfo(name, identityNo, decoratorType, page, size);
    }

    @GetMapping("/initDecorators")
    @RestResult
    public List<DecoratorInfoDTO> initDecorators() {
        return this.decoratorServiceImpl.initDecorators();
    }

    @PostMapping("/addDecorator")
    @RestResult
    public void addDecorator(@RequestBody Decorator decorator) {
        decorator.setStatus("0");
        this.decoratorServiceImpl.save(decorator);
    }

    @PostMapping("deleteDecoratorBatch")
    @RestResult
    public void deleteDecoratorBatch(@RequestBody List<Decorator> decorators) {
        List<Long> decoratorIds = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(decorators)) {
            for (Decorator decorator : decorators) {
                decoratorIds.add(decorator.getDecoratorId());
            }
            this.decoratorServiceImpl.removeByIds(decoratorIds);
        }
    }
}
