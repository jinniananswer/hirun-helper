package com.microtomato.hirun.modules.bss.customer.service;

import com.microtomato.hirun.modules.bss.customer.entity.dto.CustPreparationDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.CustPreparation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-01
 */
public interface ICustPreparationService extends IService<CustPreparation> {
    /**
     * 新增客户报备信息
     * @param dto
     */
    void addCustomerPreparation(CustPreparationDTO dto);

    /**
     *根据号码查询历史报备记录
     * @param mobileNo
     * @return
     */
    List<CustPreparationDTO> loadPreparationHistory(String mobileNo);

    /**
     * 报备裁定
     * @param custPreparation
     */
    void customerRuling(CustPreparationDTO custPreparation);

    /**
     * 查询报表记录
     * @param mobileNo
     * @param custId
     * @param status
     * @return
     */
    List<CustPreparationDTO> queryCustPreparaton(String mobileNo,Long custId,String status,Long houseId,String isExpire);
}
