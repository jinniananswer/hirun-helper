package com.microtomato.hirun.framework.mybatis.sequence.impl;

import com.microtomato.hirun.framework.mybatis.sequence.AbstractSequence;
import com.microtomato.hirun.framework.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author Steven
 * @date 2020-02-19
 */
@Component
public class RepairNoCycleSeq extends AbstractSequence {

    @Override
    public Long nextval() {
        Long nextval = nextval("REPAIR_NO_DAY_CYCLE");
        String strNextval = StringUtils.leftPad(String.valueOf(nextval), 4, '0');
        String timestamp = TimeUtils.now("yyyyMMdd");
        return Long.parseLong(timestamp + strNextval);
    }

}
