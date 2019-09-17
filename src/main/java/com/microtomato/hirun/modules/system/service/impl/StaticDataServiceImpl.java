package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.modules.system.entity.po.StaticData;
import com.microtomato.hirun.modules.system.mapper.StaticDataMapper;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

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
    public List<StaticData> getStaticDatas(String codeType) {
        List<StaticData> datas = this.list(new QueryWrapper<StaticData>().lambda()
                                                                         .eq(StaticData::getCodeType, codeType)
                                                                         .eq(StaticData::getStatus, "0")
                                                                         .orderByAsc(StaticData::getSortNo));
        return datas;
    }
}
