package com.microtomato.hirun.modules.college.config.entity.po;

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
 * (CollegeTopicLabelCfg)表实体类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-20 01:28:11
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_topic_label_cfg")
public class CollegeTopicLabelCfg extends BaseEntity {

    private static final long serialVersionUID = 1L;
    

    /** 标签标识 */
    @TableField(value = "label_id")
    private Long labelId;

    /** 标签名称 */
    @TableField(value = "label_name")
    private String labelName;

    /** 标签描述 */
    @TableField(value = "label_desc")
    private String labelDesc;

    /** 状态 */
    @TableField(value = "status")
    private String status;

}