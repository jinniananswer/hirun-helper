package com.microtomato.hirun.modules.organization.entity.domain;

import com.microtomato.hirun.modules.organization.entity.po.HrPending;
import com.microtomato.hirun.modules.organization.mapper.HrPendingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @program: hirun-helper
 * @description: HR待办领域对象
 * @author: liuhui
 **/
@Slf4j
@Component
@Scope("prototype")
public class HrPendingDO {

    @Autowired
    private HrPendingMapper hrPendingMapper;


    /**
     * 新增hr待办
     */
    public int add(HrPending hrPending) {
      return this.hrPendingMapper.insert(hrPending);
    }

    /**
     * 删除待办
     */
    public boolean delete(HrPending hrPending){
        int result=hrPendingMapper.updateById(hrPending);
        if(result<=0){
            return false;
        }
        return true;
    }

    /**
     * 修改待办
     */
    public boolean update(HrPending hrPending){
        int result=hrPendingMapper.updateById(hrPending);
        if(result<=0){
            return false;
        }
        return true;
    }
}
