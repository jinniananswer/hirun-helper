package com.microtomato.hirun.framework.config;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.microtomato.hirun.framework.aop.AutoSetMetaObjectAdvice;
import com.microtomato.hirun.framework.aop.PerformanceInterceptor;
import com.microtomato.hirun.framework.transaction.SpringManagedMultiTransactionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Steven
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.microtomato.hirun.**.mapper")
@Slf4j
public class MybatisPlusConfig {

    /**
     * 注入进来的数据源，这里是 Mybatis-Plus 的动态数据源实例：DynamicRoutingDataSource
     */
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    /**
     * XxxMapper.xml 文件所在路径
     */
    @Value("${mybatis-plus.mapper-locations}")
    private String mapperLocations;

    @Autowired(required = false)
    private OptimisticLockerInterceptor optimisticLockerInterceptor;

    @Autowired(required = false)
    private PaginationInterceptor paginationInterceptor;

    @Autowired(required = false)
    private SqlExplainInterceptor sqlExplainInterceptor;

    @Autowired(required = false)
    private PerformanceInterceptor performanceInterceptor;

    @Autowired
    private DynamicDataSourceProperties properties;

    @Autowired
    private AutoSetMetaObjectAdvice autoSetMetaObjectAdvice;

    /**
     * 使用自定义事务管理器, 不用可以注掉 @Bean
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        log.info("创建自定义事务管理器：MultiDataSourceTransactionFactory");
        log.info("mapperPath: {}", mapperLocations);
        log.info("dataSource: {}", dataSource.getClass());

        try {
            MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
            mybatisSqlSessionFactoryBean.setDataSource(dataSource);
            mybatisSqlSessionFactoryBean.setTransactionFactory(new SpringManagedMultiTransactionFactory(properties.getPrimary()));
            mybatisSqlSessionFactoryBean.setTypeAliasesPackage("com.microtomato.hirun.modules");
            mybatisSqlSessionFactoryBean.setPlugins(fetchInterceptors());
            mybatisSqlSessionFactoryBean.setGlobalConfig(fetchGlobalConfig());
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(mapperLocations);
            mybatisSqlSessionFactoryBean.setMapperLocations(resources);
            return mybatisSqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            log.error("mybatis sqlSessionFactoryBean create error", e);
            throw e;
        }
    }

    /**
     * GlobalConfig 配置
     */
    private GlobalConfig fetchGlobalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        if (null != autoSetMetaObjectAdvice) {
            globalConfig.setMetaObjectHandler(autoSetMetaObjectAdvice);
            log.info("字段自动填充处理器: {}", autoSetMetaObjectAdvice.getClass().getName());
        }
        return globalConfig;
    }
    /**
     * 各类拦截器
     */
    private Interceptor[] fetchInterceptors() {
        List<Interceptor> interceptors = new ArrayList<>();
        if (null != optimisticLockerInterceptor) {
            interceptors.add(optimisticLockerInterceptor);
        }

        if (null != paginationInterceptor) {
            interceptors.add(paginationInterceptor);
        }

        if (null != sqlExplainInterceptor) {
            interceptors.add(sqlExplainInterceptor);
        }

        if (null != performanceInterceptor) {
            interceptors.add(performanceInterceptor);
        }
        for (Interceptor interceptor : interceptors) {
            log.info("添加拦截器: {}", interceptor.getClass().getName());
        }
        return interceptors.toArray(new Interceptor[0]);
    }

    /**
     * 乐观锁拦截器
     *
     * @return 返回乐观锁拦截器
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        OptimisticLockerInterceptor optimisticLockerInterceptor = new OptimisticLockerInterceptor();
        return optimisticLockerInterceptor;
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }

    /**
     * 利用攻击 SQL 阻断解析器，来防止 "全表更新"，"全表删除" 等高危操作。
     *
     * @return
     */
    @Bean
    public SqlExplainInterceptor sqlExplainInterceptor() {
        SqlExplainInterceptor sqlExplainInterceptor = new SqlExplainInterceptor();

        List<ISqlParser> sqlParserList = new ArrayList<>();
        sqlParserList.add(new BlockAttackSqlParser());

        sqlExplainInterceptor.setSqlParserList(sqlParserList);
        return sqlExplainInterceptor;
    }

    /**
     * 性能分析拦截器，用于输出每条 SQL 语句及其执行时间，仅开发、测试环境使用，生产环境不推荐。
     * // @Profile({"dev","test"})
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        // SQL 格式化开关
        performanceInterceptor.setFormat(false);
        // SQL 最长执行时间，超过自动停止运行，单位毫秒
        performanceInterceptor.setMaxTime(5000);
        return performanceInterceptor;
    }

}
