package com.microtomato.hirun.modules.bss.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.modules.bss.config.entity.po.Enterprise;
import com.microtomato.hirun.modules.bss.config.entity.po.FeeItemCfg;
import com.microtomato.hirun.modules.bss.config.mapper.EnterpriseMapper;
import com.microtomato.hirun.modules.bss.config.service.IEnterpriseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.Cacheable;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-10
 */
@Slf4j
@Service
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseMapper, Enterprise> implements IEnterpriseService {

    /**
     * 查询所有公司数据
     * @return
     */
    @Override
    @Cacheable(value = "ins_enterprise")
    public List<Enterprise> queryAll() {
        List<Enterprise> datas = this.list(new QueryWrapper<Enterprise>().lambda().eq(Enterprise::getStatus, "1"));
        return datas;
    }

    /**
     * 根据公司ID查询公司信息
     * @param getEnterpriseId
     * @return
     */
    @Override
    public Enterprise getEnterpriseId(Long getEnterpriseId) {
        Enterprise enterprise = this.getById(getEnterpriseId);
        return enterprise;
    }

}
