package com.microtomato.hirun.modules.organization.service;

import com.microtomato.hirun.modules.organization.entity.dto.EmployeeTransDetailDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeTransDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liuhui
 * @since 2019-10-29
 */
public interface IEmployeeTransDetailService extends IService<EmployeeTransDetail> {
    /**
     *
     * @param id
     * @return
     */
    EmployeeTransDetail queryPendingDetailById(Long id);

    /**
     * 查询有效的调动详情
     * @param type
     * @return
     */
    List<EmployeeTransDetail> queryVaildTransDetail(String type);

    /**
     * 员工借调归还确认
     * @param transDetail
     * @return
     */
    boolean confirmReturnDetail(EmployeeTransDetail transDetail);

    /**
     * 确定员工调动待办
     */
    boolean confirmTransPending(EmployeeTransDetailDTO employeeTransDetailDTO);

}
