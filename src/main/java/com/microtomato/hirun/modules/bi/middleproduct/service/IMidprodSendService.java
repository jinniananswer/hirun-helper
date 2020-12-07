package com.microtomato.hirun.modules.bi.middleproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bi.middleproduct.entity.dto.PushDataStatisticDTO;
import com.microtomato.hirun.modules.bi.middleproduct.entity.dto.QueryPushDataDetailDTO;
import com.microtomato.hirun.modules.bi.middleproduct.entity.dto.QueryPushDataStatisticDTO;
import com.microtomato.hirun.modules.bi.middleproduct.entity.po.MidprodSend;

import java.util.List;

/**
 * (MidprodSend)表服务接口
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-26 00:48:47
 */
public interface IMidprodSendService extends IService<MidprodSend> {
    /**
     * 查询推送记录
     * @param dto
     * @return
     */
    List<PushDataStatisticDTO> querySendCountData(QueryPushDataStatisticDTO dto);

    /**
     * 查询top业务
     * @param dto
     * @return
     */
    List<PushDataStatisticDTO> queryTopData(QueryPushDataStatisticDTO dto);
}