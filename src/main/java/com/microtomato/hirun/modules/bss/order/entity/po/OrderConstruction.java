package com.microtomato.hirun.modules.bss.order.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
 * <p>
 * 订单施工表
 * </p>
 *
 * @author sunxin
 * @since 2020-03-04
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderConstruction extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 施工id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 工程主管
     */
    @TableField("engineering_supervisor")
    private String engineeringSupervisor;
    /**
     * 项目经理
     */
    @TableField("project_manager")
    private String projectManager;
    /**
     * 工程助理
     */
    @TableField("engineering_assistant")
    private String engineeringAssistant;

    /**
     * 水电组长
     */
    @TableField("plumber_electrician")
    private String plumberElectrician;
    /**
     * 镶贴组长
     */
    @TableField("inlayer")
    private String inlayer;
    /**
     * 木工组长
     */
    @TableField("carpentry")
    private String carpentry;
    /**
     * 涂裱组长
     */
    @TableField("paperhanger")
    private String paperhanger;
    /**
     * 敲墙组长
     */
    @TableField("wall_knocking")
    private String wallKnocking;

    /**
     * 施工类型
     */
    @TableField("construction_type")
    private String constructionType;

    /**
     * 施工状态，代表不同时期
     */
    @TableField("construction_status")
    private String constructionStatus;

    /**
     * 水电工人数
     */
    @TableField("plumber_elect_num")
    private String plumberElectNum;

    /**
     * 镶贴工人数
     */
    @TableField("inlayer_num")
    private String inlayerNum;

    /**
     * 木工人数
     */
    @TableField("carpentry_num")
    private String carpentryNum;

    /**
     * 涂裱工人数
     */
    @TableField("paperhanger_num")
    private String paperhangerNum;

    /**
     * 敲墙工人数
     */
    @TableField("wallknocking_num")
    private String wallknockingNum;

    /**
     * 进场时间
     */
    @TableField("start_date")
    private LocalDateTime startDate;

    /**
     * 离场时间
     */
    @TableField("end_date")
    private LocalDateTime endDate;

    /**
     * 归属分公司
     */
    @TableField("org_id")
    private Long orgId;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 创建员工
     */
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 更新员工
     */
    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


}
