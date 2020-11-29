package com.microtomato.hirun.modules.finance.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.mybatis.sequence.impl.VoucherNoCycleSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.DecoratorInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.Decorator;
import com.microtomato.hirun.modules.bss.order.service.IDecoratorService;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyOrder;
import com.microtomato.hirun.modules.bss.supply.mapper.SupplyOrderMapper;
import com.microtomato.hirun.modules.bss.supply.service.ISupplierService;
import com.microtomato.hirun.modules.bss.supply.service.ISupplyOrderService;
import com.microtomato.hirun.modules.finance.entity.dto.*;
import com.microtomato.hirun.modules.finance.entity.po.FinanceItem;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucher;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucherItem;
import com.microtomato.hirun.modules.finance.mapper.FinanceVoucherMapper;
import com.microtomato.hirun.modules.finance.service.IFinanceItemService;
import com.microtomato.hirun.modules.finance.service.IFinanceVoucherItemService;
import com.microtomato.hirun.modules.finance.service.IFinanceVoucherService;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 财务领款单表(FinanceVoucher)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-25 21:25:21
 */
@Service
@Slf4j
@DataSource(DataSourceKey.INS)
public class FinanceVoucherServiceImpl extends ServiceImpl<FinanceVoucherMapper, FinanceVoucher> implements IFinanceVoucherService {

    @Autowired
    private FinanceVoucherMapper financeVoucherMapper;

    @Autowired
    private IFinanceVoucherService financeVoucherService;

    @Autowired
    private IFinanceVoucherItemService financeVoucherItemService;

    @Autowired
    private SupplyOrderMapper supplyOrderMapper;

    @Autowired
    private ISupplyOrderService supplyOrderService;

    @Autowired
    private IDecoratorService decoratorServiceImpl;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private ISupplierService supplierService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IDualService dualService;

    @Autowired
    private IFinanceItemService financeItemService;

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleService;

    /**
     * 材料供应制单
     *
     * @param financeVoucherDetails
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void voucherPreparationForSupply(List<FinanceVoucherDTO> financeVoucherDetails) {
        if (ArrayUtils.isEmpty(financeVoucherDetails)) {
            return;
        }

        financeVoucherDetails.forEach(financeVoucherDetail -> {
            // financeVoucherDetail.setAuditStatus("1");
            Long supplyId = financeVoucherDetail.getSupplyId();
            financeVoucherDetail.setVoucherType("1");
            SupplyOrder supplyOrder = this.supplyOrderMapper.selectById(supplyId);
            Long orderId = supplyOrder.getOrderId();
//            List<FinanceVoucherItemDTO> financeVoucherItemDTOList = new ArrayList<>();
            //  for (SupplyMaterialDTO supplyDetail : supplyDetails) {
//            FinanceVoucherItemDTO financeVoucherItemDTO = new FinanceVoucherItemDTO();
            // financeVoucherItemDTO.setFee(supplyDetail.getFee());
            financeVoucherDetail.setParentVoucherItemId("-1");
            financeVoucherDetail.setVoucherItemId("503.01");
            financeVoucherDetail.setOrderId(orderId);
//            financeVoucherItemDTOList.add(financeVoucherItemDTO);
            //   }
            //将明细更新到对应dto里
//            financeVoucherDetail.setFinanceVoucherItemDTOList(financeVoucherItemDTOList);
        });

        this.voucherPreparation(financeVoucherDetails);
    }

    /**
     * 施工队制单
     *
     * @param financeVoucherDetails
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void voucherPreparationForConstruction(List<FinanceVoucherDTO> financeVoucherDetails) {
        if (ArrayUtils.isEmpty(financeVoucherDetails)) {
            return;
        }

        financeVoucherDetails.forEach(financeVoucherDetail -> {
            financeVoucherDetail.setVoucherType("2");
            FinanceVoucherItemDTO financeVoucherItemDTO = new FinanceVoucherItemDTO();
            financeVoucherDetail.setParentVoucherItemId("-1");
            financeVoucherDetail.setVoucherItemId("503.02");
            //将明细更新到对应dto里
//            financeVoucherDetail.setFinanceVoucherItemDTOList(financeVoucherItemDTOList);
        });

        this.voucherPreparation(financeVoucherDetails);
    }

    /**
     * 其他制单
     *
     * @param financeVoucherDetails
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void voucherPreparationForOther(List<FinanceVoucherDTO> financeVoucherDetails) {
        if (ArrayUtils.isEmpty(financeVoucherDetails)) {
            return;
        }

        financeVoucherDetails.forEach(financeVoucherDetail -> {
            financeVoucherDetail.setVoucherType("3");
            financeVoucherDetail.setParentVoucherItemId(financeVoucherDetail.getFinanceItemId());
            financeVoucherDetail.setVoucherItemId(financeVoucherDetail.getChildFinanceItemId());
        });

        this.voucherPreparation(financeVoucherDetails);
    }

    /**
     * 制单
     *
     * @param financeVoucherDetails
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void voucherPreparation(List<FinanceVoucherDTO> financeVoucherDetails) {
        if (ArrayUtils.isEmpty(financeVoucherDetails)) {
            return;
        }
        String voucherType = financeVoucherDetails.get(0).getVoucherType();
        Double totalMoney = 0d;
        for (FinanceVoucherDTO financeVoucherDetail : financeVoucherDetails) {
            totalMoney += financeVoucherDetail.getMoney();
        }
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        LocalDateTime createTime = RequestTimeHolder.getRequestTime();
        Long orgId = WebContextUtils.getUserContext().getOrgId();
        Long createUserId = WebContextUtils.getUserContext().getUserId();
        //材料下单根据不同供应商需要制作不同领款单
        if (StringUtils.equals(voucherType, "1")) {
            financeVoucherDetails.forEach(financeVoucherDetail -> {
                //拼finance_voucher表数据
                FinanceVoucher financeVoucher = new FinanceVoucher();
                financeVoucher.setAuditStatus("0");
                //需要从界面传递,暂时取对应制单员
                financeVoucher.setAuditEmployeeId(employeeId);
                financeVoucher.setCreateEmployeeId(employeeId);
                financeVoucher.setCreateTime(createTime);
                financeVoucher.setOrgId(orgId);
                financeVoucher.setTotalMoney(financeVoucherDetail.getMoney().longValue());
                financeVoucher.setVoucherDate(createTime.toLocalDate());
                financeVoucher.setStartDate(createTime);
                financeVoucher.setEndDate(TimeUtils.getForeverTime());
                financeVoucher.setVoucherEmployeeId(employeeId);
                financeVoucher.setItem(financeVoucherDetail.getVoucherType());
                financeVoucher.setRemark(financeVoucherDetail.getRemark());
                financeVoucherService.save(financeVoucher);
//                List<FinanceVoucherItemDTO> financeVoucherItemDTOList = financeVoucherDetail.getFinanceVoucherItemDTOList();
                //虽然与financeVoucher一一对应，因整个制单都是一套流程，故这还是启用循环
//                for (FinanceVoucherItemDTO financeVoucherItemDTO : financeVoucherItemDTOList) {
                FinanceVoucherItem financeVoucherItem = new FinanceVoucherItem();
                //拼finance_voucher_item表数据
                financeVoucherItem.setSupplierId(financeVoucherDetail.getSupplierId());
                financeVoucherItem.setSupplyId(financeVoucherDetail.getSupplyId());
                financeVoucherItem.setProjectId(financeVoucherDetail.getOrderId());
                financeVoucherItem.setParentFinanceItemId(financeVoucherDetail.getParentVoucherItemId());
                financeVoucherItem.setVoucherNo(financeVoucher.getId() + "");
                financeVoucherItem.setFinanceItemId(financeVoucherDetail.getVoucherItemId());
                financeVoucherItem.setCreateTime(createTime);
                financeVoucherItem.setCreateUserId(createUserId);
                financeVoucherItem.setStartDate(createTime);
                financeVoucherItem.setFee(financeVoucherDetail.getMoney().longValue());
                financeVoucherItem.setEndDate(TimeUtils.getForeverTime());
                //financeVoucherItem.setOrderId(financeVoucherDetail.getOrderId());
                financeVoucherItemService.save(financeVoucherItem);

                //制单后需要更新supply表状态
                SupplyOrder supplyOrder = new SupplyOrder();
                supplyOrder.setId(financeVoucherDetail.getSupplyId());
                supplyOrderService.updateById(supplyOrder);
//                }
            });

        }
        //施工队领款单
        if (StringUtils.equals(voucherType, "2")) {
            //拼finance_voucher表数据
            FinanceVoucher financeVoucher = new FinanceVoucher();
            financeVoucher.setAuditStatus("0");
            //需要从界面传递,暂时取对应制单员
            financeVoucher.setAuditEmployeeId(employeeId);
            financeVoucher.setCreateEmployeeId(employeeId);
            financeVoucher.setCreateTime(createTime);
            financeVoucher.setOrgId(orgId);
            financeVoucher.setTotalMoney(totalMoney.longValue());
            financeVoucher.setVoucherDate(createTime.toLocalDate());
            financeVoucher.setStartDate(createTime);
            financeVoucher.setEndDate(TimeUtils.getForeverTime());
            financeVoucher.setVoucherEmployeeId(employeeId);
            financeVoucher.setItem(voucherType);
            financeVoucherService.save(financeVoucher);

            financeVoucherDetails.forEach(financeVoucherDetail -> {
                //拼finance_voucher_item表数据
                FinanceVoucherItem financeVoucherItem = new FinanceVoucherItem();
                financeVoucherItem.setProjectId(financeVoucherDetail.getDecoratorId());
                financeVoucherItem.setParentFinanceItemId(financeVoucherDetail.getParentVoucherItemId());
                financeVoucherItem.setVoucherNo(financeVoucher.getId() + "");
                financeVoucherItem.setFinanceItemId(financeVoucherDetail.getVoucherItemId());
                financeVoucherItem.setCreateTime(createTime);
                financeVoucherItem.setCreateUserId(createUserId);
                financeVoucherItem.setStartDate(createTime);
                financeVoucherItem.setFee(financeVoucherDetail.getMoney().longValue());
                financeVoucherItem.setEndDate(TimeUtils.getForeverTime());
                // financeVoucherItem.setOrderId(financeVoucherDetail.getOrderId());
                financeVoucherItemService.save(financeVoucherItem);
            });
        }
        //其他领款单
        if (StringUtils.equals(voucherType, "3")) {
            //拼finance_voucher表数据
            FinanceVoucher financeVoucher = new FinanceVoucher();
            financeVoucher.setAuditStatus("0");
            //需要从界面传递,暂时取对应制单员
            financeVoucher.setAuditEmployeeId(employeeId);
            financeVoucher.setCreateEmployeeId(employeeId);
            financeVoucher.setCreateTime(createTime);
            financeVoucher.setOrgId(orgId);
            financeVoucher.setTotalMoney(totalMoney.longValue());
            financeVoucher.setVoucherDate(createTime.toLocalDate());
            financeVoucher.setStartDate(createTime);
            financeVoucher.setEndDate(TimeUtils.getForeverTime());
            financeVoucher.setVoucherEmployeeId(employeeId);
            financeVoucher.setType("3");
            financeVoucherService.save(financeVoucher);
            financeVoucherDetails.forEach(financeVoucherDetail -> {
                //拼finance_voucher_item表数据
                FinanceVoucherItem financeVoucherItem = new FinanceVoucherItem();
                // financeVoucherItem.setProjectId(financeVoucherDetail.getDecoratorId());
                financeVoucherItem.setParentFinanceItemId(financeVoucherDetail.getParentVoucherItemId());
                financeVoucherItem.setVoucherNo(financeVoucher.getId() + "");
                financeVoucherItem.setFinanceItemId(financeVoucherDetail.getVoucherItemId());
                financeVoucherItem.setCreateTime(createTime);
                financeVoucherItem.setCreateUserId(createUserId);
                financeVoucherItem.setStartDate(createTime);
                financeVoucherItem.setFee(financeVoucherDetail.getMoney().longValue());
                financeVoucherItem.setEndDate(TimeUtils.getForeverTime());
                //  financeVoucherItem.setOrderId(financeVoucherDetail.getOrderId());
                financeVoucherItemService.save(financeVoucherItem);
            });
        }

    }

    /**
     * 查询对应工人
     *
     * @return
     */
    @Override
    public List<DecoratorInfoDTO> selectDecorator(DecoratorInfoDTO decoratorInfoDTO) {
        List<Decorator> decorators = decoratorServiceImpl.queryAllInfo();

        List<DecoratorInfoDTO> decoratorInfoDTOArrayList = new ArrayList<>();
        for (Decorator decorator : decorators) {
            DecoratorInfoDTO decoratorInfoDTOs = new DecoratorInfoDTO();
            decoratorInfoDTOs.setName(decorator.getName());
            decoratorInfoDTOs.setDecoratorId(decorator.getDecoratorId());
            decoratorInfoDTOs.setDecoratorType(decorator.getDecoratorType());
            decoratorInfoDTOs.setDecoratorTypeName(this.staticDataService.getCodeName("DECORATOR_TYPE", decorator.getDecoratorType()));
            decoratorInfoDTOArrayList.add(decoratorInfoDTOs);
        }
        return decoratorInfoDTOArrayList;
    }

    /**
     * 供应订单查询
     *
     * @param condition
     * @return
     */
    @Override
    public IPage<FinanceVoucherDTO> queryVoucherSupplyInfo(QueryVoucherAuditDTO condition) {

        UserContext userContext = WebContextUtils.getUserContext();
        Long employeeId = userContext.getEmployeeId();
        IPage<QueryVoucherAuditDTO> page = new Page<>(condition.getPage(), condition.getLimit());
        IPage<FinanceVoucherDTO> pageVoucherSupplys = this.financeVoucherMapper.queryVoucherInfo(page, "1", "0");

        List<FinanceVoucherDTO> voucherSupplys  = pageVoucherSupplys.getRecords();
        voucherSupplys.forEach(voucherSupply -> {
            String supplierName = this.supplierService.querySupplierById(voucherSupply.getSupplierId()).getName();
            voucherSupply.setSupplierName(supplierName);
            voucherSupply.setVoucherId(voucherSupply.getId());
//            String createUserName = this.employeeService.queryByUserId(voucherSupply.getCreateUserId()).getName();
//            voucherSupply.setCreateUserName(createUserName);
//            voucherSupply.setSupplyOrderName(this.staticDataService.getCodeName("SUPPLY_ORDER_TYPE", supplyOrder.getSupplyOrderType()));
        });


        return pageVoucherSupplys;
    }

    /**
     * 审核通过
     *
     * @param financeVoucherDTOs
     */
    @Override
    public void auditForSupplyPass(List<FinanceVoucherDTO> financeVoucherDTOs) {
        if (ArrayUtils.isEmpty(financeVoucherDTOs)) {
            return;
        }
        financeVoucherDTOs.forEach(financeVoucherDTO -> {
            //拼supplyOrder表数据
            FinanceVoucher financeVoucher = new FinanceVoucher();
            financeVoucher.setId(financeVoucherDTO.getVoucherId());
            financeVoucher.setAuditStatus("1");//审核通过
            financeVoucher.setAuditEmployeeId(WebContextUtils.getUserContext().getEmployeeId());
            financeVoucher.setAuditComment(financeVoucherDTO.getRemark());
            financeVoucherService.updateById(financeVoucher);
        });


    }
    /**
     * 审核不通过
     *
     * @param financeVoucherDTOs
     */
    @Override
    public void auditForSupplyReject(List<FinanceVoucherDTO> financeVoucherDTOs) {
        if (ArrayUtils.isEmpty(financeVoucherDTOs)) {
            return;
        }
        financeVoucherDTOs.forEach(financeVoucherDTO -> {
            //拼supplyOrder表数据
            FinanceVoucher financeVoucher = new FinanceVoucher();
            financeVoucher.setId(financeVoucherDTO.getVoucherId());
            financeVoucher.setAuditStatus("2");//审核不通过
            financeVoucher.setAuditEmployeeId(WebContextUtils.getUserContext().getEmployeeId());
            financeVoucher.setAuditComment(financeVoucherDTO.getRemark());
            financeVoucherService.updateById(financeVoucher);
        });


    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void createVoucher(VoucherDTO request) {
        if (StringUtils.isBlank(request.getVoucherNo())) {
            String voucherNo = "QT" + this.dualService.nextval(VoucherNoCycleSeq.class);
            request.setVoucherNo(voucherNo);
        }

        LocalDateTime now = RequestTimeHolder.getRequestTime();
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();

        FinanceVoucher voucher = new FinanceVoucher();
        BeanUtils.copyProperties(request, voucher);
        voucher.setStartDate(now);

        Long fee = new Long(Math.round(request.getTotalMoney() * 100));
        voucher.setTotalMoney(fee);
        voucher.setEndDate(TimeUtils.getForeverTime());
        voucher.setAuditStatus("0");
        //财务其它制单
        if (StringUtils.isBlank(voucher.getType())) {
            voucher.setType("3");
        }

        if (StringUtils.isBlank(voucher.getItem())) {
            voucher.setItem("-1");
        }
        voucher.setCreateEmployeeId(employeeId);
        voucher.setVoucherEmployeeId(employeeId);

        EmployeeJobRole jobRole = this.employeeJobRoleService.queryLast(employeeId);
        if (jobRole != null) {
            voucher.setOrgId(jobRole.getOrgId());
        }

        List<VoucherItemDTO> voucherItemDatas = request.getVoucherItems();
        List<FinanceVoucherItem> voucherItems = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(voucherItemDatas)) {
            for (VoucherItemDTO voucherItemData : voucherItemDatas) {
                Long money = new Long(Math.round(voucherItemData.getFee() * 100));

                FinanceVoucherItem voucherItem = new FinanceVoucherItem();
                BeanUtils.copyProperties(voucherItemData, voucherItem);
                voucherItem.setFee(money);
                voucherItem.setVoucherNo(request.getVoucherNo());
                voucherItem.setStartDate(now);
                voucherItem.setEndDate(TimeUtils.getForeverTime());

                String financeItemId = voucherItemData.getFinanceItemId();
                if (StringUtils.isNotBlank(financeItemId)) {
                    FinanceItem financeItem = this.financeItemService.getByFinanceItemId(financeItemId);
                    if (financeItem != null) {
                        voucherItem.setParentFinanceItemId(financeItem.getParentFinanceItemId());
                    }
                }

                Long orgId = voucherItemData.getOrgId();
                if (orgId != null) {
                    OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
                    Org shop = orgDO.getBelongShop();
                    if (shop != null) {
                        voucherItem.setShopId(shop.getOrgId());
                    }
                }

                if (StringUtils.equals(voucher.getItem(), "5")) {
                    //差旅
                    Long trafficFee = 0L;
                    if (voucherItemData.getTrafficFee() != null) {
                        trafficFee = new Long(Math.round(voucherItemData.getTrafficFee() * 100));
                    }

                    Long allowance = 0L;
                    if (voucherItemData.getAllowance() != null) {
                        allowance = new Long(Math.round(voucherItemData.getAllowance() * 100));
                    }

                    Long hotelFee = 0L;
                    if (voucherItemData.getHotelFee() != null) {
                        hotelFee = new Long(Math.round(voucherItemData.getHotelFee() * 100));
                    }

                    voucherItem.setTrafficFee(trafficFee);
                    voucherItem.setAllowance(allowance);
                    voucherItem.setHotelFee(hotelFee);
                }

                voucherItems.add(voucherItem);
            }
        }

        this.save(voucher);

        if (ArrayUtils.isNotEmpty(voucherItems)) {
            this.financeVoucherItemService.saveBatch(voucherItems);
        }
    }

    @Override
    public IPage<VoucherResultDTO> queryReviewVouchers(QueryVoucherDTO request) {
        IPage<FinanceVoucher> page = new Page<>(request.getPage(), request.getLimit());
        String[] voucherDates = request.getVoucherDate();
        LocalDate startDate = null;
        LocalDate endDate = null;
        if (ArrayUtils.isNotEmpty(voucherDates)) {
            startDate = TimeUtils.stringToDate(voucherDates[0], TimeUtils.DATE_FMT_3);
            endDate = TimeUtils.stringToDate(voucherDates[1], TimeUtils.DATE_FMT_3);
        }

        LocalDateTime now = RequestTimeHolder.getRequestTime();
        Wrapper<FinanceVoucher> wrapper = Wrappers.<FinanceVoucher>lambdaQuery()
                .eq(StringUtils.isNotBlank(request.getType()), FinanceVoucher::getType, request.getType())
                .eq(request.getVoucherEmployeeId() != null, FinanceVoucher::getVoucherEmployeeId, request.getVoucherEmployeeId())
                .between(ArrayUtils.isNotEmpty(voucherDates), FinanceVoucher::getVoucherDate, startDate, endDate)
                .eq(StringUtils.isNotBlank(request.getAuditStatus()), FinanceVoucher::getAuditStatus, request.getAuditStatus())
                .ge(FinanceVoucher::getEndDate, now)
                .le(FinanceVoucher::getStartDate, now)
                .orderByDesc(FinanceVoucher::getVoucherDate);
        IPage<FinanceVoucher> vouchers = this.page(page, wrapper);

        IPage<VoucherResultDTO> resultPage = new Page<>();
        resultPage.setCurrent(page.getCurrent());
        resultPage.setPages(page.getPages());
        resultPage.setTotal(page.getTotal());
        resultPage.setSize(page.getSize());

        if (ArrayUtils.isEmpty(vouchers.getRecords())) {
            return resultPage;
        }

        List<VoucherResultDTO> result = new ArrayList<>();
        List<FinanceVoucher> records = vouchers.getRecords();
        for (FinanceVoucher voucher : records) {
            VoucherResultDTO voucherResult = new VoucherResultDTO();
            BeanUtils.copyProperties(voucher, voucherResult);

            if (StringUtils.isNotBlank(voucher.getItem())) {
                voucherResult.setItemName(this.staticDataService.getCodeName("VOUCHER_ITEM", voucher.getItem()));
            }

            if (voucher.getVoucherEmployeeId() != null) {
                String name = this.employeeService.getEmployeeNameEmployeeId(voucher.getVoucherEmployeeId());
                voucherResult.setVoucherEmployeeName(name);
            }

            if (voucher.getAuditEmployeeId() != null) {
                String name = this.employeeService.getEmployeeNameEmployeeId(voucher.getVoucherEmployeeId());
                voucherResult.setAuditEmployeeName(name);
            }

            if (voucher.getCashierEmployeeId() != null) {
                String name = this.employeeService.getEmployeeNameEmployeeId(voucher.getCashierEmployeeId());
                voucherResult.setCashierEmployeeName(name);
            }

            voucherResult.setTypeName(this.staticDataService.getCodeName("VOUCHER_TYPE", voucherResult.getType()));
            voucherResult.setAuditStatusName(this.staticDataService.getCodeName("VOUCHER_AUDIT_STATUS", voucherResult.getAuditStatus()));

            Long money = voucher.getTotalMoney();
            if (money != null) {
                voucherResult.setTotalMoney(new Double(money / 100d));
            }
            result.add(voucherResult);
        }

        resultPage.setRecords(result);
        return resultPage;
    }

    @Override
    public FinanceVoucher getByVoucherNo(String voucherNo) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.getOne(Wrappers.<FinanceVoucher>lambdaQuery()
                .eq(FinanceVoucher::getVoucherNo, voucherNo)
                .ge(FinanceVoucher::getEndDate, now), false);
    }

    /**
     * 复核
     * @param datas
     * @param pass
     */
    @Override
    public void review(List<VoucherResultDTO> datas, boolean pass) {
        if (ArrayUtils.isEmpty(datas)) {
            return;
        }

        List<FinanceVoucher> vouchers = new ArrayList<>();
        for (VoucherResultDTO data : datas) {
            FinanceVoucher voucher = new FinanceVoucher();
            voucher.setId(data.getId());
            if (pass) {
                voucher.setAuditStatus("1");
            } else {
                voucher.setAuditStatus("2");
            }
            vouchers.add(voucher);
        }

        this.updateBatchById(vouchers);
    }

    /**
     * 财务审核
     * @param datas
     * @param pass
     */
    @Override
    public void audit(List<VoucherResultDTO> datas, boolean pass) {
        if (ArrayUtils.isEmpty(datas)) {
            return;
        }

        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        List<FinanceVoucher> vouchers = new ArrayList<>();
        for (VoucherResultDTO data : datas) {
            FinanceVoucher voucher = new FinanceVoucher();
            voucher.setId(data.getId());
            if (pass) {
                voucher.setAuditStatus("3");
            } else {
                voucher.setAuditStatus("4");
            }
            voucher.setAuditEmployeeId(employeeId);
            vouchers.add(voucher);
        }

        this.updateBatchById(vouchers);
    }

    /**
     * 单个财务审核不通过
     * @param id
     * @param auditComment
     */
    @Override
    public void auditSingleNo(Long id, String auditComment) {
        FinanceVoucher voucher = new FinanceVoucher();
        voucher.setId(id);
        voucher.setAuditStatus("4");
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        voucher.setAuditEmployeeId(employeeId);
        voucher.setAuditComment(auditComment);
        this.updateById(voucher);
    }

    @Override
    public void handVoucher(Long voucherId) {
        FinanceVoucher voucher = new FinanceVoucher();
        voucher.setId(voucherId);
        voucher.setAuditStatus("6");

        this.updateById(voucher);
    }

    @Override
    public void receiveVoucher(Long voucherId, boolean pass) {
        FinanceVoucher voucher = new FinanceVoucher();
        voucher.setId(voucherId);
        if (pass) {
            voucher.setAuditStatus("7");
        } else {
            voucher.setAuditStatus("8");
        }

        this.updateById(voucher);
    }

    @Override
    public void deleteVouchers(List<VoucherResultDTO> datas) {
        if (ArrayUtils.isEmpty(datas)) {
            return;
        }

        for (VoucherResultDTO data : datas) {
            FinanceVoucher voucher = new FinanceVoucher();
            LocalDateTime now = RequestTimeHolder.getRequestTime();
            voucher.setId(data.getId());
            voucher.setEndDate(now);

            this.updateById(voucher);
            this.financeVoucherItemService.deleteItems(data.getVoucherNo());
        }
    }

    @Override
    public VoucherResultDTO getVoucher(Long id) {
        FinanceVoucher voucher = this.getById(id);

        if (voucher == null) {
            return null;
        }

        VoucherResultDTO voucherResult = new VoucherResultDTO();
        BeanUtils.copyProperties(voucher, voucherResult);

        if (voucher.getVoucherEmployeeId() != null) {
            String name = this.employeeService.getEmployeeNameEmployeeId(voucher.getVoucherEmployeeId());
            voucherResult.setVoucherEmployeeName(name);
        }

        if (voucher.getAuditEmployeeId() != null) {
            String name = this.employeeService.getEmployeeNameEmployeeId(voucher.getVoucherEmployeeId());
            voucherResult.setAuditEmployeeName(name);
        }
        voucherResult.setAuditStatusName(this.staticDataService.getCodeName("VOUCHER_AUDIT_STATUS", voucherResult.getAuditStatus()));

        Long money = voucher.getTotalMoney();
        if (money != null) {
            voucherResult.setTotalMoney(new Double(money / 100d));
        }

        String voucherNo = voucherResult.getVoucherNo();
        List<VoucherItemResultDTO> voucherItems = this.financeVoucherItemService.queryByVoucherNo(voucherNo);
        if (ArrayUtils.isNotEmpty(voucherItems)) {
            voucherResult.setVoucherItems(voucherItems);
        }

        return voucherResult;
    }

    @Override
    public void updatePay(String voucherNo, String auditStatus, Long cashierEmployeeId, LocalDate payDate) {
        FinanceVoucher voucher = this.getByVoucherNo(voucherNo);
        if (voucher == null) {
            return;
        }

        voucher.setCashierEmployeeId(cashierEmployeeId);
        voucher.setPayDate(payDate);
        voucher.setAuditStatus(auditStatus);
        this.updateById(voucher);
    }
}