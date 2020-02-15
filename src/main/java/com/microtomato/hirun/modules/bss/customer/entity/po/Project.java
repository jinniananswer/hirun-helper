package com.microtomato.hirun.modules.bss.customer.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * 
 * </p>
 *
 * @author liuhui
 * @since 2020-02-15
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_project")
public class Project extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "project_id", type = IdType.AUTO)
    private Long projectId;

    @TableField("party_id")
    private Long partyId;

    @TableField("house_mode")
    private String houseMode;

    @TableField("house_area")
    private String houseArea;

    @TableField("house_id")
    private Long houseId;

    @TableField("house_building")
    private String houseBuilding;

    @TableField("house_room_no")
    private String houseRoomNo;

    @TableField("house_address")
    private String houseAddress;

    @TableField("gauge_house_time")
    private String gaugeHouseTime;

    @TableField("offer_plane_time")
    private String offerPlaneTime;

    @TableField("contact_time")
    private String contactTime;

    @TableField("critical_process")
    private String criticalProcess;

    @TableField("other_info")
    private String otherInfo;

    @TableField("mw_experience_time")
    private String mwExperienceTime;

    @TableField("advantage")
    private String advantage;

    @TableField("is_scan_video")
    private String isScanVideo;

    @TableField("is_scan_showroom")
    private String isScanShowroom;

    @TableField("counselor_name")
    private String counselorName;

    @TableField("information_source")
    private String informationSource;

    @TableField("other_information_source")
    private String otherInformationSource;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
