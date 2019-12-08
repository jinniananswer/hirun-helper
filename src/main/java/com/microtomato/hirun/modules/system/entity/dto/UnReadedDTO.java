package com.microtomato.hirun.modules.system.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Steven
 * @date 2019-11-26
 */
@Data
@Builder
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
     * 消息类型（1: 公告 announce，2: 提醒 remind，3：信息 message）
     */
    private Integer notifyType;

    /**
     * 消息类型描述
     */
    private String notifyTypeDesc;

    /**
     * Id
     */
    private Long senderId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
