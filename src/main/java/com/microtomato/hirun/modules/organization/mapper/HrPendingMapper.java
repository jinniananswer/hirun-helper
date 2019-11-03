package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.dto.HrPendingInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.HrPending;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2019-10-22
 */
@Mapper
@DS("ins")
public interface HrPendingMapper extends BaseMapper<HrPending> {
    /**
     * 查询执行人ID待办信息
     *
     * @param page
     * @param wrapper
     * @return
     */
    @Select("select * " + " from ins_hr_pending a \n" +
            " ${ew.customSqlSegment}"
    )
    IPage<HrPendingInfoDTO> queryPendingByExecuteId(Page<HrPending> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 需待办员工ID查询待办信息
     *
     * @param page
     * @param wrapper
     * @return
     */
    @Select("select * " + " from ins_hr_pending a \n" +
            " ${ew.customSqlSegment}"
    )
    IPage<HrPendingInfoDTO> queryTransPendingByEmployeeId(Page<HrPending> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
