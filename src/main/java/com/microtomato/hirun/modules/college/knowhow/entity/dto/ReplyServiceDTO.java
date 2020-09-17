package com.microtomato.hirun.modules.college.knowhow.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author huanghua
 * @date 2020-09-17
 */
@Data
@NoArgsConstructor
public class ReplyServiceDTO {

    /** 回复内容 */
    private String replyContent;

    /** 解答人标识 */
    private Long respondent;

    /** 回复时间 */
    private LocalDateTime replyTime;

    /** 解答人 */
    private String replyer;
}
