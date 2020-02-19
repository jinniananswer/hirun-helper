package com.microtomato.hirun.framework.mybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.mapper.DualMapper;
import com.microtomato.hirun.framework.mybatis.po.Dual;
import com.microtomato.hirun.framework.mybatis.sequence.ISequence;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Steven
 * @date 2020-02-07
 */
@Slf4j
@Service
public class DualServiceImpl extends ServiceImpl<DualMapper, Dual> implements IDualService {

    @Autowired
    private DualMapper dualMapper;

    @Override
    public Long nextval(Class<?> cls) {
        ISequence sequence = (ISequence) SpringContextUtils.getBean(cls);
        return sequence.nextval();
    }

    @Override
    public Long nextval(String seqName) {
        Long nextval = dualMapper.nextval(seqName);
        return nextval;
    }

}
