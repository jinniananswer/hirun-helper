package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.system.entity.po.StaticData;
import com.microtomato.hirun.modules.system.mapper.StaticDataMapper;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2019-09-14
 */
@Slf4j
@Service
public class StaticDataServiceImpl extends ServiceImpl<StaticDataMapper, StaticData> implements IStaticDataService {

    /**
     * 根据类型获取静态参数表数据
     *
     * @param codeType
     * @return
     */
    @Override
    @Cacheable(value = "static-data-with-codetype")
    public List<StaticData> getStaticDatas(String codeType) {
        IStaticDataService staticDataService = SpringContextUtils.getBean(StaticDataServiceImpl.class);
        List<StaticData> datas = staticDataService.getAllDatas();
        if (ArrayUtils.isEmpty(datas)) {
            return null;
        }

        List<StaticData> result = new ArrayList<>();
        for (StaticData data : datas) {
            if (StringUtils.equals(codeType, data.getCodeType())) {
                result.add(data);
            }
        }
        return result;
    }

    /**
     * 根据参数类型和参数值翻译参数名称
     *
     * @param codeType
     * @param codeValue
     * @return
     */
    @Override
    @Cacheable(value = "codename-with-codetype-value")
    public String getCodeName(String codeType, String codeValue) {
        if (StringUtils.isBlank(codeValue)) {
            return "";
        }
        IStaticDataService staticDataService = SpringContextUtils.getBean(StaticDataServiceImpl.class);
        List<StaticData> datas = staticDataService.getStaticDatas(codeType);
        if (ArrayUtils.isEmpty(datas)) {
            return null;
        }

        for (StaticData data : datas) {
            if (StringUtils.equals(codeValue, data.getCodeValue())) {
                return data.getCodeName();
            }
        }

        return null;
    }

    /**
     * 查询所有的静态参数数据
     *
     * @return
     */
    @Override
    @Cacheable(value = "static-data-all")
    public List<StaticData> getAllDatas() {
        List<StaticData> datas = this.list(
            Wrappers.<StaticData>lambdaQuery().eq(StaticData::getStatus, "0")
        );
        return datas;
    }
}
