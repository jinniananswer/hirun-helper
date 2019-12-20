package com.microtomato.hirun.framework.mybatis;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 *
 * @author Steven
 */
@Slf4j
@Component
public class MyGlobalConfig extends GlobalConfig {

    private static MySqlSessionTemplate mySqlSessionTemplate;

    @Autowired
    private MySqlSessionTemplate sqlSessionTemplate;

    @PostConstruct
    public void init() {
        MyGlobalConfig.mySqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public SqlSessionFactory getSqlSessionFactory() {
        return mySqlSessionTemplate.getSqlSessionFactory();
    }

}
