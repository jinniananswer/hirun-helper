package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.system.entity.po.StaticData;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jinnian
 * @since 2019-09-14
 */
@RestController
@Slf4j
@RequestMapping("api/system/static-data")
public class StaticDataController {

    @Autowired
    private IStaticDataService staticDataServiceImpl;

    @RequestMapping("/getByCodeType")
    @RestResult
    public List<StaticData> getStaticDatas(String codeType) {
        return staticDataServiceImpl.getStaticDatas(codeType);
    }

}
