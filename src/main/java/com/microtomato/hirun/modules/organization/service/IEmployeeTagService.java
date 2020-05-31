package com.microtomato.hirun.modules.organization.service;

import com.microtomato.hirun.modules.organization.entity.po.EmployeeTag;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-05-04
 */
public interface IEmployeeTagService extends IService<EmployeeTag> {
    /**
     *
     * @param tagType
     * @param employeeId
     * @param remark
     */
   void addEmployeeTag(String tagType,Long employeeId,String remark);
}
