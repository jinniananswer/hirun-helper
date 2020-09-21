package com.microtomato.hirun.modules.bss.order.entity.dto.finance;

import com.microtomato.hirun.modules.bss.order.entity.dto.OrderTaskDTO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 财务主营控制台数据对象
 * @author: jinnian
 * @create: 2020-07-11 16:37
 **/
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class FinanceOrderTaskDTO extends OrderTaskDTO {

    private Double totalMoney;

    private String auditStatus;

    private String auditStatusName;

    private Long payNo;

    private LocalDateTime payDate;
}
