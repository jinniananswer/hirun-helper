package com.microtomato.hirun.modules.bss.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustInfoDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustPreparationDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustQueryCondDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.CustPreparation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

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

    /**
     * 校验针对公司部门的判断规则
     * @param dto
     */
    void checkPrepareOrgRules(CustPreparationDTO dto);

    /**
     * 校验针对客户的规则
     * @param mobileNo
     */
    void checkCustomerRules(String mobileNo);

    /**
     * 获取客户编码和校验权限
     * @return
     */
    Map<String,String> getCustomerNoAndSec();

    /**
     * 根据custid和状态查询报备记录
     * @param custId
     * @param status
     * @return
     */
    List<CustPreparationDTO> queryPrepareByCustIdAndStatus(Long custId,String status);

    IPage<CustInfoDTO> queryPreparationInfo(CustQueryCondDTO condDTO);


}
