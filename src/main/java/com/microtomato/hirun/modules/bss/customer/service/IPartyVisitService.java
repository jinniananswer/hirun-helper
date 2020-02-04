package com.microtomato.hirun.modules.bss.customer.service;

import com.microtomato.hirun.modules.bss.customer.entity.dto.CustVisitInfoDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.PartyVisit;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-04
 */
public interface IPartyVisitService extends IService<PartyVisit> {
    /**
     * 查询回访记录
     * @param custId
     * @return
     */
    List<CustVisitInfoDTO> queryCustVisit(Long custId);
}
