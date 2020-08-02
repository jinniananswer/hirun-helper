package com.microtomato.hirun.modules.system.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 控制台待办任务DTO
 * @author: jinnian
 * @create: 2020-07-27 22:14
 **/
@Data
public class PendingTaskDTO {

    private String name;

    private String num;

    private String link;
}
