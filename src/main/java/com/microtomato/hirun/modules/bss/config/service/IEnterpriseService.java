package com.microtomato.hirun.modules.bss.config.service;

import com.microtomato.hirun.modules.bss.config.entity.po.Enterprise;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-10
 */
public interface IEnterpriseService extends IService<Enterprise> {

   List<Enterprise> queryAll() ;

}
