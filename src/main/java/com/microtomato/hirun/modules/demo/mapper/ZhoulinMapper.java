package com.microtomato.hirun.modules.demo.mapper;

import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.modules.demo.entity.po.Zhoulin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Steven
 * @since 2019-12-19
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface ZhoulinMapper extends BaseMapper<Zhoulin> {

    @DataSource(DataSourceKey.SYS)
    @Insert("insert into sys_steven(name, create_time) values(#{name}, #{createTime})")
    public void insertRandom(@Param("name") String name, @Param("createTime")LocalDateTime createTime);

}
