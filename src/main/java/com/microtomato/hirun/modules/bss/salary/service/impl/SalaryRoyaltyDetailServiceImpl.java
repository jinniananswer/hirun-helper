package com.microtomato.hirun.modules.bss.salary.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.SalaryRoyaltyStrategy;
import com.microtomato.hirun.modules.bss.config.entity.po.SalaryStatusFeeMapping;
import com.microtomato.hirun.modules.bss.config.service.ISalaryRoyaltyStrategyService;
import com.microtomato.hirun.modules.bss.config.service.ISalaryStatusFeeMappingService;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeCompositeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderPlaneSketchDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPlaneSketch;
import com.microtomato.hirun.modules.bss.order.service.IFeeDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderPlaneSketchService;
import com.microtomato.hirun.modules.bss.salary.entity.dto.*;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryRoyaltyDetail;
import com.microtomato.hirun.modules.bss.salary.exception.SalaryException;
import com.microtomato.hirun.modules.bss.salary.mapper.SalaryRoyaltyDetailMapper;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryRoyaltyDetailService;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
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

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleService;


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
        OrderPlaneSketch planeSketch = this.orderPlaneSketchService.getByOrderId(orderId);

        employeeSalaryRoyaltyDetails.forEach(employeeSalaryRoyaltyDetail -> {
            String type = employeeSalaryRoyaltyDetail.getType();
            if (StringUtils.equals("1", type) || StringUtils.equals("2", type)) {
                //设计提成与经营提成
                designRoyaltyDetails.add(this.buildDesignRoyaltyDetail(employeeSalaryRoyaltyDetail, compositeFees, orderId, planeSketch));
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
    private DesignRoyaltyDetailDTO buildDesignRoyaltyDetail(EmployeeSalaryRoyaltyDetailDTO employeeSalaryRoyaltyDetail, List<OrderFeeCompositeDTO> compositeFees, Long orderId, OrderPlaneSketch planeSketch) {
        DesignRoyaltyDetailDTO designRoyaltyDetail = new DesignRoyaltyDetailDTO();
        BeanUtils.copyProperties(employeeSalaryRoyaltyDetail, designRoyaltyDetail);

        SalaryRoyaltyStrategy strategy = this.salaryRoyaltyStrategyService.getByStrategyId(designRoyaltyDetail.getStrategyId());
        designRoyaltyDetail.setNodeCondition(this.staticDataService.getCodeName("NODE_CONDITION", designRoyaltyDetail.getOrderStatus()));

        OrderFeeCompositeDTO compositeFee = this.find(compositeFees, "1", null);
        if (compositeFee == null) {
            throw new SalaryException(SalaryException.SalaryExceptionEnum.ORDER_FEE_NOT_FOUND, "设计费");
        }
        Double designFee = compositeFee.getTotalFee().longValue() * 1.00 / 100;
        designRoyaltyDetail.setDesignFee(designFee);

        Integer designFeeStandard = 0;
        if (planeSketch != null) {
            designFeeStandard = planeSketch.getDesignFeeStandard();
        }

        designRoyaltyDetail.setDesignFeeStandard(designFeeStandard);
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
        designRoyaltyDetail.setItemName(this.staticDataService.getCodeName("ROYALTY_ITEM", employeeSalaryRoyaltyDetail.getItem()));

        return designRoyaltyDetail;
    }

    /**
     * 构建工程款展示编辑数据
     * @param employeeSalaryRoyaltyDetails
     * @param compositeFees
     * @return
     */
    private List<ProjectRoyaltyDetailDTO> buildProjectRoyaltyDetails(List<? extends EmployeeSalaryRoyaltyDetailDTO> employeeSalaryRoyaltyDetails,
                                                                     List<OrderFeeCompositeDTO> compositeFees) {
        //第一步，将多行(主要是basic、door，furniture三条合成一条）数据变成一行，匹配条件，employee_id相同
        if (ArrayUtils.isEmpty(employeeSalaryRoyaltyDetails)) {
            return null;
        }

        Map<String, ProjectRoyaltyDetailDTO> temp = new HashMap<>();
        employeeSalaryRoyaltyDetails.forEach(employeeSalaryRoyaltyDetail -> {

            String key = employeeSalaryRoyaltyDetail.getEmployeeId() + "_" + employeeSalaryRoyaltyDetail.getOrderStatus();
            ProjectRoyaltyDetailDTO projectRoyaltyDetail = null;
            SalaryStatusFeeMapping statusFeeMapping = this.salaryStatusFeeMappingService.getStatusFeeMapping(employeeSalaryRoyaltyDetail.getOrderStatus());
            Integer periods = statusFeeMapping.getPeriods();
            OrderFeeCompositeDTO orderFee = this.find(compositeFees, "2", periods);
            if (orderFee == null) {
                throw new SalaryException(SalaryException.SalaryExceptionEnum.ORDER_FEE_NOT_FOUND, "工程款");
            }

            if (temp.containsKey(key)) {
                projectRoyaltyDetail = temp.get(key);
            } else {
                projectRoyaltyDetail = new ProjectRoyaltyDetailDTO();
                BeanUtils.copyProperties(employeeSalaryRoyaltyDetail, projectRoyaltyDetail);
                projectRoyaltyDetail.setJobRoleName(this.staticDataService.getCodeName("JOB_ROLE", employeeSalaryRoyaltyDetail.getJobRole()));
                if (StringUtils.isNotBlank(projectRoyaltyDetail.getJobGrade())) {
                    projectRoyaltyDetail.setJobGradeName(this.staticDataService.getCodeName("JOB_GRADE", employeeSalaryRoyaltyDetail.getJobGrade()));
                }
                projectRoyaltyDetail.setEmployeeStatusName(this.staticDataService.getCodeName("EMPLOYEE_STATUS", projectRoyaltyDetail.getEmployeeStatus()));
                projectRoyaltyDetail.setPeriods(periods);
                projectRoyaltyDetail.setPeriodName(this.staticDataService.getCodeName("FEE_PERIODS", periods + ""));
                projectRoyaltyDetail.setValue(employeeSalaryRoyaltyDetail.getValue());
                projectRoyaltyDetail.setContractFee(orderFee.getContractFee());
                Long orgId = employeeSalaryRoyaltyDetail.getOrgId();

                if (orgId != null) {
                    Org org = this.orgService.getById(orgId);
                    projectRoyaltyDetail.setOrgName(org.getName());
                }
                projectRoyaltyDetail.setAuditStatusName(this.staticDataService.getCodeName("SALARY_AUDIT_STATUS", projectRoyaltyDetail.getAuditStatus()));
                temp.put(key, projectRoyaltyDetail);
            }

            projectRoyaltyDetail.setNodeCondition(this.staticDataService.getCodeName("NODE_CONDITION", employeeSalaryRoyaltyDetail.getOrderStatus()));


            if ("|23|26|29|".indexOf("|"+employeeSalaryRoyaltyDetail.getItem()+"|") >= 0) {
                //basic
                projectRoyaltyDetail.setBasicId(employeeSalaryRoyaltyDetail.getId());
                Long basicFee = orderFee.getBasicFee();
                if (basicFee != null) {
                    projectRoyaltyDetail.setBasicFee(basicFee / 100d);
                } else {
                    projectRoyaltyDetail.setBasicFee(0d);
                }
                Long totalRoyalty = employeeSalaryRoyaltyDetail.getTotalRoyalty();
                if (totalRoyalty != null) {
                    projectRoyaltyDetail.setBasicRoyalty(totalRoyalty/100d);
                } else {
                    projectRoyaltyDetail.setBasicRoyalty(0d);
                }
                Long alreadyFetch = employeeSalaryRoyaltyDetail.getAlreadyFetch();
                if (alreadyFetch != null) {
                    projectRoyaltyDetail.setBasicAlreadyFetch(alreadyFetch/100d);
                } else {
                    projectRoyaltyDetail.setBasicAlreadyFetch(0d);
                }

            } else if ("|24|27|30|".indexOf("|"+employeeSalaryRoyaltyDetail.getItem()+"|") >= 0) {
                //door
                projectRoyaltyDetail.setDoorId(employeeSalaryRoyaltyDetail.getId());
                Long doorFee = orderFee.getDoorFee();
                if (doorFee != null) {
                    projectRoyaltyDetail.setDoorFee(doorFee/100d);
                } else {
                    projectRoyaltyDetail.setDoorFee(0d);
                }
                Long totalRoyalty = employeeSalaryRoyaltyDetail.getTotalRoyalty();
                if (totalRoyalty != null) {
                    projectRoyaltyDetail.setDoorRoyalty(totalRoyalty/100d);
                } else {
                    projectRoyaltyDetail.setDoorRoyalty(0d);
                }
                Long alreadyFetch = employeeSalaryRoyaltyDetail.getAlreadyFetch();
                if (alreadyFetch != null) {
                    projectRoyaltyDetail.setDoorAlreadyFetch(alreadyFetch/100d);
                } else {
                    projectRoyaltyDetail.setDoorAlreadyFetch(0d);
                }
            } else if ("|25|28|31|".indexOf("|"+employeeSalaryRoyaltyDetail.getItem()+"|") >= 0) {
                //furniture
                projectRoyaltyDetail.setFurnitureId(employeeSalaryRoyaltyDetail.getId());
                Long furnitureFee = orderFee.getFurnitureFee();
                if (furnitureFee != null) {
                    projectRoyaltyDetail.setFurnitureFee(furnitureFee/100d);
                } else {
                    projectRoyaltyDetail.setFurnitureFee(0d);
                }
                Long totalRoyalty = employeeSalaryRoyaltyDetail.getTotalRoyalty();
                if (totalRoyalty != null) {
                    projectRoyaltyDetail.setFurnitureRoyalty(totalRoyalty/100d);
                } else {
                    projectRoyaltyDetail.setFurnitureRoyalty(0d);
                }

                Long alreadyFetch = employeeSalaryRoyaltyDetail.getAlreadyFetch();
                if (alreadyFetch != null) {
                    projectRoyaltyDetail.setFurnitureAlreadyFetch(alreadyFetch/100d);
                } else {
                    projectRoyaltyDetail.setFurnitureAlreadyFetch(0d);
                }
            }
        });

        if (temp.size() > 0) {
            //计算本月可提
            temp.values().forEach(value -> {
                Double total = 0d;
                //计算本月可提的basic
                Double basicRoyalty = value.getBasicRoyalty();
                Double basicAlready = value.getBasicAlreadyFetch();
                if (basicRoyalty == null) {
                    basicRoyalty = 0d;
                }
                if (basicAlready == null) {
                    basicAlready = 0d;
                }
                total = basicRoyalty - basicAlready;

                Double doorRoyalty = value.getDoorRoyalty();
                Double doorAlready = value.getDoorAlreadyFetch();
                if (doorRoyalty == null) {
                    doorRoyalty = 0d;
                }
                if (doorAlready == null) {
                    doorAlready = 0d;
                }

                total += doorRoyalty - doorAlready;

                Double furnitureRoyalty = value.getFurnitureRoyalty();
                Double furnitureAlready = value.getFurnitureAlreadyFetch();
                if (furnitureRoyalty == null) {
                    furnitureRoyalty = 0d;
                }
                if (furnitureAlready == null) {
                    furnitureAlready = 0d;
                }

                total += furnitureRoyalty - furnitureAlready;

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

    /**
     * 保存提成明细数据
     * @param designRoyaltyDetails
     */
    @Override
    public void saveDesignRoyaltyDetails(List<DesignRoyaltyDetailDTO> designRoyaltyDetails) {
        if (ArrayUtils.isEmpty(designRoyaltyDetails)) {
            return;
        }
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        List<SalaryRoyaltyDetail> royaltyDetails = new ArrayList<>();
        List<SalaryRoyaltyDetail> createDetails = new ArrayList<>();
        designRoyaltyDetails.forEach(designRoyaltyDetail -> {
            SalaryRoyaltyDetail detail = new SalaryRoyaltyDetail();
            BeanUtils.copyProperties(designRoyaltyDetail, detail);

            if (StringUtils.equals("2", designRoyaltyDetail.getIsModified()) && designRoyaltyDetail.getId() != null) {
                //表示删除
                detail.setEndTime(now);
                royaltyDetails.add(detail);
                return;
            }

            SalaryRoyaltyDetail oldDetail = null;
            if (detail.getId() != null) {
                oldDetail = this.getById(detail.getId());
            }
            boolean isModified = false;

            //表示被修改过了
            Double totalRoyaltyDouble = designRoyaltyDetail.getTotalRoyalty();
            if (totalRoyaltyDouble != null) {
                totalRoyaltyDouble = totalRoyaltyDouble * 100;
                Long totalRoyalty = new Long(totalRoyaltyDouble.longValue());
                detail.setTotalRoyalty(totalRoyalty);
            } else {
                detail.setTotalRoyalty(0L);
            }

            if (oldDetail != null && !detail.getTotalRoyalty().equals(oldDetail.getTotalRoyalty())) {
                isModified = true;
            }

            Double alreadyFetchDouble = designRoyaltyDetail.getAlreadyFetch();
            if (alreadyFetchDouble != null) {
                alreadyFetchDouble = alreadyFetchDouble * 100;
                Long alreadyFetch = new Long(alreadyFetchDouble.longValue());
                detail.setAlreadyFetch(alreadyFetch);
            } else {
                detail.setAlreadyFetch(0L);
            }

            if (oldDetail != null && !detail.getAlreadyFetch().equals(oldDetail.getAlreadyFetch())) {
                isModified = true;
            }

            Double thisMonthFetchDouble = designRoyaltyDetail.getThisMonthFetch();
            if (thisMonthFetchDouble != null) {
                thisMonthFetchDouble = thisMonthFetchDouble * 100;
                Long thisMonthFetch = new Long(thisMonthFetchDouble.longValue());
                detail.setThisMonthFetch(thisMonthFetch);
            } else {
                detail.setAlreadyFetch(0L);
            }

            if (oldDetail != null && !detail.getThisMonthFetch().equals(oldDetail.getThisMonthFetch())) {
                isModified = true;
            }

            if (oldDetail != null && !detail.getSalaryMonth().equals(oldDetail.getSalaryMonth())) {
                isModified = true;
            }

            if (isModified) {
                detail.setIsModified("1");
            }

            if (detail.getId() == null) {
                detail.setStartTime(now);
                detail.setEndTime(TimeUtils.getForeverTime());
                detail.setIsModified(null);
                createDetails.add(detail);
            } else {
                royaltyDetails.add(detail);
            }
        });
        if (ArrayUtils.isNotEmpty(createDetails)) {
            this.saveBatch(createDetails);
        }

        if (ArrayUtils.isNotEmpty(royaltyDetails)) {
            this.updateBatchById(royaltyDetails);
        }
    }

    /**
     * 提交提成明细数据进行审核
     * @param designRoyaltyDetails
     */
    @Override
    public void auditDesignRoyaltyDetails(List<DesignRoyaltyDetailDTO> designRoyaltyDetails) {
        if (ArrayUtils.isEmpty(designRoyaltyDetails)) {
            return;
        }

        designRoyaltyDetails.forEach(designRoyaltyDetail -> {
            designRoyaltyDetail.setAuditStatus("1");
        });

        this.saveDesignRoyaltyDetails(designRoyaltyDetails);
    }

    /**
     * 保存工程提成明细数据
     * @param projectRoyaltyDetails
     */
    @Override
    public void saveProjectRoyaltyDetails(List<ProjectRoyaltyDetailDTO> projectRoyaltyDetails) {
        if (ArrayUtils.isEmpty(projectRoyaltyDetails)) {
            return;
        }

        LocalDateTime now = RequestTimeHolder.getRequestTime();
        List<SalaryRoyaltyDetail> createDetails = new ArrayList<>();
        List<SalaryRoyaltyDetail> royaltyDetails = new ArrayList<>();
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();

        projectRoyaltyDetails.forEach(projectDetail -> {
            String isModify = projectDetail.getIsModified();
            if (StringUtils.equals("2", isModify) && projectDetail.getBasicId() == null) {
                return;
            }
            SalaryStatusFeeMapping statusFeeMapping = this.salaryStatusFeeMappingService.getStatusFeeMapping(projectDetail.getOrderStatus());
            Integer period = statusFeeMapping.getPeriods();

            SalaryRoyaltyDetail basicDetail = new SalaryRoyaltyDetail();
            SalaryRoyaltyDetail doorDetail = new SalaryRoyaltyDetail();
            SalaryRoyaltyDetail furnitureDetail = new SalaryRoyaltyDetail();

            BeanUtils.copyProperties(projectDetail, basicDetail);
            Double basicRoyalty = projectDetail.getBasicRoyalty();
            Double basicAlreadyFetch = projectDetail.getBasicAlreadyFetch();

            if (StringUtils.equals("2", isModify) && projectDetail.getBasicId() != null) {
                //表示删除
                basicDetail.setId(projectDetail.getBasicId());
                basicDetail.setEndTime(now);
                royaltyDetails.add(basicDetail);
            } else {
                if (basicRoyalty == null) {
                    basicRoyalty = 0d;
                    basicDetail.setTotalRoyalty(0L);
                } else {
                    basicRoyalty = basicRoyalty * 100;
                    basicDetail.setTotalRoyalty(new Long(basicRoyalty.longValue()));
                }

                if (basicAlreadyFetch == null) {
                    basicAlreadyFetch = 0d;
                    basicDetail.setAlreadyFetch(0L);
                } else {
                    basicAlreadyFetch = basicAlreadyFetch * 100;
                    basicDetail.setAlreadyFetch(new Long(basicAlreadyFetch.longValue()));
                }

                Double basicThisMonthFetch = basicRoyalty - basicAlreadyFetch;
                basicDetail.setThisMonthFetch(new Long(basicThisMonthFetch.longValue()));

                if (projectDetail.getBasicId() == null) {
                    basicDetail.setCreateEmployeeId(employeeId);
                    basicDetail.setStartTime(now);
                    basicDetail.setEndTime(TimeUtils.getForeverTime());
                    basicDetail.setIsModified(null);
                    basicDetail.setType("3");
                    if (StringUtils.equals("1", period + "")) {
                        basicDetail.setItem("23");
                    } else if (StringUtils.equals("2", period + "")) {
                        basicDetail.setItem("26");
                    } else if (StringUtils.equals("3", period + "")) {
                        basicDetail.setItem("29");
                    }
                    basicDetail.setIsModified(null);
                    createDetails.add(basicDetail);
                } else {
                    basicDetail.setId(projectDetail.getBasicId());

                    boolean isModified = false;
                    SalaryRoyaltyDetail oldDetail = this.getById(projectDetail.getBasicId());
                    if (oldDetail != null && !basicDetail.getTotalRoyalty().equals(oldDetail.getTotalRoyalty())) {
                        isModified = true;
                    }

                    if (oldDetail != null && !basicDetail.getAlreadyFetch().equals(oldDetail.getAlreadyFetch())) {
                        isModified = true;
                    }

                    if (oldDetail != null && !basicDetail.getThisMonthFetch().equals(oldDetail.getThisMonthFetch())) {
                        isModified = true;
                    }

                    if (isModified) {
                        //表示被人为修改过了
                        basicDetail.setIsModified("1");
                    }
                    royaltyDetails.add(basicDetail);
                }
            }

            BeanUtils.copyProperties(projectDetail, doorDetail);
            Double doorRoyalty = projectDetail.getDoorRoyalty();
            Double doorAlreadyFetch = projectDetail.getDoorAlreadyFetch();

            if (StringUtils.equals("2", isModify) && projectDetail.getDoorId() != null) {
                //表示删除
                doorDetail.setId(projectDetail.getDoorId());
                doorDetail.setEndTime(now);
                royaltyDetails.add(doorDetail);
            } else {
                if (doorRoyalty == null) {
                    doorRoyalty = 0d;
                    doorDetail.setTotalRoyalty(0L);
                } else {
                    doorRoyalty = doorRoyalty * 100;
                    doorDetail.setTotalRoyalty(new Long(doorRoyalty.longValue()));
                }

                if (doorAlreadyFetch == null) {
                    doorAlreadyFetch = 0d;
                    doorDetail.setAlreadyFetch(0L);
                } else {
                    doorAlreadyFetch = doorAlreadyFetch * 100;
                    doorDetail.setAlreadyFetch(new Long(doorAlreadyFetch.longValue()));
                }

                Double doorThisMonthFetch = doorRoyalty - doorAlreadyFetch;
                doorDetail.setThisMonthFetch(new Long(doorThisMonthFetch.longValue()));

                if (projectDetail.getDoorId() == null) {
                    doorDetail.setCreateEmployeeId(employeeId);
                    doorDetail.setStartTime(now);
                    doorDetail.setEndTime(TimeUtils.getForeverTime());
                    doorDetail.setIsModified(null);
                    doorDetail.setType("3");
                    if (StringUtils.equals("1", period + "")) {
                        doorDetail.setItem("24");
                    } else if (StringUtils.equals("2", period + "")) {
                        doorDetail.setItem("27");
                    } else if (StringUtils.equals("3", period + "")) {
                        doorDetail.setItem("30");
                    }
                    doorDetail.setIsModified(null);
                    createDetails.add(doorDetail);
                } else {
                    doorDetail.setId(projectDetail.getDoorId());

                    boolean isModified = false;
                    SalaryRoyaltyDetail oldDetail = this.getById(projectDetail.getDoorId());
                    if (oldDetail != null && !doorDetail.getTotalRoyalty().equals(oldDetail.getTotalRoyalty())) {
                        isModified = true;
                    }

                    if (oldDetail != null && !doorDetail.getAlreadyFetch().equals(oldDetail.getAlreadyFetch())) {
                        isModified = true;
                    }

                    if (oldDetail != null && !doorDetail.getThisMonthFetch().equals(oldDetail.getThisMonthFetch())) {
                        isModified = true;
                    }

                    if (isModified) {
                        //表示被人为修改过了
                        doorDetail.setIsModified("1");
                    }
                    royaltyDetails.add(doorDetail);
                }
            }

            BeanUtils.copyProperties(projectDetail, furnitureDetail);
            Double furnitureRoyalty = projectDetail.getFurnitureRoyalty();
            Double furnitureAlreadyFetch = projectDetail.getFurnitureAlreadyFetch();

            if (StringUtils.equals("2", isModify) && projectDetail.getFurnitureId() != null) {
                //表示删除
                furnitureDetail.setId(projectDetail.getFurnitureId());
                furnitureDetail.setEndTime(now);
                royaltyDetails.add(furnitureDetail);
            } else {
                if (furnitureRoyalty == null) {
                    furnitureRoyalty = 0d;
                    furnitureDetail.setTotalRoyalty(0L);
                } else {
                    furnitureRoyalty = furnitureRoyalty * 100;
                    furnitureDetail.setTotalRoyalty(new Long(furnitureRoyalty.longValue()));
                }

                if (furnitureAlreadyFetch == null) {
                    furnitureAlreadyFetch = 0d;
                    furnitureDetail.setAlreadyFetch(0L);
                } else {
                    furnitureAlreadyFetch = furnitureAlreadyFetch * 100;
                    furnitureDetail.setAlreadyFetch(new Long(furnitureAlreadyFetch.longValue()));
                }

                Double furnitureThisMonthFetch = furnitureRoyalty - furnitureAlreadyFetch;
                furnitureDetail.setThisMonthFetch(new Long(furnitureThisMonthFetch.longValue()));

                if (projectDetail.getFurnitureId() == null) {
                    furnitureDetail.setCreateEmployeeId(employeeId);
                    furnitureDetail.setStartTime(now);
                    furnitureDetail.setEndTime(TimeUtils.getForeverTime());
                    furnitureDetail.setIsModified(null);
                    furnitureDetail.setType("3");
                    if (StringUtils.equals("1", period + "")) {
                        furnitureDetail.setItem("25");
                    } else if (StringUtils.equals("2", period + "")) {
                        furnitureDetail.setItem("28");
                    } else if (StringUtils.equals("3", period + "")) {
                        furnitureDetail.setItem("31");
                    }
                    furnitureDetail.setIsModified(null);
                    createDetails.add(furnitureDetail);
                } else {
                    furnitureDetail.setId(projectDetail.getFurnitureId());

                    boolean isModified = false;
                    SalaryRoyaltyDetail oldDetail = this.getById(projectDetail.getFurnitureId());
                    if (oldDetail != null && !furnitureDetail.getTotalRoyalty().equals(oldDetail.getTotalRoyalty())) {
                        isModified = true;
                    }

                    if (oldDetail != null && !furnitureDetail.getAlreadyFetch().equals(oldDetail.getAlreadyFetch())) {
                        isModified = true;
                    }

                    if (oldDetail != null && !furnitureDetail.getThisMonthFetch().equals(oldDetail.getThisMonthFetch())) {
                        isModified = true;
                    }

                    if (isModified) {
                        //表示被人为修改过了
                        furnitureDetail.setIsModified("1");
                    }
                    royaltyDetails.add(furnitureDetail);
                }
            }
        });

        if (ArrayUtils.isNotEmpty(createDetails)) {
            this.saveBatch(createDetails);
        }

        if (ArrayUtils.isNotEmpty(royaltyDetails)) {
            this.updateBatchById(royaltyDetails);
        }

    }

    /**
     * 提交工程提成数据到审核员审核
     * @param projectRoyaltyDetails
     */
    @Override
    public void auditProjectRoyaltyDetails(List<ProjectRoyaltyDetailDTO> projectRoyaltyDetails) {
        if (ArrayUtils.isEmpty(projectRoyaltyDetails)) {
            return;
        }

        projectRoyaltyDetails.forEach(projectRoyaltyDetail -> {
            projectRoyaltyDetail.setAuditStatus("1");
        });

        this.saveProjectRoyaltyDetails(projectRoyaltyDetails);
    }

    /**
     * 确定新增后的查询
     * @param detail
     * @return
     */
    @Override
    public DesignRoyaltyDetailDTO afterCreateDesignDetail(DesignRoyaltyDetailDTO detail) {
        detail.setItemName(this.staticDataService.getCodeName("ROYALTY_ITEM", detail.getItem()));
        detail.setIsModified("0");
        detail.setAuditStatus("0");
        detail.setNodeCondition(this.staticDataService.getCodeName("NODE_CONDITION", detail.getOrderStatus()));
        Long employeeId = detail.getEmployeeId();
        Employee employee = this.employeeService.getById(employeeId);
        detail.setEmployeeStatusName(this.staticDataService.getCodeName("EMPLOYEE_STATUS", employee.getStatus()));
        EmployeeJobRole jobRole = this.employeeJobRoleService.queryLast(employeeId);
        detail.setJobRole(jobRole.getJobRole());
        detail.setOrgId(jobRole.getOrgId());
        detail.setJobGrade(jobRole.getJobGrade());
        detail.setJobRoleName(this.staticDataService.getCodeName("JOB_ROLE", jobRole.getJobRole()));
        detail.setJobGradeName(this.staticDataService.getCodeName("JOB_GRADE", jobRole.getJobGrade()));
        detail.setAuditStatusName(this.staticDataService.getCodeName("SALARY_AUDIT_STATUS", detail.getAuditStatus()));
        Long orgId = jobRole.getOrgId();
        Org org = this.orgService.getById(orgId);
        detail.setOrgName(org.getName());
        Long orderId = detail.getOrderId();
        OrderPlaneSketchDTO planeSketch = this.orderPlaneSketchService.getPlaneSketch(orderId);
        if (planeSketch != null) {
            detail.setDesignFeeStandard(planeSketch.getDesignFeeStandard());
        }
        return detail;
    }

    /**
     * 确定新增工程提成后的查询
     * @param detail
     * @return
     */
    @Override
    public ProjectRoyaltyDetailDTO afterCreateProjectDetail(ProjectRoyaltyDetailDTO detail) {
        detail.setIsModified("0");
        detail.setAuditStatus("0");
        detail.setNodeCondition(this.staticDataService.getCodeName("NODE_CONDITION", detail.getOrderStatus()));
        Long employeeId = detail.getEmployeeId();
        Employee employee = this.employeeService.getById(employeeId);
        detail.setEmployeeStatusName(this.staticDataService.getCodeName("EMPLOYEE_STATUS", employee.getStatus()));
        EmployeeJobRole jobRole = this.employeeJobRoleService.queryLast(employeeId);
        detail.setOrgId(jobRole.getOrgId());
        detail.setJobRoleName(this.staticDataService.getCodeName("JOB_ROLE", jobRole.getJobRole()));
        detail.setJobRole(jobRole.getJobRole());
        detail.setJobGrade(jobRole.getJobGrade());
        detail.setJobGradeName(this.staticDataService.getCodeName("JOB_GRADE", jobRole.getJobGrade()));
        detail.setAuditStatusName(this.staticDataService.getCodeName("SALARY_AUDIT_STATUS", detail.getAuditStatus()));
        Long orgId = jobRole.getOrgId();
        Org org = this.orgService.getById(orgId);
        detail.setOrgName(org.getName());

        SalaryStatusFeeMapping statusFeeMapping = this.salaryStatusFeeMappingService.getStatusFeeMapping(detail.getOrderStatus());
        Integer periods = statusFeeMapping.getPeriods();
        detail.setPeriodName(this.staticDataService.getCodeName("FEE_PERIODS", periods + ""));
        List<OrderFeeCompositeDTO> compositeFees = this.feeDomainService.buildCompositeFee(detail.getOrderId());
        OrderFeeCompositeDTO orderFee = this.find(compositeFees, "2", periods);
        if (orderFee == null) {
            throw new SalaryException(SalaryException.SalaryExceptionEnum.ORDER_FEE_NOT_FOUND, detail.getPeriodName());
        }

        Long basicFee = orderFee.getBasicFee();
        if (basicFee != null) {
            detail.setBasicFee(basicFee / 100d);
        } else {
            detail.setBasicFee(0d);
        }

        Long doorFee = orderFee.getDoorFee();
        if (doorFee != null) {
            detail.setDoorFee(doorFee / 100d);
        } else {
            detail.setDoorFee(0d);
        }

        Long furnitureFee = orderFee.getFurnitureFee();
        if (furnitureFee != null) {
            detail.setFurnitureFee(furnitureFee / 100d);
        } else {
            detail.setFurnitureFee(0d);
        }
        detail.setContractFee(orderFee.getContractFee());
        return detail;
    }

    /**
     * 审核时查询设计费提成明细
     * @param request
     * @return
     */
    @Override
    public IPage<DesignRoyaltyDetailDTO> queryAuditDesignRoyaltyDetails(QueryRoyaltyDetailDTO request) {
        IPage<DesignRoyaltyDetailDTO> result = new Page<>(request.getPage(), request.getLimit());
        QueryWrapper<QueryRoyaltyDetailDTO> designWrapper = new QueryWrapper<>();
        Integer currentPage = request.getPage();
        designWrapper.apply(" b.employee_id = a.employee_id ");
        designWrapper.apply(" d.order_id = a.order_id ");
        designWrapper.apply(" c.cust_id = d.cust_id ");
        designWrapper.like(StringUtils.isNotBlank(request.getCustName()), "c.cust_name", request.getCustName());
        designWrapper.eq(StringUtils.isNotBlank(request.getMobileNo()), "c.mobile_no", request.getMobileNo());
        designWrapper.eq(request.getHouseId() != null, "d.houses_id", request.getHouseId());
        designWrapper.eq(StringUtils.isNotBlank(request.getAuditStatus()), "a.audit_status", request.getAuditStatus());
        designWrapper.in("a.type", "1", "2");
        designWrapper.exists(StringUtils.isNotBlank(request.getOrgIds()), "(select 1 from ins_employee_job_role r where r.employee_id = a.employee_id and r.org_id in ("+request.getOrgIds()+"))");
        designWrapper.orderByAsc("a.employee_id", "a.order_status", "a.type", "a.id");

        IPage<QueryRoyaltyDetailDTO> designPage = new Page<>(currentPage, request.getLimit());
        IPage<OrderSalaryRoyaltyDetailDTO> pageDesignRoyaltyDetails = this.salaryRoyaltyDetailMapper.queryOrderSalaryRoyaltyDetailPages(designPage, designWrapper);

        List<OrderSalaryRoyaltyDetailDTO> designOrderRoyaltyDetails = pageDesignRoyaltyDetails.getRecords();
        if (ArrayUtils.isEmpty(designOrderRoyaltyDetails)) {
            return result;
        }

        List<Long> orderIds = new ArrayList<>();

        designOrderRoyaltyDetails.forEach(detail -> {
            Long orderId = detail.getOrderId();
            if (!orderIds.contains(orderId)) {
                orderIds.add(orderId);
            }
        });

        Map<Long, List<OrderFeeCompositeDTO>> feeCache = this.feeDomainService.buildMultiCompositeFee(orderIds);
        if (ArrayUtils.isNotEmpty(designOrderRoyaltyDetails)) {
            Map<Long, List<OrderSalaryRoyaltyDetailDTO>> detailCache = new HashMap<>();
            designOrderRoyaltyDetails.forEach(detail -> {
                Long orderId = detail.getOrderId();
                List<OrderSalaryRoyaltyDetailDTO> temp = new ArrayList<>();
                if (detailCache.containsKey(orderId)) {
                    temp = detailCache.get(orderId);
                } else {
                    detailCache.put(orderId, temp);
                }

                temp.add(detail);
            });

            List<DesignRoyaltyDetailDTO> designRoyaltyDetails = new ArrayList<>();

            List<OrderPlaneSketch> planeSketches = this.orderPlaneSketchService.queryByOrderIds(orderIds);
            detailCache.forEach((key, values) -> {
                values.forEach(detail -> {
                    OrderPlaneSketch planeSketch = this.findPlanSketch(planeSketches, detail.getOrderId());
                    DesignRoyaltyDetailDTO designDetail = this.buildDesignRoyaltyDetail(detail, feeCache.get(key), key, planeSketch);
                    if (designDetail != null) {
                        designRoyaltyDetails.add(designDetail);
                    }
                });
            });
            if (ArrayUtils.isNotEmpty(designRoyaltyDetails)) {
                result.setRecords(designRoyaltyDetails);
                result.setTotal(pageDesignRoyaltyDetails.getTotal());
                result.setSize(pageDesignRoyaltyDetails.getSize());
            }
        }
        return result;
    }

    /**
     * 审核时查询工程提成明细
     * @param request
     * @return
     */
    @Override
    public IPage<ProjectRoyaltyDetailDTO> queryAuditProjectRoyaltyDetails(QueryRoyaltyDetailDTO request) {
        QueryWrapper<QueryRoyaltyDetailDTO> projectWrapper = new QueryWrapper<>();
        projectWrapper.apply(" b.employee_id = a.employee_id ");
        projectWrapper.apply(" d.order_id = a.order_id ");
        projectWrapper.apply(" c.cust_id = d.cust_id ");
        projectWrapper.like(StringUtils.isNotBlank(request.getCustName()), "c.cust_name", request.getCustName());
        projectWrapper.eq(StringUtils.isNotBlank(request.getMobileNo()), "c.mobile_no", request.getMobileNo());
        projectWrapper.eq(request.getHouseId() != null, "d.houses_id", request.getHouseId());
        projectWrapper.eq(StringUtils.isNotBlank(request.getAuditStatus()), "a.audit_status", request.getAuditStatus());
        projectWrapper.in("a.type", "3");
        projectWrapper.exists(StringUtils.isNotBlank(request.getOrgIds()), "(select 1 from ins_employee_job_role r where r.employee_id = a.employee_id and r.org_id in ("+request.getOrgIds()+"))");
        projectWrapper.orderByAsc("a.employee_id", "a.order_status", "a.type", "a.id");

        IPage<QueryRoyaltyDetailDTO> projectPage = new Page<>(request.getProjectPage(), request.getProjectLimit() * 3);
        IPage<OrderSalaryRoyaltyDetailDTO> pageProjectRoyaltyDetails = this.salaryRoyaltyDetailMapper.queryOrderSalaryRoyaltyDetailPages(projectPage, projectWrapper);
        List<OrderSalaryRoyaltyDetailDTO> projectOrderRoyaltyDetails = pageProjectRoyaltyDetails.getRecords();
        if (ArrayUtils.isEmpty(projectOrderRoyaltyDetails)) {
            return null;
        }

        List<Long> orderIds = new ArrayList<>();

        projectOrderRoyaltyDetails.forEach(detail -> {
            Long orderId = detail.getOrderId();
            if (!orderIds.contains(orderId)) {
                orderIds.add(orderId);
            }
        });

        Map<Long, List<OrderFeeCompositeDTO>> feeCache = this.feeDomainService.buildMultiCompositeFee(orderIds);

        if (ArrayUtils.isNotEmpty(projectOrderRoyaltyDetails)) {
            Map<Long, List<OrderSalaryRoyaltyDetailDTO>> detailCache = new HashMap<>();

            projectOrderRoyaltyDetails.forEach(detail -> {
                Long orderId = detail.getOrderId();
                List<OrderSalaryRoyaltyDetailDTO> temp = new ArrayList<>();
                if (detailCache.containsKey(orderId)) {
                    temp = detailCache.get(orderId);
                } else {
                    detailCache.put(orderId, temp);
                }
                temp.add(detail);
            });
            List<ProjectRoyaltyDetailDTO> projectRoyaltyDetails = new ArrayList<>();
            detailCache.forEach((key, values) -> {
                List<ProjectRoyaltyDetailDTO> projectDetails = this.buildProjectRoyaltyDetails(values, feeCache.get(key));
                projectRoyaltyDetails.addAll(projectDetails);
            });

            if (ArrayUtils.isNotEmpty(projectRoyaltyDetails)) {
                IPage<ProjectRoyaltyDetailDTO> pages = new Page<>(request.getProjectPage(), request.getProjectLimit());
                pages.setRecords(projectRoyaltyDetails);
                pages.setTotal(pageProjectRoyaltyDetails.getTotal() / 3);
                pages.setSize(pageProjectRoyaltyDetails.getSize() / 3);
                return pages;
            }
        }
        return null;
    }

    /**
     * 查找设计费信息
     * @param planeSketches
     * @param orderId
     * @return
     */
    private OrderPlaneSketch findPlanSketch(List<OrderPlaneSketch> planeSketches, Long orderId) {
        if (ArrayUtils.isEmpty(planeSketches)) {
            return null;
        }

        for (OrderPlaneSketch planeSketch : planeSketches) {
            if (orderId.equals(planeSketch.getOrderId())) {
                return planeSketch;
            }
        }

        return null;
    }

    /**
     * 设计费提成明细审核通过
     * @param designDetails
     */
    @Override
    public void auditDesignRoyaltyPass(List<DesignRoyaltyDetailDTO> designDetails) {
        if (ArrayUtils.isEmpty(designDetails)) {
            return;
        }

        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();

        List<SalaryRoyaltyDetail> salaryRoyaltyDetails = new ArrayList<>();
        designDetails.forEach(detail -> {
            SalaryRoyaltyDetail salaryRoyaltyDetail = new SalaryRoyaltyDetail();
            salaryRoyaltyDetail.setId(detail.getId());
            salaryRoyaltyDetail.setAuditStatus("2");
            salaryRoyaltyDetail.setAuditEmployeeId(employeeId);
            salaryRoyaltyDetails.add(salaryRoyaltyDetail);
        });

        this.updateBatchById(salaryRoyaltyDetails);
    }

    /**
     * 设计费提成明细审核不通过
     * @param designDetails
     */
    @Override
    public void auditDesignRoyaltyNo(List<DesignRoyaltyDetailDTO> designDetails) {
        if (ArrayUtils.isEmpty(designDetails)) {
            return;
        }

        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();

        List<SalaryRoyaltyDetail> salaryRoyaltyDetails = new ArrayList<>();
        designDetails.forEach(detail -> {
            SalaryRoyaltyDetail salaryRoyaltyDetail = new SalaryRoyaltyDetail();
            salaryRoyaltyDetail.setId(detail.getId());
            salaryRoyaltyDetail.setAuditStatus("3");
            salaryRoyaltyDetail.setAuditEmployeeId(employeeId);
            salaryRoyaltyDetail.setAuditRemark(detail.getAuditRemark());
            salaryRoyaltyDetails.add(salaryRoyaltyDetail);
        });
        this.updateBatchById(salaryRoyaltyDetails);
    }

    /**
     * 工程提成明细审核通过
     * @param projectDetails
     */
    @Override
    public void auditProjectRoyaltyPass(List<ProjectRoyaltyDetailDTO> projectDetails) {
        if (ArrayUtils.isEmpty(projectDetails)) {
            return;
        }

        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();

        List<SalaryRoyaltyDetail> salaryRoyaltyDetails = new ArrayList<>();
        projectDetails.forEach(detail -> {
            if (detail.getBasicId() != null) {
                SalaryRoyaltyDetail salaryRoyaltyDetail = new SalaryRoyaltyDetail();
                salaryRoyaltyDetail.setId(detail.getBasicId());
                salaryRoyaltyDetail.setAuditStatus("2");
                salaryRoyaltyDetail.setAuditEmployeeId(employeeId);
                salaryRoyaltyDetails.add(salaryRoyaltyDetail);
            }

            if (detail.getDoorId() != null) {
                SalaryRoyaltyDetail salaryRoyaltyDetail = new SalaryRoyaltyDetail();
                salaryRoyaltyDetail.setId(detail.getDoorId());
                salaryRoyaltyDetail.setAuditStatus("2");
                salaryRoyaltyDetail.setAuditEmployeeId(employeeId);
                salaryRoyaltyDetails.add(salaryRoyaltyDetail);
            }

            if (detail.getFurnitureId() != null) {
                SalaryRoyaltyDetail salaryRoyaltyDetail = new SalaryRoyaltyDetail();
                salaryRoyaltyDetail.setId(detail.getFurnitureId());
                salaryRoyaltyDetail.setAuditStatus("2");
                salaryRoyaltyDetail.setAuditEmployeeId(employeeId);
                salaryRoyaltyDetails.add(salaryRoyaltyDetail);
            }
        });

        this.updateBatchById(salaryRoyaltyDetails);
    }

    /**
     * 工程提成明细审核不通过
     * @param projectDetails
     */
    @Override
    public void auditProjectRoyaltyNo(List<ProjectRoyaltyDetailDTO> projectDetails) {
        if (ArrayUtils.isEmpty(projectDetails)) {
            return;
        }

        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();

        List<SalaryRoyaltyDetail> salaryRoyaltyDetails = new ArrayList<>();
        projectDetails.forEach(detail -> {
            if (detail.getBasicId() != null) {
                SalaryRoyaltyDetail salaryRoyaltyDetail = new SalaryRoyaltyDetail();
                salaryRoyaltyDetail.setId(detail.getBasicId());
                salaryRoyaltyDetail.setAuditStatus("3");
                salaryRoyaltyDetail.setAuditRemark(detail.getAuditRemark());
                salaryRoyaltyDetail.setAuditEmployeeId(employeeId);
                salaryRoyaltyDetails.add(salaryRoyaltyDetail);
            }

            if (detail.getDoorId() != null) {
                SalaryRoyaltyDetail salaryRoyaltyDetail = new SalaryRoyaltyDetail();
                salaryRoyaltyDetail.setId(detail.getDoorId());
                salaryRoyaltyDetail.setAuditStatus("3");
                salaryRoyaltyDetail.setAuditRemark(detail.getAuditRemark());
                salaryRoyaltyDetail.setAuditEmployeeId(employeeId);
                salaryRoyaltyDetails.add(salaryRoyaltyDetail);
            }

            if (detail.getFurnitureId() != null) {
                SalaryRoyaltyDetail salaryRoyaltyDetail = new SalaryRoyaltyDetail();
                salaryRoyaltyDetail.setId(detail.getFurnitureId());
                salaryRoyaltyDetail.setAuditStatus("3");
                salaryRoyaltyDetail.setAuditRemark(detail.getAuditRemark());
                salaryRoyaltyDetail.setAuditEmployeeId(employeeId);
                salaryRoyaltyDetails.add(salaryRoyaltyDetail);
            }
        });
        this.updateBatchById(salaryRoyaltyDetails);
    }
}