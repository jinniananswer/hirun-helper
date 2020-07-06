package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.dto.ConstructionDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderConstructionParty;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayNo;

import java.util.List;

/**
 * <p>
 * 施工参与人表 服务类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-04
 */
public interface IOrderConstructionPartyService extends IService<OrderConstructionParty> {

    List<OrderConstructionParty> queryByConstructionId(Long constructionId);

    void updateForRoleId(ConstructionDTO dto);

}
