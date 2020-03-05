package com.microtomato.hirun.framework.mybatis.sequence.impl;

import com.microtomato.hirun.framework.mybatis.sequence.AbstractSequence;
import com.microtomato.hirun.framework.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @program: hirun-helper
 * @description: 费用编号序列
 * @author: jinnian
 * @create: 2020-03-04 23:07
 **/
public class FeeNoCycleSeq extends AbstractSequence {

    @Override
    public Long nextval() {
        Long value = this.nextval("FEE_NO_DAY_CYCLE");
        String strNextval = StringUtils.leftPad(String.valueOf(value), 6, '0');
        String timestamp = TimeUtils.now("yyyyMMdd");
        return Long.parseLong(timestamp + strNextval);
    }
}
