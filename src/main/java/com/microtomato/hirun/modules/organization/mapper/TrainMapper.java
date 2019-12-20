package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.organization.entity.dto.TrainDTO;
import com.microtomato.hirun.modules.organization.entity.po.Train;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
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
@DataSource(DataSourceKey.INS)
public interface TrainMapper extends BaseMapper<Train> {

    @Select("select a.train_id, b.employee_id,  a.name, a.type, a.start_date,a.end_date " +
             "from ins_train a, ins_train_sign b " +
             "where b.train_id = a.train_id " +
             "and a.status = '0' " +
             "and a.sign_status = '1' " +
             "and b.status = '0' " +
             "and b.employee_id = #{employeeId} "
    )
    @Results(
            value = {
                    @Result(property = "trainId",
                            column = "train_id"),
                    @Result(property = "courseName",
                            column = "train_id",
                            many = @Many(
                                    select = "com.microtomato.hirun.modules.organization.mapper.CourseMapper.queryCourseNameByTrainId"
                            ))
            }
    )
    List<TrainDTO> queryByEmployeeId(Long employeeId);
}
