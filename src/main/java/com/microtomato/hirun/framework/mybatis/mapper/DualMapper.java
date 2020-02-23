package com.microtomato.hirun.framework.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.po.Dual;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author Steven
 * @date 2020-02-07
 */
@Storage(DataSourceKey.SYS)
public interface DualMapper extends BaseMapper<Dual> {

    @Select("select nextval(#{seqName})")
    Long nextval(@Param("seqName") String seqName);

}
