package com.microtomato.hirun.framework.mybatis.sequence.impl;

import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.mybatis.sequence.AbstractSequence;
import com.microtomato.hirun.framework.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @program: hirun-helper
 * @description: 领款单号序列
 * @author: jinnian
 * @create: 2020-11-15 00:48
 **/
@Component
public class VoucherNoCycleSeq extends AbstractSequence {

    @Override
    @DataSource(DataSourceKey.SYS)
    public Long nextval() {
        Long value = this.nextval("VOUCHER_NO_DAY_CYCLE");
        String strNextval = StringUtils.leftPad(String.valueOf(value), 6, '0');
        String timestamp = TimeUtils.now("yyyyMMdd");
        return Long.parseLong(timestamp + strNextval);
    }
}
