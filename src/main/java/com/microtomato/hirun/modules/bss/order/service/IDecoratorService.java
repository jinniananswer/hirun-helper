package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.DecoratorInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.Decorator;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 装修工人表 服务类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-04
 */
public interface IDecoratorService extends IService<Decorator> {

    /**
     * 根据类型获取施工人员信息
     * @param orgId
     * @return
     */
    List<Decorator> queryDecoratorInfo(Long orgId, Long type);

    IPage<Decorator> queryDecoratorInfo(String name, String identityNo);

    /**
     * 查询所有
     * @param
     * @return
     */
    List<Decorator> queryAllInfo();

    /**
     * 根据工人ID查询工人信息
     * @param decoratorId
     * @return
     */
    Decorator getDecorator(Long decoratorId);

    /**
     * 根据工人姓名、证件号码查询工人信息
     * @param name
     * @param identityNo
     * @return
     */
    Decorator getDecorator(String name, String identityNo);
}
