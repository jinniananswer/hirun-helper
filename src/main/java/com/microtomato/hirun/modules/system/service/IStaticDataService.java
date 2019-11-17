package com.microtomato.hirun.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.system.entity.po.StaticData;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @since 2019-09-14
 */
public interface IStaticDataService extends IService<StaticData> {

    /**
     * 根据 codeType 获取静态配置数据
     *
     * @param codeType
     * @return 静态数据列表
     */
    List<StaticData> getStaticDatas(String codeType);

    String getCodeName(String codeType, String codeValue);

    List<StaticData> getAllDatas();
}
