package com.microtomato.hirun.modules.bss.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.FeeItemCfg;
import com.microtomato.hirun.modules.bss.config.mapper.FeeItemCfgMapper;
import com.microtomato.hirun.modules.bss.config.service.IFeeItemCfgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import springfox.documentation.annotations.Cacheable;

import java.util.ArrayList;
import java.util.List;

/**
 * 费用项配置表(FeeItemCfg)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-04 22:22:39
 */
@Service
@Slf4j
public class FeeItemCfgServiceImpl extends ServiceImpl<FeeItemCfgMapper, FeeItemCfg> implements IFeeItemCfgService {

    @Autowired
    private FeeItemCfgMapper feeItemCfgMapper;

    /**
     * 查询所有的费用项数据
     * @return
     */
    @Override
    @Cacheable(value = "sys_fee_item_cfg-all")
    public List<FeeItemCfg> queryAll() {
        List<FeeItemCfg> datas = this.list(new QueryWrapper<FeeItemCfg>().lambda().eq(FeeItemCfg::getStatus, "U"));
        return datas;
    }

    /**
     * 根据费用项ID查询费用项配置数据
     * @param feeItemId
     * @return
     */
    @Override
    @Cacheable(value = "sys_fee_item_cfg-feeItemId")
    public FeeItemCfg getFeeItem(Long feeItemId) {
        List<FeeItemCfg> feeItems = this.queryAll();
        if (ArrayUtils.isEmpty(feeItems)) {
            return null;
        }

        for (FeeItemCfg feeItem : feeItems) {
            if (feeItem.getId().equals(feeItemId)) {
                return feeItem;
            }
        }
        return null;
    }

    @Override
    @Cacheable(value = "sys_fee_item_cfg_leaf-type")
    public List<FeeItemCfg> queryLeafByType(String type) {
        List<FeeItemCfg> all = this.queryAll();

        if (ArrayUtils.isEmpty(all)) {
            return null;
        }

        List<FeeItemCfg> result = new ArrayList<>();
        for (FeeItemCfg feeItemCfg : all) {
            if (StringUtils.equals(type, feeItemCfg.getType())) {
                boolean isFindSon = false;

                for (FeeItemCfg son : all) {
                    if (son.getParentFeeItemId().equals(feeItemCfg.getId())) {
                        isFindSon = true;
                    }
                }

                if (!isFindSon) {
                    result.add(feeItemCfg);
                }
            }
        }

        return result;
    }

}