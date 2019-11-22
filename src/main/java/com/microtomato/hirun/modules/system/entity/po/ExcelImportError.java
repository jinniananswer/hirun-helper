package com.microtomato.hirun.modules.system.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.microtomato.hirun.framework.data.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 导入异常数据
 * </p>
 *
 * @author Steven
 * @since 2019-11-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExcelImportError extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 批次号（UUID）
     */
    @TableField("batch_id")
    private String batchId;

    /**
     * 异常数据
     */
    @TableField("row_content")
    private String rowContent;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;


}
