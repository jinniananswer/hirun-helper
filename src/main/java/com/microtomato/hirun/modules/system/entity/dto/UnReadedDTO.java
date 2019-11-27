package com.microtomato.hirun.modules.system.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Steven
 * @date 2019-11-26
 */
@Data
public class UnReadedDTO {

    /**
     * Id
     */
    private Long id;

    /**
     * 内容
     */
    private String content;

    /**
     * Id
     */
    private Long senderId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 雇员姓名
     */
    private String name;

    /**
     * 是否已读
     */
    private Boolean readed;
}
