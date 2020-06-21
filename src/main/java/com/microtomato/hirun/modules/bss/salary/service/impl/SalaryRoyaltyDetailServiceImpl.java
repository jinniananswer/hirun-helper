package com.microtomato.hirun.modules.bss.salary.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.SalaryRoyaltyStrategy;
import com.microtomato.hirun.modules.bss.config.entity.po.SalaryStatusFeeMapping;
import com.microtomato.hirun.modules.bss.config.service.ISalaryRoyaltyStrategyService;
import com.microtomato.hirun.modules.bss.config.service.ISalaryStatusFeeMappingService;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeCompositeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderPlaneSketchDTO;
import com.microtomato.hirun.modules.bss.order.service.IFeeDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderPlaneSketchService;
import com.microtomato.hirun.modules.bss.salary.entity.dto.DesignRoyaltyDetailDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.EmployeeSalaryRoyaltyDetailDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.ProjectRoyaltyDetailDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryRoyaltyDetailDTO;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryRoyaltyDetail;
import com.microtomato.hirun.modules.bss.salary.mapper.SalaryRoyaltyDetailMapper;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryRoyaltyDetailService;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工工资提成明细(SalaryRoyaltyDetail)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-17 17:57:21
 */
@Service
@DataSource(DataSourceKey.INS)
@Slf4j
public class SalaryRoyaltyDetailServiceImpl extends ServiceImpl<SalaryRoyaltyDetailMapper, SalaryRoyaltyDetail> implements ISalaryRoyaltyDetailService {

    @Autowired
    private SalaryRoyaltyDetailMapper salaryRoyaltyDetailMapper;

    @Autowired
    private IFeeDomainService feeDomainService;

    @Autowired
    private ISalaryRoyaltyStrategyService salaryRoyaltyStrategyService;

    @Autowired
    private ISalaryStatusFeeMappingService salaryStatusFeeMappingService;

    @Autowired
    private IOrderPlaneSketchService orderPlaneSketchService;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IOrgService orgService;


    /**
     * 根据订单ID、员工ID，提成明细项列表查询用户提成详情信息
     * @param orderId
     * @param employeeId
     * @param items
     * @return
     */
    @Override
    public List<SalaryRoyaltyDetail> queryByOrderIdEmployeeIdItems(Long orderId, Long employeeId, List<String> items) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.list(Wrappers.<SalaryRoyaltyDetail>lambdaQuery()
                .eq(SalaryRoyaltyDetail::getOrderId, orderId)
                .eq(SalaryRoyaltyDetail::getEmployeeId, employeeId)
                .in(SalaryRoyaltyDetail::getItem, items)
                .ge(SalaryRoyaltyDetail::getEndTime, now));
    }

    /**
     * 获取订单提成信息
     * @param orderId
     * @return
     */
    @Override
    public SalaryRoyaltyDetailDTO queryByOrderId(Long orderId) {
        SalaryRoyaltyDetailDTO result = new SalaryRoyaltyDetailDTO();
        List<EmployeeSalaryRoyaltyDetailDTO> employeeSalaryRoyaltyDetails = this.salaryRoyaltyDetailMapper.querySalaries(orderId);

        if (ArrayUtils.isEmpty(employeeSalaryRoyaltyDetails)) {
            return result;
        }

        List<OrderFeeCompositeDTO> compositeFees = this.feeDomainService.buildCompositeFee(orderId);
        List<DesignRoyaltyDetailDTO> designRoyaltyDetails = new ArrayList<>();

        List<EmployeeSalaryRoyaltyDetailDTO> projectEmployeeRoyaltyDetails = new ArrayList<>();
        employeeSalaryRoyaltyDetails.forEach(employeeSalaryRoyaltyDetail -> {
            String type = employeeSalaryRoyaltyDetail.getType();
            if (StringUtils.equals("1", type) || StringUtils.equals("2", type)) {
                //设计提成与经营提成
                designRoyaltyDetails.add(this.buildDesignRoyaltyDetail(employeeSalaryRoyaltyDetail, compositeFees, orderId));
            } else if (StringUtils.equals("3", type)) {
                //工程提成
                projectEmployeeRoyaltyDetails.add(employeeSalaryRoyaltyDetail);
            } else if (StringUtils.equals("4", type)) {
                //主材提成
            }
        });

        if (ArrayUtils.isNotEmpty(projectEmployeeRoyaltyDetails)) {
            List<ProjectRoyaltyDetailDTO> projectRoyaltyDetails = this.buildProjectRoyaltyDetails(projectEmployeeRoyaltyDetails, compositeFees);
            result.setProjectRoyaltyDetails(projectRoyaltyDetails);
        }

        if (ArrayUtils.isNotEmpty(designRoyaltyDetails)) {
            result.setDesignRoyaltyDetails(designRoyaltyDetails);
        }

        return result;
    }

    /**
     * 构建设计费提成展示数据
     * @param employeeSalaryRoyaltyDetail
     * @param compositeFees
     * @return
     */
    private DesignRoyaltyDetailDTO buildDesignRoyaltyDetail(EmployeeSalaryRoyaltyDetailDTO employeeSalaryRoyaltyDetail, List<OrderFeeCompositeDTO> compositeFees, Long orderId) {
        DesignRoyaltyDetailDTO designRoyaltyDetail = new DesignRoyaltyDetailDTO();
        BeanUtils.copyProperties(employeeSalaryRoyaltyDetail, designRoyaltyDetail);

        SalaryRoyaltyStrategy strategy = this.salaryRoyaltyStrategyService.getByStrategyId(designRoyaltyDetail.getStrategyId());
        designRoyaltyDetail.setNodeCondition(this.staticDataService.getCodeName("NODE_CONDITION", strategy.getOrderStatus()));

        OrderFeeCompositeDTO compositeFee = this.find(compositeFees, "1", null);
        Double designFee = compositeFee.getTotalFee().longValue() * 1.00 / 100;
        designRoyaltyDetail.setDesignFee(designFee);

        OrderPlaneSketchDTO planeSketch = this.orderPlaneSketchService.getPlaneSketch(orderId);
        Integer designFeeStandard = 0;
        if (planeSketch != null) {
            designFeeStandard = planeSketch.getDesignFeeStandard();
        }

        designRoyaltyDetail.setDesignFeeStandard(designFeeStandard);
        designRoyaltyDetail.setValue(strategy.getValue());
        Long totalRoyalty = employeeSalaryRoyaltyDetail.getTotalRoyalty();
        if (totalRoyalty != null) {
            designRoyaltyDetail.setTotalRoyalty(totalRoyalty.longValue() / 100d);
        }
        Long thisMonthFetch = employeeSalaryRoyaltyDetail.getThisMonthFetch();
        if (thisMonthFetch != null) {
            designRoyaltyDetail.setThisMonthFetch(thisMonthFetch / 100d);
        }

        Long alreadyFetch = employeeSalaryRoyaltyDetail.getAlreadyFetch();
        if (alreadyFetch != null) {
            designRoyaltyDetail.setAlreadyFetch(alreadyFetch / 100d);
        }

        Long orgId = employeeSalaryRoyaltyDetail.getOrgId();
        Org org = this.orgService.queryByOrgId(orgId);
        if (org != null) {
            designRoyaltyDetail.setOrgName(org.getName());
        }
        designRoyaltyDetail.setJobRoleName(this.staticDataService.getCodeName("JOB_ROLE", employeeSalaryRoyaltyDetail.getJobRole()));
        designRoyaltyDetail.setJobGradeName(this.staticDataService.getCodeName("JOB_GRADE", employeeSalaryRoyaltyDetail.getJobGrade()));
        designRoyaltyDetail.setEmployeeStatusName(this.staticDataService.getCodeName("EMPLOYEE_STATUS", employeeSalaryRoyaltyDetail.getEmployeeStatus()));
        designRoyaltyDetail.setAuditStatusName(this.staticDataService.getCodeName("SALARY_AUDIT_STATUS", employeeSalaryRoyaltyDetail.getAuditStatus()));

        return designRoyaltyDetail;
    }

    /**
     * 构建工程款展示编辑数据
     * @param employeeSalaryRoyaltyDetails
     * @param compositeFees
     * @return
     */
    private List<ProjectRoyaltyDetailDTO> buildProjectRoyaltyDetails(List<EmployeeSalaryRoyaltyDetailDTO> employeeSalaryRoyaltyDetails,
                                                                     List<OrderFeeCompositeDTO> compositeFees) {
        //第一步，将多行(主要是basic、door，furniture三条合成一条）数据变成一行，匹配条件，employee_id相同
        if (ArrayUtils.isEmpty(employeeSalaryRoyaltyDetails)) {
            return null;
        }

        Map<String, ProjectRoyaltyDetailDTO> temp = new HashMap<>();
        employeeSalaryRoyaltyDetails.forEach(employeeSalaryRoyaltyDetail -> {
            SalaryRoyaltyStrategy strategy = this.salaryRoyaltyStrategyService.getByStrategyId(employeeSalaryRoyaltyDetail.getStrategyId());

            String key = employeeSalaryRoyaltyDetail.getEmployeeId() + "_" + strategy.getOrderStatus();
            ProjectRoyaltyDetailDTO projectRoyaltyDetail = null;
            if (temp.containsKey(key)) {
                projectRoyaltyDetail = temp.get(key);
            } else {
                projectRoyaltyDetail = new ProjectRoyaltyDetailDTO();
                BeanUtils.copyProperties(employeeSalaryRoyaltyDetail, projectRoyaltyDetail);
                projectRoyaltyDetail.setJobRoleName(this.staticDataService.getCodeName("JOB_ROLE", employeeSalaryRoyaltyDetail.getJobRole()));
                projectRoyaltyDetail.setValue(strategy.getValue());
                temp.put(key, projectRoyaltyDetail);
            }

            projectRoyaltyDetail.setNodeCondition(this.staticDataService.getCodeName("NODE_CONDITION", strategy.getOrderStatus()));

            SalaryStatusFeeMapping statusFeeMapping = this.salaryStatusFeeMappingService.getStatusFeeMapping(strategy.getOrderStatus());
            Integer periods = statusFeeMapping.getPeriods();
            OrderFeeCompositeDTO orderFee = this.find(compositeFees, "2", periods);

            if ("|23|26|29|".indexOf("|"+employeeSalaryRoyaltyDetail.getItem()+"|") >= 0) {
                //basic
                projectRoyaltyDetail.setBasicFee(orderFee.getBasicFee());
                projectRoyaltyDetail.setBasicRoyalty(employeeSalaryRoyaltyDetail.getTotalRoyalty());
                projectRoyaltyDetail.setBasicAlreadyFetch(employeeSalaryRoyaltyDetail.getAlreadyFetch());

            } else if ("|24|27|30|".indexOf("|"+employeeSalaryRoyaltyDetail.getItem()+"|") >= 0) {
                //door
                projectRoyaltyDetail.setDoorFee(orderFee.getDoorFee());
                projectRoyaltyDetail.setDoorRoyalty(employeeSalaryRoyaltyDetail.getTotalRoyalty());
                projectRoyaltyDetail.setDoorAlreadyFetch(employeeSalaryRoyaltyDetail.getAlreadyFetch());
            } else if ("|25|28|31|".indexOf("|"+employeeSalaryRoyaltyDetail.getItem()+"|") >= 0) {
                //furniture
                projectRoyaltyDetail.setFurnitureFee(orderFee.getFurnitureFee());
                projectRoyaltyDetail.setFurnitureRoyalty(employeeSalaryRoyaltyDetail.getTotalRoyalty());
                projectRoyaltyDetail.setFurnitureAlreadyFetch(employeeSalaryRoyaltyDetail.getAlreadyFetch());
            }
        });

        if (temp.size() > 0) {
            //计算本月可提
            temp.values().forEach(value -> {
                Long total = 0L;
                //计算本月可提的basic
                Long basicFee = value.getBasicFee();
                Long basicAlready = value.getBasicAlreadyFetch();
                if (basicFee == null) {
                    basicFee = 0L;
                }
                if (basicAlready == null) {
                    basicAlready = 0L;
                }
                total = basicFee - basicAlready;

                Long doorFee = value.getDoorFee();
                Long doorAlready = value.getDoorAlreadyFetch();
                if (doorFee == null) {
                    doorFee = 0L;
                }
                if (doorAlready == null) {
                    doorAlready = 0L;
                }

                total += doorFee - doorAlready;

                Long furnitureFee = value.getFurnitureFee();
                Long furnitureAlready = value.getFurnitureAlreadyFetch();
                if (furnitureFee == null) {
                    furnitureFee = 0L;
                }
                if (furnitureAlready == null) {
                    furnitureAlready = 0L;
                }

                total += furnitureFee - furnitureAlready;

                value.setThisMonthFetch(total);
            });
            return new ArrayList<>(temp.values());
        } else {
            return null;
        }
    }

    /**
     * 从费用明细中找出符合条件的费用
     * @param compositeFees
     * @return
     */
    private OrderFeeCompositeDTO find(List<OrderFeeCompositeDTO> compositeFees, String type, Integer periods) {
        if (ArrayUtils.isEmpty(compositeFees)) {
            return null;
        }

        for (OrderFeeCompositeDTO compositeFee : compositeFees) {
            if (StringUtils.equals("1", type) && StringUtils.equals(type, compositeFee.getType())) {
                //设计费
                return compositeFee;
            } else if (StringUtils.equals("2", type) && StringUtils.equals(type, compositeFee.getType()) && periods.equals(compositeFee.getPeriods())) {
                //工程款
                return compositeFee;
            }
        }

        return null;
    }

}