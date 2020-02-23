package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.system.entity.po.FeeItemCfg;
import com.microtomato.hirun.modules.system.mapper.FeeItemCfgMapper;
import com.microtomato.hirun.modules.system.service.IFeeItemCfgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 费用项配置表 服务实现类
 * </p>
 *
 * @author sunxin
 * @since 2020-02-06
 */
@Slf4j
@Service
public class FeeItemCfgServiceImpl extends ServiceImpl<FeeItemCfgMapper, FeeItemCfg> implements IFeeItemCfgService {

    @Autowired
    private FeeItemCfgMapper FeeItemCfgMapper;

    /**
     * 根据 费用大类 获取费用小类或者品牌配置数据
     *
     * @param codeType
     * @return
     */
    @Override
    public List<FeeItemCfg> getFeeItemDatas(String codeType) {
         IFeeItemCfgService feeItemCfgDataService = SpringContextUtils.getBean(FeeItemCfgServiceImpl.class);
        List<FeeItemCfg> datas = feeItemCfgDataService.getAllFeeItemDatas();
        if (ArrayUtils.isEmpty(datas)) {
            return null;
        }

        List<FeeItemCfg> result = new ArrayList<>();
        for (FeeItemCfg data : datas) {
            if (StringUtils.equals(codeType, data.getParentFeeItemId().toString())) {
                result.add(data);
            }
        }
        return result;
    }


    /**
     * 查询所有的静态参数数据
     *
     * @return
     */
    @Override
    public List<FeeItemCfg> getAllFeeItemDatas() {
        List<FeeItemCfg> datas = this.list(
            Wrappers.<FeeItemCfg>lambdaQuery().eq(FeeItemCfg::getStatus, "U")
        );
        return datas;
    }

    @Override
    public String getFeeItemNameFeeItemId(Long feeItemId) {
        FeeItemCfg feeItem = this.FeeItemCfgMapper.selectById(feeItemId);
        if (feeItem == null) {
            return null;
        }
        String name = feeItem.getName();
        return name;
    }

}
