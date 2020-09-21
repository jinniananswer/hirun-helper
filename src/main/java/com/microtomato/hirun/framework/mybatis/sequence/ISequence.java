package com.microtomato.hirun.framework.mybatis.sequence;

/**
 * @author Steven
 * @date 2020-02-10
 */
public interface ISequence {

    /**
     * 获取下一个序列
     *
     * @return
     */
    Long nextval();
}
