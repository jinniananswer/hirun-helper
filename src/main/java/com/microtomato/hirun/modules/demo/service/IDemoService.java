package com.microtomato.hirun.modules.demo.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Steven
 * @date 2019-12-19
 */
public interface IDemoService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    void testSave();

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    void testUpdate();

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    void testSaveOrUpdate();
}
