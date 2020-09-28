package com.microtomato.hirun.modules.bss.order.entity.dto.factory;

import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 编辑跟单信息数据对象
 * @author: jinnian
 * @create: 2020-09-26 23:29
 **/
@Data
public class FactoryOrderFollowDTO {

    private Long id;

    /** 工厂订单ID */
    private Long factoryOrderId;

    /** 跟单时间 */
    private LocalDate followDate;

    /** 性质 */
    private String nature;

    /** 内容 */
    private String content;

    /** 责任人 */
    private String responsible;

    /** 下单日期 */
    private LocalDate orderDate;

    /** 下单人 */
    private String orderMan;

    /** 送货日期 */
    private LocalDate deliverDate;

}
