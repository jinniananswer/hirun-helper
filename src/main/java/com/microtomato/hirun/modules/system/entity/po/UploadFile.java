package com.microtomato.hirun.modules.system.entity.po;

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
 * 上传文件
 * </p>
 *
 * @author Steven
 * @since 2020-02-05
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_upload_file")
public class UploadFile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 原件名称
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 服务器上文件路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 文件大小（Bytes）
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 是否有效
     */
    @TableField("enabled")
    private Boolean enabled;

    /**
     * employeeId
     */
    @TableField("create_employee_id")
    private Long createEmployeeId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新用户ID
     */
    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
