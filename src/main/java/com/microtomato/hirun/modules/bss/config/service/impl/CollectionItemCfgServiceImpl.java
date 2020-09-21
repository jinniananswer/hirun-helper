package com.microtomato.hirun.modules.bss.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.CollectionItemCfg;
import com.microtomato.hirun.modules.bss.config.entity.po.FeeItemCfg;
import com.microtomato.hirun.modules.bss.config.entity.po.PayItemCfg;
import com.microtomato.hirun.modules.bss.config.mapper.CollectionItemCfgMapper;
import com.microtomato.hirun.modules.bss.config.service.ICollectionItemCfgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.config.service.IPayItemCfgService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 非主营收款项配置表 服务实现类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-09
 */
@Slf4j
@Service
public class CollectionItemCfgServiceImpl extends ServiceImpl<CollectionItemCfgMapper, CollectionItemCfg> implements ICollectionItemCfgService {


    /**
     * 查询所有付款项配置
     * @return
     */
    @Override
    @Cacheable(value = "collectionItemcfg-all")
    public List<CollectionItemCfg> queryAll() {
        List<CollectionItemCfg> configs = this.list(new QueryWrapper<CollectionItemCfg>().lambda().eq(CollectionItemCfg::getStatus, "U"));
        return configs;
    }


    @Cacheable(value = "collectionItemcfg-plus-all")
    @Override
    public List<CollectionItemCfg> queryPlusCollectionyItems() {
        ICollectionItemCfgService collectionItemCfgService = SpringContextUtils.getBean(CollectionItemCfgServiceImpl.class);
        List<CollectionItemCfg> configs = collectionItemCfgService.queryAll();

        if (ArrayUtils.isEmpty(configs)) {
            return null;
        }

        List<CollectionItemCfg> result = new ArrayList<>();
        for (CollectionItemCfg config : configs) {
            if (config.getDirection().equals(new Integer(0))) {
                result.add(config);
            }
        }
        return result;
    }

    /**
     * 根据费用项ID查询费用项配置数据
     * @param feeItemId
     * @return
     */
    @Override
    @springfox.documentation.annotations.Cacheable(value = "sys_collection_item_cfg-feeItemId")
    public CollectionItemCfg getFeeItem(Long feeItemId) {
        List<CollectionItemCfg> feeItems = this.queryAll();
        if (ArrayUtils.isEmpty(feeItems)) {
            return null;
        }

        for (CollectionItemCfg feeItem : feeItems) {
            if (feeItem.getId().equals(feeItemId)) {
                return feeItem;
            }
        }
        return null;
    }
}
