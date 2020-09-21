package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.dto.ConstructionDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.Decorator;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderConstruction;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单施工表 服务类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-04
 */
public interface IOrderConstructionService extends IService<OrderConstruction> {

    /**
     * 分配任务保存
     * @param dto
     */
    void saveAssignInfo(ConstructionDTO dto) ;
    /**
     * 项目经理审核保存
     * @param dto
     */
    void saveProjectManagerInfo(ConstructionDTO dto) ;
    /**
     * 开工交底保存
     * @param dto
     */
    void saveCommencementInfo(ConstructionDTO dto) ;

    /**
     * init方法查询工程信息
     * @param orderId
     * @return
     */
    ConstructionDTO queryOrderConstruction(long orderId) ;

    /**
     * 工程文员提交项目经理审核
     * @param dto
     */
    void submitTask(ConstructionDTO dto);

    /**
     * 项目经理审核
     * @param dto
     */
    void submitAuditProject(ConstructionDTO dto);

    /**
     * 开工交底
     * @param dto
     */
    void submitAssignment(ConstructionDTO dto);



}
