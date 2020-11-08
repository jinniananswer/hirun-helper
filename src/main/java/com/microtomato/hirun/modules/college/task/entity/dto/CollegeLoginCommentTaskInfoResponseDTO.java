package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

import java.util.List;

@Data
public class CollegeLoginCommentTaskInfoResponseDTO {

    private List<CollegeTaskCommentDetailResponseDTO> noComment;

    private List<CollegeTaskCommentDetailResponseDTO> finishComment;
}
