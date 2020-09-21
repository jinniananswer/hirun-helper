package com.microtomato.hirun.modules.bss.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustQueryCondDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustomerInfoDetailDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.QueryCustCondDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.Customer;
import com.microtomato.hirun.modules.bss.customer.mapper.CustomerMapper;
import com.microtomato.hirun.modules.bss.customer.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.house.service.IHousesService;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.SimpleEmployeeDTO;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
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
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IHousesService housesService;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IOrgService orgService;


    @Override
    public Customer queryCustomerInfo(Long customerId) {
        if(customerId==null){
            return null;
        }
        return this.baseMapper.selectOne(new QueryWrapper<Customer>().lambda().eq(Customer::getCustId,customerId)
                .eq(Customer::getCustStatus,'1'));
    }

    @Override
    public IPage<CustomerInfoDetailDTO> queryCustomerInfoDetail(QueryCustCondDTO condDTO) {
        QueryWrapper<CustQueryCondDTO> queryWrapper = new QueryWrapper<>();
        Page<CustQueryCondDTO> page = new Page<>(condDTO.getPage(), condDTO.getSize());
        String orgLine="";
        List<SimpleEmployeeDTO> employees=null;
        List<Long> emplyeeIds=new ArrayList<>();

        if(condDTO.getOrgId()==null){
           orgLine= orgService.listOrgSecurityLine();
            employees=employeeService.queryEmployeeByOrgLine(orgLine);
        }else{
            employees=employeeService.queryEmployeeByOrgId(condDTO.getOrgId());
        }

        if(ArrayUtils.isNotEmpty(employees)){
            for(SimpleEmployeeDTO employeeDTO:employees){
                emplyeeIds.add(employeeDTO.getEmployeeId());
            }
        }

        queryWrapper.like(StringUtils.isNotEmpty(condDTO.getCustName()), "a.cust_name", condDTO.getCustName());
        queryWrapper.like(StringUtils.isNotEmpty(condDTO.getWxNick()), "a.wx_nick", condDTO.getWxNick());
        queryWrapper.eq(condDTO.getHouseId()!=null, "a.house_id",condDTO.getHouseId());
        queryWrapper.in(ArrayUtils.isNotEmpty(emplyeeIds), "a.house_counselor_id", emplyeeIds);
        queryWrapper.eq(condDTO.getCounselorEmployeeId()!=null, "a.house_counselor_id", condDTO.getCounselorEmployeeId());
        queryWrapper.ge(condDTO.getStartTime()!=null, "a.create_time", condDTO.getStartTime());
        queryWrapper.le(condDTO.getEndTime()!=null, "a.create_time", condDTO.getEndTime());

        queryWrapper.eq("a.cust_status","1");
        queryWrapper.orderByDesc("a.create_time ");
        queryWrapper.notExists(" select * from ins_party b where a.identify_code=b.open_id ");

        IPage<CustomerInfoDetailDTO> iPage=this.baseMapper.queryCustomerDetailInfo(page,queryWrapper);
        if (iPage.getRecords().size() <= 0) {
            return iPage;
        }

        List<CustomerInfoDetailDTO> list=iPage.getRecords();
        //模糊化电话号码和姓名
        UserContext userContext = WebContextUtils.getUserContext();
        Long employeeId = userContext.getEmployeeId();

        for(CustomerInfoDetailDTO dto:list){
            dto.setHouseCounselorName(employeeService.getEmployeeNameEmployeeId(dto.getHouseCounselorId()));
            dto.setHouseAddress(housesService.queryHouseName(dto.getHouseId())+"::"+dto.getHouseDetail());
            dto.setHouseModeName(staticDataService.getCodeName("HOUSE_MODE",dto.getHouseMode()));
            if(!employeeId.equals(dto.getHouseCounselorId())){
                dto.setCustomerName(this.nameDesensitization(dto.getCustomerName()));
                dto.setMobileNo("***********");
            }
        }
        return iPage;
    }


    /**
     * 模糊化客户姓名
     *
     * @param name
     * @return
     */
    private String nameDesensitization(String name) {
        String newName = "";
        String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
        name=name.replaceAll(regEx,"");
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
