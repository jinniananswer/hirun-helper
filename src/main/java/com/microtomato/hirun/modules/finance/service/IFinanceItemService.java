package com.microtomato.hirun.modules.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.dto.CascadeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.CollectionComponentDTO;
import com.microtomato.hirun.modules.finance.entity.po.FinanceItem;

import java.util.List;

/**
 * 财务科目表(FinanceItem)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-19 22:26:54
 */
public interface IFinanceItemService extends IService<FinanceItem> {

    List<CascadeDTO<FinanceItem>>  loadFinancenItem();

}