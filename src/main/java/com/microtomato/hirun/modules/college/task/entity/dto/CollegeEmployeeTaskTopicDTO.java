package com.microtomato.hirun.modules.college.task.entity.dto;


import com.microtomato.hirun.modules.college.topic.entity.dto.TopicServiceDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CollegeEmployeeTaskTopicDTO {

    private Long taskId;

    private int taskTimeLen;

    private List<TopicServiceDTO> taskTopics;
}
