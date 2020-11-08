package com.microtomato.hirun.modules.college.topic.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huanghua
 * @date 2020-09-13
 */
@Data
@NoArgsConstructor
public class TopicOptionServiceDTO {

    private Long topicId;

    private String name;

    private String symbol;

    private String status;
}
