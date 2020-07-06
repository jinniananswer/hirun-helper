package com.microtomato.hirun.modules.bss.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustVisitInfoDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.PartyVisit;
import com.microtomato.hirun.modules.bss.customer.mapper.PartyVisitMapper;
import com.microtomato.hirun.modules.bss.customer.service.IPartyVisitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-04
 */
@Slf4j
@Service
public class PartyVisitServiceImpl extends ServiceImpl<PartyVisitMapper, PartyVisit> implements IPartyVisitService {
    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IStaticDataService staticDataService;

    @Override
    public List<CustVisitInfoDTO> queryCustVisit(Long custId) {
        List<PartyVisit> list=this.list(new QueryWrapper<PartyVisit>().lambda().eq(PartyVisit::getCustId,custId));
        if(ArrayUtils.isEmpty(list)){
            return null;
        }
        List<CustVisitInfoDTO> dtoList=new ArrayList<>();
        for(PartyVisit partyVisit:list){
            CustVisitInfoDTO custVisitInfoDTO=new CustVisitInfoDTO();
            BeanUtils.copyProperties(partyVisit,custVisitInfoDTO);
            custVisitInfoDTO.setVisitEmployeeName(employeeService.getEmployeeNameEmployeeId(partyVisit.getVisitEmployeeId()));
            custVisitInfoDTO.setVisitTypeName(staticDataService.getCodeName("VISIT_TYPE",partyVisit.getVisitType()));
            dtoList.add(custVisitInfoDTO);
        }
        return dtoList;
    }

    @Override
    public void addCustomerVisit(PartyVisit partyVisit) {
        UserContext userContext= WebContextUtils.getUserContext();
        //考虑有可能先做回访，后记录的情况，回访时间设置前台可选
        if(partyVisit.getVisitTime()==null){
            partyVisit.setVisitTime(LocalDateTime.now());
        }
        partyVisit.setVisitEmployeeId(userContext.getEmployeeId());
        this.baseMapper.insert(partyVisit);
    }
}
