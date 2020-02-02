package com.microtomato.hirun.modules.bss.customer.service.impl;

import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustPreparationDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.CustBase;
import com.microtomato.hirun.modules.bss.customer.entity.po.CustPreparation;
import com.microtomato.hirun.modules.bss.customer.mapper.CustPreparationMapper;
import com.microtomato.hirun.modules.bss.customer.service.ICustBaseService;
import com.microtomato.hirun.modules.bss.customer.service.ICustPreparationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-01
 */
@Slf4j
@DataSource(DataSourceKey.INS)
@Service
public class CustPreparationServiceImpl extends ServiceImpl<CustPreparationMapper, CustPreparation> implements ICustPreparationService {

    @Autowired
    private ICustBaseService baseService;

    @Autowired
    private CustPreparationMapper preparationMapper;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IStaticDataService staticDataService;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addCustomerPreparation(CustPreparationDTO dto) {
        UserContext userContext= WebContextUtils.getUserContext();
        //当客户存在报备信息时，联系文员可以直接做报备信息保存，否则客户存在多次，需判断客户之前的报备信息是否失效，如果失效则客户选择继续报备
        //客户申报状态为有效期内，不允许办理报备
        //客户未做过报备，但是在客户表存在过数据，可以联系有权限的人员进行报备信息录入
        CustBase custBase=new CustBase();
        CustPreparation preparation=new CustPreparation();
        BeanUtils.copyProperties(dto, preparation);
        BeanUtils.copyProperties(dto, custBase);
        //保存customer信息
        custBase.setCustStatus(0);
        baseService.save(custBase);
        //保存报备信息
        preparation.setCustId(custBase.getCustId());
        preparation.setStatus(1);
        preparation.setEnterEmployeeId(userContext.getEmployeeId());
        preparation.setEnterTime(LocalDateTime.now());
        this.preparationMapper.insert(preparation);

    }

    @Override
    public List<CustPreparationDTO> loadPreparationHistory(String mobileNo) {
        List<CustPreparationDTO> list=this.preparationMapper.loadPreparationHistory(mobileNo);
        if(list.size()<=0){
            return null;
        }
        for(CustPreparationDTO dto:list){
            dto.setPrepareEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getPrepareEmployeeId()));
            dto.setEnterEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getEnterEmployeeId()));
            dto.setPreparationStatusName(staticDataService.getCodeName("PREPARATION_STATUS",dto.getStatus()+""));
        }
        return list;
    }
}
