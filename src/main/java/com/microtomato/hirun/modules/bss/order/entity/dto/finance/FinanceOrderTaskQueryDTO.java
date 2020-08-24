package com.microtomato.hirun.modules.bss.order.entity.dto.finance;

import com.microtomato.hirun.modules.bss.order.entity.dto.OrderTaskQueryDTO;
import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 财务主营控制台查询条件DTO
 * @author: jinnian
 * @create: 2020-07-11 16:37
 **/
@Data
public class FinanceOrderTaskQueryDTO extends OrderTaskQueryDTO {

    private String auditStatus;
}
