package com.microtomato.hirun.modules.system.service;

import com.microtomato.hirun.modules.system.entity.po.FeeItemCfg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 费用项配置表 服务类
 * </p>
 *
 * @author sunxin
 * @since 2020-02-06
 */
public interface IFeeItemCfgService extends IService<FeeItemCfg> {

    /**
     * 根据 费用大类 获取费用小类或者品牌配置数据
     *
     * @param codeType
     * @return 费用数据列表
     */
    List<FeeItemCfg> getFeeItemDatas(String codeType);


    List<FeeItemCfg> getAllFeeItemDatas();

    /**
     * 根据feeItemId获取name
     */
    String getFeeItemNameFeeItemId(Long feeItemId);


}
