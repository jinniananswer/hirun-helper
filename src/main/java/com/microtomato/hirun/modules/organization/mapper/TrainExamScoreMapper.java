package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.microtomato.hirun.modules.organization.entity.dto.TrainScoreDTO;
import com.microtomato.hirun.modules.organization.entity.po.TrainExamScore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2019-12-17
 */
@Storage
@DS("ins")
public interface TrainExamScoreMapper extends BaseMapper<TrainExamScore> {

    @Select("select train_id, item, max(score) score " +
            "from ins_train_exam_score " +
            "where employee_id = #{employeeId} " +
            "group by train_id, item "
    )
    List<TrainScoreDTO> queryScoreByEmployeeId(Long employeeId);
}
