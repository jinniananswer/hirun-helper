package com.microtomato.hirun.modules.demo.service;

import com.microtomato.hirun.modules.demo.entity.po.Zhoulin;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Steven
 * @since 2019-12-19
 */
public interface IZhoulinService extends IService<Zhoulin> {

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRES_NEW)
    void isExistTransactionId();
}
