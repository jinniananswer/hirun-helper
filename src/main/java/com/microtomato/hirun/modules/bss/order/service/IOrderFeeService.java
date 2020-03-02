package com.microtomato.hirun.modules.bss.order.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.config.entity.dto.PayComponentDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.SecondInstallmentCollectionDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayNo;

/**
 * <p>
 * 订单费用表 服务类
 * </p>
 *
 * @author sunxin
 * @since 2020-02-05
 */
public interface IOrderFeeService extends IService<OrderFee> {

    /**
     * 查询已收取费用信息 初始化方法使用
     * @param orderId
     */
    OrderFee queryOrderCollectFee(Long orderId);

    /**
     * 审核费用信息
     * @param orderId
     */
    PayComponentDTO initCostAudit(Long orderId);

    /**
     * 审核费用信息
     * @param dto
     */
    void submitAudit(OrderFeeDTO dto);

    /**
     * 费用复审
     * @param orderPayNo
     */
    void costReview(OrderPayNo orderPayNo);

    public void secondInstallmentCollect(SecondInstallmentCollectionDTO dto);



    /**
     * 工程文员提交项目经理审核
     * @param dto
     */
    void submitTask(OrderFeeDTO dto);

    /**
     * 项目经理审核
     * @param dto
     */
    void submitAuditProject(OrderFeeDTO dto);

    /**
     * 开工交底
     * @param dto
     */
    void submitAssignment(OrderFeeDTO dto);

}
