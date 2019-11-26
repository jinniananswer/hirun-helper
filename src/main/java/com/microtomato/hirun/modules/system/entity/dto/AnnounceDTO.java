package com.microtomato.hirun.modules.system.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Steven
 * @date 2019-11-26
 */
@Data
public class AnnounceDTO {
    private Long id;
    private String content;
    private Long senderId;
    private LocalDateTime createTime;
}
