package com.microtomato.hirun.modules.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.finance.entity.dto.VoucherItemResultDTO;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucherItem;

import java.util.List;

/**
 * 财务领款单明细表(FinanceVoucherItem)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-25 21:25:21
 */
public interface IFinanceVoucherItemService extends IService<FinanceVoucherItem> {

    List<VoucherItemResultDTO> queryByVoucherNo(String voucherNo);

    List<FinanceVoucherItem> queryItemsByVoucherNo(String voucherNo);

    void deleteItems(String voucherNo);
}