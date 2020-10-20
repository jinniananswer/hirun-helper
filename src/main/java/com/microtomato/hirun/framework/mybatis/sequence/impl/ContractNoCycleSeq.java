package com.microtomato.hirun.framework.mybatis.sequence.impl;

import com.microtomato.hirun.framework.mybatis.sequence.AbstractSequence;
import org.springframework.stereotype.Component;

/**
 * @program: hirun-helper
 * @description: 合同编号序列
 * @author: jinnian
 * @create: 2020-10-20 15:14
 **/
@Component
public class ContractNoCycleSeq extends AbstractSequence {

    @Override
    public Long nextval() {
        Long value = this.nextval("CONTRACT_NO_YEAR_CYCLE");
        return value;
    }
}
