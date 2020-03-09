package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.ConstructionDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.Decorator;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.order.service.IOrderConstructionService;

import java.util.List;

/**
 * <p>
 * 订单施工表 前端控制器
 * </p>
 *
 * @author sunxin
 * @since 2020-03-04
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-construction")
public class OrderConstructionController {

    @Autowired
    private IOrderConstructionService orderConstructionServiceImpl;


    @GetMapping("/queryOrderConstruction")
    @RestResult
    public ConstructionDTO queryOrderConstruction(Long orderId) {
        log.debug("orderId========="+orderId);
        return this.orderConstructionServiceImpl.queryOrderConstruction(orderId);
    }

    /**
     * 分配任务保存
     *
     * @param dto
     */
    @PostMapping("/saveAssignInfo")
    @RestResult
    public void saveAssignInfo(ConstructionDTO dto) {
        orderConstructionServiceImpl.saveAssignInfo(dto);
    }

    /**
     * 项目经理审核保存
     *
     * @param dto
     */
    @PostMapping("/saveProjectManagerInfo")
    @RestResult
    public void saveProjectManagerInfo(ConstructionDTO dto) {
        orderConstructionServiceImpl.saveProjectManagerInfo(dto);
    }


    /**
     * 开工交底保存
     *
     * @param dto
     */
    @PostMapping("/saveCommencementInfo")
    @RestResult
    public void saveCommencementInfo(ConstructionDTO dto) {
        orderConstructionServiceImpl.saveCommencementInfo(dto);
    }

    /**
     * 分配任务
     * @param dto
     */
    @PostMapping("/submitTask")
    @RestResult
    public void submitTask(ConstructionDTO dto) {
        orderConstructionServiceImpl.submitTask(dto);
    }

    /**
     * 项目经理审核
     * @param dto
     */
    @PostMapping("/submitAuditProject")
    @RestResult
    public void submitAuditProject(ConstructionDTO dto) {
        orderConstructionServiceImpl.submitAuditProject(dto);
    }

    /**
     * 开工交底
     * @param dto
     */
    @PostMapping("/submitAssignment")
    @RestResult
    public void submitAssignment(ConstructionDTO dto) {
        orderConstructionServiceImpl.submitAssignment(dto);
    }
}
