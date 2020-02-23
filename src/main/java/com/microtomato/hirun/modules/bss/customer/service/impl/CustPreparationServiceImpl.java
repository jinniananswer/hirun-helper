package com.microtomato.hirun.modules.bss.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.exception.cases.AlreadyExistException;
import com.microtomato.hirun.framework.exception.cases.NotFoundException;
import com.microtomato.hirun.framework.mybatis.sequence.impl.CustNoMaxCycleSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SecurityUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.PrepareConfig;
import com.microtomato.hirun.modules.bss.config.service.IPrepareConfigService;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustPreparationDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.CustBase;
import com.microtomato.hirun.modules.bss.customer.entity.po.CustPreparation;
import com.microtomato.hirun.modules.bss.customer.entity.po.Project;
import com.microtomato.hirun.modules.bss.customer.entity.po.ProjectIntention;
import com.microtomato.hirun.modules.bss.customer.mapper.CustPreparationMapper;
import com.microtomato.hirun.modules.bss.customer.service.ICustBaseService;
import com.microtomato.hirun.modules.bss.customer.service.ICustPreparationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.customer.service.IProjectIntentionService;
import com.microtomato.hirun.modules.bss.customer.service.IProjectService;
import com.microtomato.hirun.modules.bss.house.entity.po.HousesPlan;
import com.microtomato.hirun.modules.bss.house.service.IHousesPlanService;
import com.microtomato.hirun.modules.bss.house.service.IHousesService;
import com.microtomato.hirun.modules.bss.order.entity.dto.NewOrderDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.microtomato.hirun.modules.bss.order.service.IOrderDomainService;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-01
 */
@Slf4j
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

    @Autowired
    private IHousesService housesService;

    @Autowired
    private IPrepareConfigService configService;

    @Autowired
    private IEmployeeJobRoleService jobRoleService;

    @Autowired
    private IHousesPlanService housesPlanService;

    @Autowired
    private IProjectService projectService;

    @Autowired
    private IProjectIntentionService intentionService;

    @Autowired
    private IOrderDomainService domainService;

    @Autowired
    private IDualService dualService;



    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addCustomerPreparation(CustPreparationDTO dto) {
        UserContext userContext = WebContextUtils.getUserContext();
        //校验报备的规则
        this.checkCustomerRules(dto.getMobileNo());
        this.checkPrepareOrgRules(dto);
        CustPreparation preparation = new CustPreparation();
        BeanUtils.copyProperties(dto, preparation);
        preparation.setStatus(0);
        preparation.setEnterEmployeeId(userContext.getEmployeeId());
        preparation.setEnterTime(LocalDateTime.now());
        preparation.setPreparationExpireTime(TimeUtils.addTime(dto.getPrepareTime(), ChronoUnit.DAYS, 5));
        preparation.setRefereeInfo(dto.getRefereeName()+":"+dto.getRefereeMobileNo()+":"+dto.getRefereeFixPlace());

        if (preparation.getPrepareOrgId() == null) {
            EmployeeJobRole employeeJobRole = jobRoleService.queryValidMain(preparation.getPrepareEmployeeId());
            preparation.setPrepareOrgId(employeeJobRole.getOrgId());
        }

        if (dto.getCustId() != null) {
            preparation.setCustId(dto.getCustId());
            this.preparationMapper.insert(preparation);
            this.preparationMapper.update(null,new UpdateWrapper<CustPreparation>().lambda().eq(CustPreparation::getId,dto.getPrepareId())
                    .set(CustPreparation::getStatus,"2"));
            baseService.update(new UpdateWrapper<CustBase>().lambda().eq(CustBase::getCustId, dto.getCustId()).set(CustBase::getPrepareId, preparation.getId()));
        } else {
            CustBase custBase = new CustBase();
            Project project = new Project();
            ProjectIntention projectIntention = new ProjectIntention();
            BeanUtils.copyProperties(dto, custBase);
            BeanUtils.copyProperties(dto, project);

            //保存customer信息,用作测试将状态设置成0，实际应该将状态设置成报备状态
            custBase.setCustStatus(0);
            baseService.save(custBase);
            //保存project信息
            project.setPartyId(custBase.getCustId());
            projectService.save(project);
            //保存客户意向
            projectIntention.setProjectId(project.getProjectId());
            intentionService.save(projectIntention);
            //保存报备信息

            preparation.setCustId(custBase.getCustId());
            this.preparationMapper.insert(preparation);
            //回填customer表的prepareId
            baseService.update(new UpdateWrapper<CustBase>().lambda().eq(CustBase::getCustId, custBase.getCustId()).set(CustBase::getPrepareId, preparation.getId()));
            //生成订单信息
            NewOrderDTO orderBase = new NewOrderDTO();
            orderBase.setCustId(custBase.getCustId());
            orderBase.setHousesId(preparation.getHouseId());
            orderBase.setHouseLayout(dto.getHouseMode());
            orderBase.setFloorage(dto.getHouseArea());
            orderBase.setType("0");
            orderBase.setStatus("1");
            orderBase.setDecorateAddress(dto.getHouseBuilding() + dto.getHouseRoomNo());
            domainService.createNewOrder(orderBase);
        }
    }

    @Override
    public List<CustPreparationDTO> loadPreparationHistory(String mobileNo) {
        List<CustPreparationDTO> list = this.preparationMapper.loadPreparationHistory(mobileNo);
        if (list.size() <= 0) {
            return null;
        }
        for (CustPreparationDTO dto : list) {
            dto.setPrepareEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getPrepareEmployeeId()));
            dto.setEnterEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getEnterEmployeeId()));
            dto.setHouseAddress(housesService.queryHouseName(dto.getHouseId()));
            dto.setPreparationStatusName(staticDataService.getCodeName("PREPARATION_STATUS", dto.getPrepareStatus() + ""));
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void customerRuling(CustPreparationDTO custPreparation) {
        UserContext userContext = WebContextUtils.getUserContext();
        CustPreparation addPreparation = new CustPreparation();
        CustPreparation updatePreparation = new CustPreparation();
        Long prepareOrgId=null;
        BeanUtils.copyProperties(custPreparation, addPreparation);

        if (custPreparation.getPrepareOrgId() == null) {
            EmployeeJobRole employeeJobRole = jobRoleService.queryValidMain(custPreparation.getPrepareEmployeeId());
            prepareOrgId=employeeJobRole.getOrgId();
        }else {
            prepareOrgId=custPreparation.getPrepareOrgId();
        }

        //如果点的为系统自动补录,设置客户属性为主管报备
        if(custPreparation.getIsAddNewPrepareFlag()){
            addPreparation.setPrepareOrgId(prepareOrgId);
            addPreparation.setCustProperty("6");
            addPreparation.setPrepareTime(LocalDateTime.now());
            addPreparation.setEnterTime(LocalDateTime.now());
            addPreparation.setEnterEmployeeId(userContext.getEmployeeId());
            addPreparation.setRulingTime(LocalDateTime.now());
            addPreparation.setStatus(2);
            this.baseMapper.insert(addPreparation);
            //获取客户原来的prepareId,根据该id将之前的报备信息改成报备失败,然后回填cust表的prepareId为新的报备id
            CustBase custBase=baseService.getById(custPreparation.getCustId());
            if(custBase.getPrepareId()!=null){
                updatePreparation.setStatus(1);
                updatePreparation.setId(custBase.getPrepareId());
                this.baseMapper.updateById(updatePreparation);
            }

            this.baseService.update(new UpdateWrapper<CustBase>().lambda()
                    .eq(CustBase::getCustId,custPreparation.getCustId()).set(CustBase::getPrepareId,addPreparation.getId()));
        }else{
            //如果选择为裁定，则更新选择的报备信息
            BeanUtils.copyProperties(custPreparation, updatePreparation);
            updatePreparation.setPrepareOrgId(prepareOrgId);
            updatePreparation.setStatus(2);
            updatePreparation.setRulingTime(LocalDateTime.now());
            this.baseMapper.updateById(updatePreparation);

            //防止原来的prepareId与选择的id不一致，多做一步更新cust对应的prepareId
            CustBase custBase=baseService.getById(custPreparation.getCustId());
            if(custBase.getPrepareId()!=null){
                CustPreparation updateSource=new CustPreparation();
                updateSource.setStatus(1);
                updateSource.setId(custBase.getPrepareId());
                this.baseMapper.updateById(updateSource);
            }

            this.baseService.update(new UpdateWrapper<CustBase>().lambda()
                    .eq(CustBase::getCustId,custPreparation.getCustId()).set(CustBase::getPrepareId,custPreparation.getId()));
        }



    }

    @Override
    public List<CustPreparationDTO> queryCustPreparaton(String mobileNo, Long custId, String status, Long houseId, String isExpire) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(mobileNo), "a.mobile_no", mobileNo);
        queryWrapper.eq(custId != null, "a.cust_id", custId);
        queryWrapper.eq(StringUtils.isNotEmpty(status), "b.status", status);
        queryWrapper.eq(houseId != null, "b.house_id", houseId);
        //2表示需要判断未过失效期
        queryWrapper.apply(StringUtils.equals(isExpire, "2"), "b.preparation_expire_time > now() ");

        List<CustPreparationDTO> list = this.baseMapper.queryCustomerPreparation(queryWrapper);


        if (ArrayUtils.isEmpty(list)) {
            return null;
        }
        for (CustPreparationDTO dto : list) {
            dto.setPrepareEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getPrepareEmployeeId()));
            dto.setEnterEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getEnterEmployeeId()));
            dto.setCustPropertyName(staticDataService.getCodeName("CUSTOMER_PROPERTY", dto.getCustProperty()));
            dto.setHouseModeName(staticDataService.getCodeName("HOUSE_MODE", dto.getHouseMode()));
            dto.setHouseAddress(housesService.queryHouseName(dto.getHouseId()) + dto.getHouseBuilding() + dto.getHouseRoomNo());
            dto.setRulingEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getRulingEmployeeId()));
            dto.setPreparationStatusName(staticDataService.getCodeName("PREPARATION_STATUS",dto.getPrepareStatus()+""));
        }
        return list;
    }

    @Override
    public void checkPrepareOrgRules(CustPreparationDTO dto) {
        PrepareConfig prepareConfig = configService.queryValid();

        if (prepareConfig == null) {
            return;
        }
        //加上判断如果报备部门id为空，则查询报备员工所属部门
        EmployeeJobRole employeeJobRole = jobRoleService.queryValidMain(dto.getPrepareEmployeeId());
        String prepareOrgId = "";
        if (dto.getPrepareOrgId() == null) {
            prepareOrgId = "," + employeeJobRole.getOrgId() + ",";
        } else {
            prepareOrgId = "," + dto.getPrepareOrgId() + ",";
        }

        //限制部门报备次数
        if (!StringUtils.isEmpty(prepareConfig.getLimitPrepareOrgId())) {
            String limitOrgId = "," + prepareConfig.getLimitPrepareOrgId() + ",";

            if (limitOrgId.indexOf(prepareOrgId) != -1) {
                Integer limitOrgPrepareTimes = prepareConfig.getLimitOrgPrepareTimes();
                LocalDateTime limitTime1 = TimeUtils.addTime(LocalDateTime.now(), ChronoUnit.DAYS, -7);
                List<CustPreparation> existList1 = this.preparationMapper.selectList(new QueryWrapper<CustPreparation>().lambda()
                        .eq(CustPreparation::getPrepareOrgId, employeeJobRole.getOrgId())
                        .ge(CustPreparation::getPrepareTime, limitTime1));
                if (existList1.size() >= limitOrgPrepareTimes) {
                    throw new AlreadyExistException(" 该部门在过去的7天内已超过限制报备次数" + limitOrgPrepareTimes + "不允许报备！", ErrorKind.ALREADY_EXIST.getCode());
                }
            }
        }
        //判断责任楼盘 1现 2责任 3 散
        String limitConsultOrgId = "," + prepareConfig.getLimitOrgId() + ",";
        //限制家装顾问只允许报备责任楼盘则进判断
        if (prepareConfig.getIsLimitConsult().equals(1)) {
            if (limitConsultOrgId.indexOf(prepareOrgId) != -1) {
                HousesPlan housesPlan = housesPlanService.queryHousesPlan(dto.getHouseId(), dto.getPrepareEmployeeId());
                if (housesPlan == null) {
                    throw new NotFoundException("报备员工只能报备责任楼盘！", ErrorKind.NOT_FOUND.getCode());
                }
            }
        }
    }

    @Override
    public void checkCustomerRules(String mobileNo) {

        //客户申报状态为有效期内，不允许办理报备
        List<CustPreparationDTO> list = this.queryCustPreparaton(mobileNo, null, "0", null, "2");
        if (ArrayUtils.isNotEmpty(list)) {
            throw new AlreadyExistException(" 该客户有效期内存在有效的报备！", ErrorKind.ALREADY_EXIST.getCode());
        }

        PrepareConfig prepareConfig = configService.queryValid();
        if (prepareConfig == null) {
            return;
        }
        //限制报备客户周期范围的报备次数limitCycle限制周期limitTimes限制次数
        Integer limitTimes = prepareConfig.getLimitCustPrepareTimes();
        Integer limitCycle = prepareConfig.getLimitCustPrepareCycle();
        LocalDateTime limitTime = TimeUtils.addTime(LocalDateTime.now(), ChronoUnit.DAYS, -limitCycle);
        List<CustPreparationDTO> existList = this.preparationMapper.queryPrepareByTime(mobileNo, limitTime);
        if (existList.size() >= limitTimes) {
            throw new AlreadyExistException(" 该客户在" + limitCycle + "天内已超过限制报备次数" + limitTimes + "不允许报备！", ErrorKind.ALREADY_EXIST.getCode());
        }
    }

    @Override
    public Map<String, String> getCustomerNoAndSec() {
        Map<String, String> map = new HashMap<>();
        //todo 未定义权限编码
        map.put("isContinueAuth", SecurityUtils.hasFuncId("") + "");
        Long seq = dualService.nextval(CustNoMaxCycleSeq.class);
        map.put("custNo", "KH" + seq);
        return map;
    }

    @Override
    public List<CustPreparationDTO> queryPrepareByCustIdAndStatus(Long custId, String status) {
        List<CustPreparationDTO> list = this.baseMapper.queryPrepareByCustIdAndStatus(custId,status);
        if (ArrayUtils.isEmpty(list)) {
            return list;
        }
        for (CustPreparationDTO dto : list) {
            dto.setPrepareEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getPrepareEmployeeId()));
            dto.setEnterEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getEnterEmployeeId()));
            if(StringUtils.equals(dto.getCustProperty(),"6")){
                dto.setCustPropertyName("主管补备");
            }else{
                dto.setCustPropertyName(staticDataService.getCodeName("CUSTOMER_PROPERTY", dto.getCustProperty()));
            }
            dto.setHouseModeName(staticDataService.getCodeName("HOUSE_MODE", dto.getHouseMode()));
            dto.setHouseAddress(housesService.queryHouseName(dto.getHouseId()) + dto.getHouseBuilding() + dto.getHouseRoomNo());
        }
        return list;
    }


}
