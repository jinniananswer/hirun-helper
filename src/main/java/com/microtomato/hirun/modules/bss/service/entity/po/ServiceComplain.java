package com.microtomato.hirun.modules.bss.service.entity.po;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.microtomato.hirun.framework.data.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * (ServiceComplain)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("service_complain")
public class ServiceComplain extends BaseEntity {

    private static final long serialVersionUID = 1L;
    


    @TableField(value = "id")
    private Long id;


    @TableField(value = "complain_no")
    private String complainNo;


    @TableField(value = "customer_id")
    private Long customerId;

    /** 投诉类型 */
    @TableField(value = "complain_type")
    private String complainType;

    /** 投诉方式 */
    @TableField(value = "complain_way")
    private String complainWay;


    @TableField(value = "complain_time")
    private LocalDateTime complainTime;

    /** 投诉内容 */
    @TableField(value = "complain_content")
    private String complainContent;


    @TableField(value = "accept_employee_id")
    private Long acceptEmployeeId;

    /** 处理结果 */
    @TableField(value = "deal_result")
    private String dealResult;

    /** 处理时间 */
    @TableField(value = "deal_date")
    private LocalDate dealDate;


    @TableField(value = "deal_employee_id")
    private Long dealEmployeeId;

    /** 跟踪情况 */
    @TableField(value = "follow_trace")
    private String followTrace;


    @TableField(value = "remark")
    private String remark;


    @TableField(value = "status")
    private String status;


    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}