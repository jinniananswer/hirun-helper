package com.microtomato.hirun.modules.finance.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.finance.entity.dto.VoucherItemResultDTO;
import com.microtomato.hirun.modules.finance.entity.po.FinanceItem;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucher;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucherItem;
import com.microtomato.hirun.modules.finance.mapper.FinanceVoucherItemMapper;
import com.microtomato.hirun.modules.finance.service.IFinanceItemService;
import com.microtomato.hirun.modules.finance.service.IFinanceVoucherItemService;
import com.microtomato.hirun.modules.finance.service.IFinanceVoucherService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 财务领款单明细表(FinanceVoucherItem)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-25 21:25:21
 */
@Service
@Slf4j
@DataSource(DataSourceKey.INS)
public class FinanceVoucherItemServiceImpl extends ServiceImpl<FinanceVoucherItemMapper, FinanceVoucherItem> implements IFinanceVoucherItemService {

    @Autowired
    private FinanceVoucherItemMapper financeVoucherItemMapper;

    @Autowired
    private IFinanceItemService financeItemService;

    @Autowired
    private IFinanceVoucherService financeVoucherService;

    @Autowired
    private IStaticDataService staticDataService;

    @Override
    public List<VoucherItemResultDTO> queryByVoucherNo(String voucherNo) {

        FinanceVoucher voucher = this.financeVoucherService.getByVoucherNo(voucherNo);
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        List<FinanceVoucherItem> items = this.list(Wrappers.<FinanceVoucherItem>lambdaQuery()
                .eq(FinanceVoucherItem::getVoucherNo, voucherNo)
                .ge(FinanceVoucherItem::getEndDate, now)
                .le(FinanceVoucherItem::getStartDate, now));

        if (ArrayUtils.isEmpty(items)) {
            return null;
        }

        List<VoucherItemResultDTO> results = new ArrayList<>();
        for (FinanceVoucherItem item : items) {
            VoucherItemResultDTO result = new VoucherItemResultDTO();
            BeanUtils.copyProperties(item, result);
            String financeItemId = item.getFinanceItemId();
            FinanceItem financeItem = this.financeItemService.getByFinanceItemId(financeItemId);
            result.setFinanceItemName(financeItem.getName());

            Long fee = item.getFee();
            if (fee != null) {
                Double money = fee / 100d;
                result.setFee(money);
            }

            if (voucher != null && StringUtils.isNotBlank(voucher.getItem())) {
                result.setItem(voucher.getItem());
                result.setItemName(this.staticDataService.getCodeName("VOUCHER_ITEM", voucher.getItem()));
            }

            if (voucher != null && StringUtils.isNotBlank(voucher.getType())) {
                result.setType(voucher.getType());
                result.setTypeName(this.staticDataService.getCodeName("VOUCHER_TYPE", voucher.getType()));
            }

            if (voucher != null && StringUtils.equals("5", voucher.getItem())) {
                Long trafficFee = item.getTrafficFee();
                Long allowance = item.getAllowance();
                Long hotelFee = item.getHotelFee();

                if (trafficFee != null) {
                    result.setTrafficFee(trafficFee / 100d);
                }

                if (allowance != null) {
                    result.setAllowance(allowance / 100d);
                }

                if (hotelFee != null) {
                    result.setHotelFee(hotelFee / 100d);
                }
            }

            result.setProjectName(item.getProjectName());
            results.add(result);
        }

        return results;
    }

    @Override
    public List<FinanceVoucherItem> queryItemsByVoucherNo(String voucherNo) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        List<FinanceVoucherItem> items = this.list(Wrappers.<FinanceVoucherItem>lambdaQuery()
                .eq(FinanceVoucherItem::getVoucherNo, voucherNo)
                .ge(FinanceVoucherItem::getEndDate, now)
                .le(FinanceVoucherItem::getStartDate, now));

        return items;
    }

    @Override
    public void deleteItems(String voucherNo) {
        List<FinanceVoucherItem> items = this.queryItemsByVoucherNo(voucherNo);

        if (ArrayUtils.isEmpty(items)) {
            return;
        }
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        for (FinanceVoucherItem item : items) {
            item.setEndDate(now);
        }

        this.updateBatchById(items);
    }

}