package com.microtomato.hirun.modules.college.teacher.entity.po;

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
 * (Teacher)表实体类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-17 01:10:28
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_teacher")
public class Teacher extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "teacher_id", type = IdType.AUTO)
    private Long teacherId;


    @TableField(value = "id")
    private Long id;


    @TableField(value = "type")
    private String type;


    @TableField(value = "name")
    private String name;


    @TableField(value = "level")
    private String level;


    @TableField(value = "mobile_no")
    private String mobileNo;


    @TableField(value = "qq_no")
    private String qqNo;


    @TableField(value = "wechat_no")
    private String wechatNo;


    @TableField(value = "pic")
    private String pic;


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