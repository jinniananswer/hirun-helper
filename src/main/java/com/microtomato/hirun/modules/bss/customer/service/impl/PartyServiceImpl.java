package com.microtomato.hirun.modules.bss.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustQueryCondDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustomerInfoDetailDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.QueryCustCondDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.Party;
import com.microtomato.hirun.modules.bss.customer.mapper.PartyMapper;
import com.microtomato.hirun.modules.bss.customer.service.IPartyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.house.service.IHousesService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 客户信息 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-04-20
 */
@Slf4j
@Service
public class PartyServiceImpl extends ServiceImpl<PartyMapper, Party> implements IPartyService {

    @Autowired
    private IHousesService housesService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IStaticDataService staticDataService;

    @Override
    public IPage<CustomerInfoDetailDTO> queryCustomerInfo(QueryCustCondDTO condDTO) {
        QueryWrapper<CustQueryCondDTO> queryWrapper = new QueryWrapper<>();
        Page<CustQueryCondDTO> page = new Page<>(condDTO.getPage(), condDTO.getSize());

        queryWrapper.like(StringUtils.isNotEmpty(condDTO.getCustName()), "a.party_name", condDTO.getCustName());
        queryWrapper.eq(condDTO.getCustomerServiceEmployeeId() != null, "c.link_employee_id", condDTO.getCustomerServiceEmployeeId());
        queryWrapper.like(StringUtils.isNotEmpty(condDTO.getWxNick()), "a.wx_nick", condDTO.getWxNick());
        queryWrapper.eq(condDTO.getCounselorEmployeeId()!=null, "d.house_counselor_id", condDTO.getCounselorEmployeeId());
        queryWrapper.like(condDTO.getHouseId()!=null, "b.house_id", condDTO.getHouseId());



        queryWrapper.ge(condDTO.getStartTime()!=null, "a.consult_time", condDTO.getStartTime());

        LocalDateTime endTime=null;
        if(condDTO.getEndTime()!=null){
            LocalDate endDate=condDTO.getEndTime();
            ZonedDateTime zonedDateTime = endDate.atStartOfDay(ZoneId.systemDefault());
            endTime=TimeUtils.reserveDateCustomTime(Date.from(zonedDateTime.toInstant()),"23:59:59");
        }
        queryWrapper.le(endTime!=null, "a.consult_time", endTime);


        queryWrapper.apply(" a.party_id=b.party_id");
        queryWrapper.apply(" c.project_id=b.project_id");
        queryWrapper.apply(" a.party_status='0' ");
        queryWrapper.apply(" c.role_type='CUSTOMERSERVICE'");
        queryWrapper.orderByDesc("a.create_time ");

        IPage<CustomerInfoDetailDTO> iPage = this.baseMapper.queryCustomerInfo(page, queryWrapper);
        if (iPage.getRecords().size() <= 0) {
            return iPage;
        }

        List<CustomerInfoDetailDTO> list=iPage.getRecords();
        UserContext userContext = WebContextUtils.getUserContext();
        Long employeeId = userContext.getEmployeeId();

        for(CustomerInfoDetailDTO dto:list){
            if(dto.getHouseCounselorId()!=null){
                dto.setHouseCounselorName(employeeService.getEmployeeNameEmployeeId(dto.getHouseCounselorId()));
            }
            dto.setCustomerServiceName(employeeService.getEmployeeNameEmployeeId(dto.getLinkEmployeeId()));
            if(StringUtils.isNotBlank(dto.getPartyName())){
                dto.setCustomerName(dto.getPartyName());
            }
            if(StringUtils.isBlank(dto.getHouseAddress())||StringUtils.equals(dto.getHouseAddress(),"null")){
                dto.setHouseAddress(housesService.queryHouseName(dto.getHouseId())+":"+dto.getHouseBuilding()+":"+dto.getHouseRoomNo());
            }
            if(StringUtils.isBlank(dto.getMobileNo())){
                dto.setMobileNo(dto.getCounselorMobileNo());
            }
            if(StringUtils.isNotBlank(dto.getHouseMode())){
                dto.setHouseModeName(staticDataService.getCodeName("HOUSE_MODE",dto.getHouseMode()));
            }
            if(!employeeId.equals(dto.getHouseCounselorId())&&!employeeId.equals(dto.getLinkEmployeeId())){
                dto.setCustomerName(this.nameDesensitization(dto.getCustomerName()));
                dto.setMobileNo("***********");
            }
        }

        return iPage;
    }

    @Override
    public CustomerInfoDetailDTO queryPartyInfoDetail(Long partyId) {
        if (partyId == null) {
            return null;
        }
        return this.baseMapper.queryPartyInfoDetail(partyId);
    }

    /**
     * 模糊化客户姓名
     *
     * @param name
     * @return
     */
    private String nameDesensitization(String name) {
        String newName = "";
        if (StringUtils.isBlank(name)) {
            return "";
        }
        char[] chars = name.toCharArray();
        if (chars.length == 1) {
            newName = name;
        } else if (chars.length == 2) {
            newName = name.replaceFirst(name.substring(1), "*");
        } else {
            newName = name.replaceAll(name.substring(1, chars.length - 1), "*");
        }
        return newName;
    }
}
