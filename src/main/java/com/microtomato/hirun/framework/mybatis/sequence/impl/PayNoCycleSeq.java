package com.microtomato.hirun.framework.mybatis.sequence.impl;

import com.microtomato.hirun.framework.mybatis.sequence.AbstractSequence;
import com.microtomato.hirun.framework.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @program: hirun-helper
 * @description: 付款编码序列
 * @author: jinnian
 * @create: 2020-02-26 09:29
 **/
@Component
public class PayNoCycleSeq extends AbstractSequence {

    @Override
    public Long nextval() {
        Long value = this.nextval("PAY_NO_DAY_CYCLE");
        String strNextval = StringUtils.leftPad(String.valueOf(value), 6, '0');
        String timestamp = TimeUtils.now("yyyyMMdd");
        return Long.parseLong(timestamp + strNextval);
    }
}
