package com.microtomato.hirun.modules.system.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.system.entity.po.StaticData;
import com.microtomato.hirun.modules.system.mapper.StaticDataMapper;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2019-09-14
 */
@Slf4j
@Service
public class StaticDataServiceImpl extends ServiceImpl<StaticDataMapper, StaticData> implements IStaticDataService {

    @Override
    @Cacheable(value = "static-data-with-codetype")
    public List<StaticData> getStaticDatas(String codeType) {
        List<StaticData> datas = this.list(new QueryWrapper<StaticData>().lambda()
                                                                         .eq(StaticData::getCodeType, codeType)
                                                                         .eq(StaticData::getStatus, "0")
                                                                         .orderByAsc(StaticData::getSortNo));
        return datas;
    }

    @Override
    @Cacheable(value="codename-with-codetype-value")
    public String getCodeName(String codeType, String codeValue) {
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
}
