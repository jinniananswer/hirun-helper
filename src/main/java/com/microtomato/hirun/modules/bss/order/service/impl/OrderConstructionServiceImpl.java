package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.ConstructionDTO;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderConstruction;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderConstructionParty;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayNo;
import com.microtomato.hirun.modules.bss.order.mapper.OrderConstructionMapper;
import com.microtomato.hirun.modules.bss.order.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.system.entity.po.StaticData;
import org.apache.commons.lang3.StringUtils;
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
 * 订单施工表 服务实现类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-04
 */
@Slf4j
@Service
public class OrderConstructionServiceImpl extends ServiceImpl<OrderConstructionMapper, OrderConstruction> implements IOrderConstructionService {
    @Autowired
    private IOrderWorkerService workerService;

    @Autowired
    private IOrderDomainService orderDomainService;

    @Autowired
    private IOrderConstructionPartyService orderConstructionPartyService;

    @Autowired
    private IDecoratorService decoratorService;

    /**
     * 分配任务保存
     *
     * @param dto
     */

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveAssignInfo(ConstructionDTO dto) {
        log.debug("ConstructionDTO==========" + dto);

        OrderConstruction orderConstruction = new OrderConstruction();
        orderConstruction.setOrderId(dto.getOrderId());
        orderConstruction.setEngineeringSupervisor(dto.getEngineeringSupervisor().toString());
        orderConstruction.setProjectManager(dto.getProjectManager().toString());
        orderConstruction.setStartDate(LocalDateTime.now());
        orderConstruction.setCreateTime(LocalDateTime.now());
        List<OrderConstruction> existsOrderConstruction = this.queryOrderConstructionByOrderId(dto.getOrderId());
        //新增
        if (ArrayUtils.isEmpty(existsOrderConstruction)) {
            orderConstruction.setConstructionStatus("0");//进入任务分配状态，暂定0状态
            this.save(orderConstruction);
        } else {
            this.update(new UpdateWrapper<OrderConstruction>().lambda().eq(OrderConstruction::getOrderId, dto.getOrderId()).eq(OrderConstruction::getConstructionStatus, '0').set(OrderConstruction::getEngineeringSupervisor, dto.getEngineeringSupervisor()).set(OrderConstruction::getProjectManager, dto.getProjectManager()).set(OrderConstruction::getUpdateTime, LocalDateTime.now()).set(OrderConstruction::getRemark, dto.getRemark()));

        }
    }

    /**
     * 项目经理审核保存
     *
     * @param dto
     */

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveProjectManagerInfo(ConstructionDTO dto) {
        log.debug("ConstructionDTO==========" + dto);

        OrderConstruction orderConstruction = new OrderConstruction();
        BeanUtils.copyProperties(dto, orderConstruction);
        this.update(new UpdateWrapper<OrderConstruction>().lambda().eq(OrderConstruction::getOrderId, dto.getOrderId()).set(OrderConstruction::getEngineeringAssistant, dto.getEngineeringAssistant()).set(OrderConstruction::getUpdateTime, LocalDateTime.now()).set(OrderConstruction::getRemark, dto.getRemark()));


    }

    /**
     * 开工交底保存
     *
     * @param dto
     */

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveCommencementInfo(ConstructionDTO dto) {
        log.debug("ConstructionDTO==========" + dto);
        OrderConstruction orderConstruction = new OrderConstruction();
        BeanUtils.copyProperties(dto, orderConstruction);
        this.update(new UpdateWrapper<OrderConstruction>().lambda().eq(OrderConstruction::getOrderId, dto.getOrderId())
                .set(OrderConstruction::getPlumberElectrician, dto.getPlumberAndElectrician()).set(OrderConstruction::getPlumberElectNum, dto.getPlumberElectNum())
                .set(OrderConstruction::getInlayer, dto.getInlayer()).set(OrderConstruction::getInlayerNum, dto.getInlayerNum())
                .set(OrderConstruction::getCarpentry, dto.getCarpentry()).set(OrderConstruction::getCarpentryNum, dto.getCarpentryNum())
                .set(OrderConstruction::getPaperhanger, dto.getPaperhanger()).set(OrderConstruction::getPaperhangerNum, dto.getPaperhangerNum())
                .set(OrderConstruction::getWallKnocking, dto.getWallKnocking()).set(OrderConstruction::getWallknockingNum, dto.getWallknockingNum())
                .set(OrderConstruction::getUpdateTime, LocalDateTime.now()).set(OrderConstruction::getRemark, dto.getRemark()));


    }


    /**
     * 工程文员提交项目经理审核
     *
     * @param dto
     */

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void submitTask(ConstructionDTO dto) {
        this.saveAssignInfo(dto);
        orderDomainService.orderStatusTrans(dto.getOrderId(), OrderConst.OPER_NEXT_STEP);
        //如果需要流转到指定人，才需要处理worker记录，流转到角色33项目经理
        workerService.updateOrderWorker(dto.getOrderId(), 33L, dto.getProjectManager());


    }

    /**
     * 项目经理审核
     *
     * @param dto
     */

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void submitAuditProject(ConstructionDTO dto) {
        String auditStatus = dto.getAuditStatus();//1是审核通过，2是审核不通过

        if (auditStatus.equals("1")) {
            this.saveProjectManagerInfo(dto);
            orderDomainService.orderStatusTrans(dto.getOrderId(), OrderConst.OPER_NEXT_STEP);
            //如果需要流转到指定人，才需要处理worker记录 流转到角色31项目助理
            workerService.updateOrderWorker(dto.getOrderId(), 31L, dto.getEngineeringAssistant());
        } else {
            orderDomainService.orderStatusTrans(dto.getOrderId(), OrderConst.OPER_AUDIT_NO);
        }

    }

    /**
     * 开工交底
     *
     * @param dto
     */

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void submitAssignment(ConstructionDTO dto) {
        this.saveCommencementInfo(dto);
        orderDomainService.orderStatusTrans(dto.getOrderId(), OrderConst.OPER_NEXT_STEP);


    }

    public List<OrderConstruction> queryOrderConstructionByOrderId(Long orderId) {
        return this.list(new QueryWrapper<OrderConstruction>().lambda()
                .eq(OrderConstruction::getOrderId, orderId));
    }


    /**
     * 查询工程信息
     *
     * @param orderId
     * @return
     */

    @Override
    public ConstructionDTO queryOrderConstruction(long orderId) {
        ConstructionDTO constructionData = new ConstructionDTO();
        List<OrderConstruction> existsOrderConstruction = this.queryOrderConstructionByOrderId(orderId);
        log.debug("existsOrderConstruction========="+existsOrderConstruction);
        if (ArrayUtils.isNotEmpty(existsOrderConstruction)) {
            log.debug("existsOrderConstruction===0000======"+existsOrderConstruction.get(0));
            BeanUtils.copyProperties(existsOrderConstruction.get(0), constructionData);
            constructionData.setEngineeringSupervisor(Long.parseLong(existsOrderConstruction.get(0).getEngineeringSupervisor()));
            constructionData.setProjectManager(Long.parseLong(existsOrderConstruction.get(0).getProjectManager()));
            if (StringUtils.isNotEmpty(existsOrderConstruction.get(0).getEngineeringAssistant())){
                constructionData.setEngineeringAssistant(Long.parseLong(existsOrderConstruction.get(0).getEngineeringAssistant()));
            }
            if (StringUtils.isNotEmpty(existsOrderConstruction.get(0).getPlumberElectrician())){
                constructionData.setPlumberAndElectrician(Long.parseLong(existsOrderConstruction.get(0).getPlumberElectrician()));
            }
            if (StringUtils.isNotEmpty(existsOrderConstruction.get(0).getInlayer())){
                constructionData.setInlayer(Long.parseLong(existsOrderConstruction.get(0).getInlayer()));
            }
            if (StringUtils.isNotEmpty(existsOrderConstruction.get(0).getCarpentry())){
                constructionData.setCarpentry(Long.parseLong(existsOrderConstruction.get(0).getCarpentry()));
            }
            if (StringUtils.isNotEmpty(existsOrderConstruction.get(0).getPaperhanger())){
                constructionData.setPaperhanger(Long.parseLong(existsOrderConstruction.get(0).getPaperhanger()));
            }
            if (StringUtils.isNotEmpty(existsOrderConstruction.get(0).getWallKnocking())){
                constructionData.setWallKnocking(Long.parseLong(existsOrderConstruction.get(0).getWallKnocking()));
            }

        }
        log.debug("constructionData========="+constructionData);
        return constructionData;
    }


}
