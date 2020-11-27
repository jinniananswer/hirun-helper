package com.microtomato.hirun.modules.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.finance.entity.po.FinancePayMoney;

import java.util.List;

/**
 * 财务付款方式表(FinancePayMoney)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-11-24 15:57:13
 */
public interface IFinancePayMoneyService extends IService<FinancePayMoney> {

    List<FinancePayMoney> queryByPayNo(Long payNo);
}