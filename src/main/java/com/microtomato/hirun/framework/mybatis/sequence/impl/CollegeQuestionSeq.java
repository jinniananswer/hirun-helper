package com.microtomato.hirun.framework.mybatis.sequence.impl;

import com.microtomato.hirun.framework.mybatis.sequence.AbstractSequence;
import com.microtomato.hirun.framework.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @program: hirun-helper
 * @description: 知乎问题序列
 * @author: huanghua
 * @create: 2020-08-31 23:07
 **/
@Component
public class CollegeQuestionSeq extends AbstractSequence {

    @Override
    public Long nextval() {
        Long value = this.nextval("COLLEGE_QUESTION_SEQ");
        String strNextval = StringUtils.leftPad(String.valueOf(value), 6, '0');
        String timestamp = TimeUtils.now("yyyyMMdd");
        return Long.parseLong(timestamp + strNextval);
    }
}
