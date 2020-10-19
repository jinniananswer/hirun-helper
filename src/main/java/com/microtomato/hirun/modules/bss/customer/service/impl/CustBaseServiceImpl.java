package com.microtomato.hirun.modules.bss.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustInfoDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustQueryCondDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.CustBase;
import com.microtomato.hirun.modules.bss.customer.mapper.CustBaseMapper;
import com.microtomato.hirun.modules.bss.customer.service.ICustBaseService;
import com.microtomato.hirun.modules.bss.customer.service.ICustPreparationService;
import com.microtomato.hirun.modules.bss.house.service.IHousesService;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.microtomato.hirun.modules.bss.order.service.IOrderBaseService;
import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 客户信息 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-01
 */
@Slf4j
@Service
public class CustBaseServiceImpl extends ServiceImpl<CustBaseMapper, CustBase> implements ICustBaseService {

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IOrderBaseService orderBaseService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IHousesService housesService;

    @Autowired
    private ICustPreparationService preparationService;

    @Autowired
    private IOrderWorkerService workerService;

    @Override
    public CustBase queryByCustId(Long custId) {
        return this.getById(custId);
    }

    /**
     * 根据客户ID或者订单ID查询客户信息，如果客户ID不为空，永远以客户ID为准进行查询
     *
     * @param custId
     * @param orderId
     * @return
     */
    @Override
    public CustInfoDTO queryByCustIdOrOrderId(Long custId, Long orderId) {
        CustInfoDTO custInfoDTO = new CustInfoDTO();

        if (custId == null && orderId != null) {
            OrderBase orderBase = this.orderBaseService.queryByOrderId(orderId);
            if (orderBase != null) {
                custId = orderBase.getCustId();
            }
        }

        if (custId != null) {
            CustBase custBase = this.queryByCustId(custId);
            if (custBase != null) {
                BeanUtils.copyProperties(custBase, custInfoDTO);

                if (StringUtils.isNotBlank(custInfoDTO.getSex())) {
                    custInfoDTO.setSexName(this.staticDataService.getCodeName("SEX", custInfoDTO.getSex()));
                }
                if(StringUtils.isNotBlank(custInfoDTO.getCustType())){
                    custInfoDTO.setCustTypeName(this.staticDataService.getCodeName("CUSTOMER_TYPE",custBase.getCustType()));
                }
            }
        }

        return custInfoDTO;
    }

    @Override
    public IPage<CustInfoDTO> queryCustomerInfo(CustQueryCondDTO condDTO) {
        QueryWrapper<CustQueryCondDTO> queryWrapper = new QueryWrapper<>();
        Page<CustQueryCondDTO> page = new Page<>(condDTO.getPage(), condDTO.getSize());

        queryWrapper.like(StringUtils.isNotEmpty(condDTO.getCustName()), "a.cust_name", condDTO.getCustName());
        queryWrapper.eq(condDTO.getHouseId() != null, "c.house_id", condDTO.getHouseId());
        queryWrapper.eq(StringUtils.isNotEmpty(condDTO.getHouseMode()), "c.house_mode", condDTO.getHouseMode());
        queryWrapper.eq(condDTO.getReportEmployeeId() != null, "b.prepare_employee_id", condDTO.getReportEmployeeId());
        queryWrapper.eq(StringUtils.isNotEmpty(condDTO.getInformationSource()), "c.information_source", condDTO.getInformationSource());
        queryWrapper.eq(StringUtils.isNotEmpty(condDTO.getOrderStatus()), "d.status", condDTO.getOrderStatus());
        queryWrapper.eq(StringUtils.isNotEmpty(condDTO.getCustomerType()), "a.cust_type", condDTO.getCustomerType());
        queryWrapper.eq(condDTO.getDesignEmployeeId()!=null,"f.design_employee_id",condDTO.getDesignEmployeeId());
        queryWrapper.eq(condDTO.getAgentEmployeeId()!=null,"f.cust_service_employee_id",condDTO.getAgentEmployeeId());

        queryWrapper.apply(" a.cust_id=c.party_id");
        queryWrapper.apply(" a.cust_id=d.cust_id");
        queryWrapper.orderByDesc("a.create_time");

        IPage<CustInfoDTO> iPage = this.baseMapper.queryCustomerInfo(page, queryWrapper);
        if (iPage.getRecords().size() <= 0) {
            return iPage;
        }
        List<CustInfoDTO> custInfoDTOList = iPage.getRecords();

        UserContext userContext = WebContextUtils.getUserContext();
        Long employeeId = userContext.getEmployeeId();

        for (CustInfoDTO dto : custInfoDTOList) {
            if (StringUtils.equals(dto.getCustProperty(), "6")) {
                dto.setCustPropertyName("主管补备");
            } else {
                dto.setCustPropertyName(staticDataService.getCodeName("CUSTOMER_PROPERTY", dto.getCustProperty()));
            }
            dto.setPrepareEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getPrepareEmployeeId()));
            dto.setHouseModeName(staticDataService.getCodeName("HOUSE_MODE", dto.getHouseMode()));

            String houseAddress="";
            if(dto.getHouseId()!=null){
                houseAddress=housesService.queryHouseName(dto.getHouseId());
            }
            if(StringUtils.isNotEmpty(dto.getHouseBuilding())){
                houseAddress=houseAddress+"|"+dto.getHouseBuilding();
            }
            if(StringUtils.isNotEmpty(dto.getHouseRoomNo())){
                houseAddress=houseAddress+"|"+dto.getHouseRoomNo();
            }
            dto.setHouseAddress(houseAddress);
            dto.setRulingEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getRulingEmployeeId()));
            dto.setPrepareStatusName(staticDataService.getCodeName("PREPARATION_STATUS", dto.getPrepareStatus() + ""));
            dto.setCustTypeName(staticDataService.getCodeName("CUSTOMER_TYPE",dto.getCustType()));
            dto.setPrepareStatusName(staticDataService.getCodeName("PREPARATION_STATUS",dto.getPrepareStatus()+""));
            dto.setInformationSource(staticDataService.getCodeName("INFORMATIONSOURCE",dto.getInformationSource()+""));
            dto.setPloyType(staticDataService.getCodeName("MARKETING_TYPE",dto.getPloyType()+""));
            dto.setOrderStatusName(staticDataService.getCodeName("ORDER_STATUS",dto.getOrderStatus()));
            dto.setCustomerServiceName(employeeService.getEmployeeNameEmployeeId(dto.getCustServiceEmployeeId()));
            dto.setDesignEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getDesignEmployeeId()));
            if(!workerService.checkIncludeEmployeeId(dto.getOrderId(),employeeId)){
                dto.setCustName(this.nameDesensitization(dto.getCustName()));
                dto.setMobileNo("***********");
            }
        }
        return iPage;
    }

    @Override
    public List<CustInfoDTO> queryCustomerInfoByMobile(String mobileNo) {
        preparationService.checkCustomerRules(mobileNo);
        List<CustInfoDTO> custList = this.baseMapper.queryCustomerInfoByMobile(mobileNo);
        if (custList.size() <= 0) {
            return new ArrayList<>();
        }
        for (CustInfoDTO dto : custList) {
            dto.setHouseAddress(housesService.queryHouseName(dto.getHouseId()) + "|" + dto.getHouseBuilding() + "|" + dto.getHouseRoomNo());
            dto.setHouseModeName(staticDataService.getCodeName("HOUSE_MODE", dto.getHouseMode()));
            dto.setPrepareEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getPrepareEmployeeId()));
            dto.setEnterEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getEnterEmployeeId()));
            this.checkAllowSelect(dto);
            dto.setStatus(staticDataService.getCodeName("PREPARATION_STATUS", dto.getStatus()));
        }
        return custList;
    }

    @Override
    public List<CustInfoDTO> queryCustomer4TransOrder() {
        UserContext userContext = WebContextUtils.getUserContext();
        List<CustInfoDTO> list = this.baseMapper.queryCustomer4TransOrder(userContext.getEmployeeId());
        return list;
    }

    /**
     * 判断是否在前台选择做老客户的选择
     *
     * @param dto
     * @return
     */
    private CustInfoDTO checkAllowSelect(CustInfoDTO dto) {
        /**
         * 客户报备已失效，允许普通报备员工选择报备客户进行再次报备。【权限操作】文员允许新增客户报备
         * 先做客户信息新增，然后再做报备，允许普通报备员工选择客户进行报备。但是报备状态不更改
         * 客户报备已失败，允许普通报备员工选择报备客户进行再次报备。【权限操作】文员允许新增客户报备
         */

        if (dto.getPrepareId() == null) {
            dto.setAllowSelect(true);
        } else if (StringUtils.equals(dto.getStatus(), "1")
                && (Duration.between(dto.getPreparationExpireTime(), LocalDateTime.now()).toMillis()) > 0) {
            dto.setAllowSelect(true);
        } else {
            dto.setAllowSelect(false);
        }

        return dto;
    }


    /**
     * 模糊化客户姓名
     *
     * @param name
     * @return
     */
    private String nameDesensitization(String name) {
        String newName = "";
        String regEx = "[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
        name = name.replaceAll(regEx, "");
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
