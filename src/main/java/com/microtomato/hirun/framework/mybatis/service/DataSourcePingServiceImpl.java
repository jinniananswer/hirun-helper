package com.microtomato.hirun.framework.mybatis.service;

import com.baomidou.mybatisplus.core.toolkit.IOUtils;
import com.microtomato.hirun.framework.mybatis.MyGlobalConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * 数据库连接池保活
 *
 * @author Steven
 * @date 2020-01-01
 */
@Slf4j
@Service
public class DataSourcePingServiceImpl implements ApplicationContextAware {

    static {
        Checker checker = new Checker();
        checker.setDaemon(true);
        checker.start();
    }

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    private static class Checker extends Thread {

        @Override
        public void run() {

            while (true) {
                try {
                    Thread.sleep(180000);
                    MyGlobalConfig myGlobalConfig = ctx.getBean(MyGlobalConfig.class);
                    Map<String, SqlSessionFactory> targetSqlSessionFactories = myGlobalConfig.getSqlSessionTemplate().getTargetSqlSessionFactories();
                    for (String dataSource : targetSqlSessionFactories.keySet()) {
                        SqlSessionFactory sqlSessionFactory = targetSqlSessionFactories.get(dataSource);
                        SqlSession sqlSession = sqlSessionFactory.openSession();
                        keepAlive(sqlSession);
                        log.debug("Connection keepalive: {}", dataSource);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void keepAlive(SqlSession sqlSession) {
            Connection conn = sqlSession.getConnection();
            Statement statement = null;
            try {
                statement = conn.createStatement();
                statement.executeQuery("SELECT 1");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(statement);
                IOUtils.closeQuietly(conn);
                IOUtils.closeQuietly(sqlSession);
            }
        }

    }

}
