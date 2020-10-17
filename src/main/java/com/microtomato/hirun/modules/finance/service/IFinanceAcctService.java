package com.microtomato.hirun.modules.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.finance.entity.po.FinanceAcct;

import java.util.List;

/**
 * (FinanceAcct)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-10-13 16:51:29
 */
public interface IFinanceAcctService extends IService<FinanceAcct> {

    FinanceAcct getByPaymentId(Long paymentId);

    List<FinanceAcct> queryByType(String type);

    List<FinanceAcct> queryAll();

    List<FinanceAcct> queryByLoginEmployeeId();
}