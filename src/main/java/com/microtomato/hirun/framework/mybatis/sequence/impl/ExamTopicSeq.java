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
public class ExamTopicSeq extends AbstractSequence {

    @Override
    public Long nextval() {
        return this.nextval("COLLEGE_TOPIC_SEQ");
    }
}
