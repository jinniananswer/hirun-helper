package com.microtomato.hirun.modules.bss.config.service;

import com.microtomato.hirun.modules.bss.config.entity.po.CollectionItemCfg;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.config.entity.po.PayItemCfg;

import java.util.List;

/**
 * <p>
 * 非主营收款项配置表 服务类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-09
 */
public interface ICollectionItemCfgService extends IService<CollectionItemCfg> {

    List<CollectionItemCfg> queryAll();

    CollectionItemCfg getFeeItem(Long collectionId);

    List<CollectionItemCfg> queryPlusCollectionyItems();

   // String getPath(Long payItemId);

}
