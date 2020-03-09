package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.modules.bss.order.entity.dto.ConstructionDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderConstruction;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderConstructionParty;
import com.microtomato.hirun.modules.bss.order.mapper.OrderConstructionPartyMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderConstructionPartyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 施工参与人表 服务实现类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-04
 */
@Slf4j
@Service
public class OrderConstructionPartyServiceImpl extends ServiceImpl<OrderConstructionPartyMapper, OrderConstructionParty> implements IOrderConstructionPartyService {


    /**
     * 根据工程ID查询工程人员信息
     * @param constructionId
     * @return
     */
    @Override
    public List<OrderConstructionParty> queryByConstructionId(Long constructionId) {
        return this.list(new QueryWrapper<OrderConstructionParty>().lambda().eq(OrderConstructionParty::getConstructionId, constructionId).gt(OrderConstructionParty::getEndDate, RequestTimeHolder.getRequestTime()));
    }

    @Override
    public void updateForRoleId(ConstructionDTO dto) {
        String personelId ="";
        long roleId =dto.getRoleId();
        this.update(new UpdateWrapper<OrderConstructionParty>().lambda().eq(OrderConstructionParty::getConstructionId,dto.getConstructionId()).eq(OrderConstructionParty::getRoleId, dto.getRoleId()).set(OrderConstructionParty::getPersonnelId,personelId ).set(OrderConstructionParty::getUpdateTime, LocalDateTime.now()).set(OrderConstructionParty::getRemark, dto.getRemark()));
    }

}
