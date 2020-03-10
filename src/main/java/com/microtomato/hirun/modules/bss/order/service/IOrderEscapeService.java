package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.dto.OrderEscapeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderEscape;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-14
 */
public interface IOrderEscapeService extends IService<OrderEscape> {
    /**
     * 保存数据
     * @param orderEscape
     */
    void saveOrderEscape(OrderEscape orderEscape);

    /**
     * 查询跑单信息
     * @param orderId
     * @return
     */
    OrderEscapeDTO getEscapeInfo(Long orderId);

    /**
     * 提交主管审核
     * @param orderEscape
     */
    void submitDirectorAudit(OrderEscape orderEscape);

    /**
     * 非跑单，返回上一个节点
     * @param orderEscape
     */
    void submitBack(OrderEscape orderEscape);

    /**
     * 关闭单据
     * @param orderEscape
     */
    void closeOrder(OrderEscape orderEscape);

    /**
     * 审核不通过，返回上一个节点
     * @param orderEscape
     */
    void auditBack(OrderEscape orderEscape);

}
