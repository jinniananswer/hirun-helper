package com.microtomato.hirun.modules.bss.order.entity.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author huanghua
 * @date 2020-09-14
 */
@Data
@NoArgsConstructor
public class DecoratorServiceDTO {
    private Long decoratorId;

    private String name;

    private String identityNo;

    private String mobileNo;

    private String decoratorType;

    private String agreementTag;

    private String field1;

    private String status;

    private String remark;

    private Long orgId;
}
