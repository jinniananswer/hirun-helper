package com.microtomato.hirun.modules.bi.middleproduct.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bi.middleproduct.entity.dto.PushDataStatisticDTO;
import com.microtomato.hirun.modules.bi.middleproduct.entity.po.MidprodSend;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeExampleDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (MidprodSend)表数据库访问层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-26 00:48:48
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface MidprodSendMapper extends BaseMapper<MidprodSend> {

    @Select("SELECT b.name,count(1) as pushNum,b.employee_id " +
            " FROM ins_midprod_send a ,ins_employee b" +
            " ${ew.customSqlSegment}")
    List<PushDataStatisticDTO> querySendByEmployee(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT b.name,count(1) as openNum,b.employee_id " +
            " FROM ins_midprod_send a ,ins_employee b , ins_midprod_open c " +
            " ${ew.customSqlSegment}")
    List<PushDataStatisticDTO> queryOpenByEmployee(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT b.name,count(a.shop_id) as pushNum,a.shop_id as org_id " +
            " FROM ins_org b left join ins_midprod_send a on (b.org_id=a.shop_id) " +
            " ${ew.customSqlSegment}")
    List<PushDataStatisticDTO> querySendByShop(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT b.name,count(1) as openNum,a.shop_id as org_id " +
            " FROM ins_midprod_send a ,ins_org b , ins_midprod_open c " +
            " ${ew.customSqlSegment}")
    List<PushDataStatisticDTO> queryOpenByShop(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT b.title as name,count(b.title) as pushNum " +
            " FROM ins_midprod_send b " +
            " ${ew.customSqlSegment}")
    List<PushDataStatisticDTO> queryTopPush(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT b.title as name ,count(b.title) as pushNum " +
            " FROM ins_midprod_open b " +
            " ${ew.customSqlSegment}")
    List<PushDataStatisticDTO> queryTopOpen(@Param(Constants.WRAPPER) Wrapper wrapper);
}