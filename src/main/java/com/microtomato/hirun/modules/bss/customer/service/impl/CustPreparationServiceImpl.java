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


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addCustomerPreparation(CustPreparationDTO dto) {
        UserContext userContext= WebContextUtils.getUserContext();

        CustBase custBase=new CustBase();
        CustPreparation preparation=new CustPreparation();
        BeanUtils.copyProperties(dto, preparation);
        BeanUtils.copyProperties(dto, custBase);
        //保存customer信息
        custBase.setCustStatus(0);
        baseService.save(custBase);
        //保存报备信息
        preparation.setCustId(custBase.getCustId());
        preparation.setStatus(0);
        preparation.setEnterEmployeeId(userContext.getEmployeeId());
        preparation.setEnterTime(LocalDateTime.now());
        this.preparationMapper.insert(preparation);

    }

    @Override
    public List<CustPreparationDTO> loadPreparationHistory(String mobileNo) {
        return this.preparationMapper.loadPreparationHistory(mobileNo);
    }
}
