package com.microtomato.hirun.framework.mybatis.config;

import com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.microtomato.hirun.framework.aop.AutoSetMetaObjectAdvice;
import com.microtomato.hirun.framework.aop.PerformanceInterceptor;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.MyGlobalConfig;
import com.microtomato.hirun.framework.mybatis.MySqlSessionTemplate;
import com.microtomato.hirun.framework.util.PackageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Steven
 * @date 2019-12-19
 */
@EnableTransactionManagement
@Slf4j
@Configuration
@MapperScan(basePackages = {"com.microtomato.hirun.**.mapper"}, sqlSessionTemplateRef = "sqlSessionTemplate")
public class MyBatisPlusConfig {

    @Autowired(required = false)
    private OptimisticLockerInterceptor optimisticLockerInterceptor;

    @Autowired(required = false)
    private PaginationInterceptor paginationInterceptor;

    @Autowired(required = false)
    private SqlExplainInterceptor sqlExplainInterceptor;

    @Autowired(required = false)
    private PerformanceInterceptor performanceInterceptor;

    @Autowired
    private AutoSetMetaObjectAdvice autoSetMetaObjectAdvice;

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.atomikos.sys")
    public AtomikosNonXADataSourceBean sysDataSource() {
        return DataSourceBuilder.create().type(AtomikosNonXADataSourceBean.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.atomikos.ins")
    public AtomikosNonXADataSourceBean insDataSource() {
        return DataSourceBuilder.create().type(AtomikosNonXADataSourceBean.class).build();
    }

    @Bean(name = "sqlSessionTemplate")
    public MySqlSessionTemplate customSqlSessionTemplate() throws Exception {
        Map<String, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<String, SqlSessionFactory>() {{
            put(DataSourceKey.SYS, createSqlSessionFactory(sysDataSource()));
            put(DataSourceKey.INS, createSqlSessionFactory(insDataSource()));
        }};
        MySqlSessionTemplate sqlSessionTemplate = new MySqlSessionTemplate(sqlSessionFactoryMap.get(DataSourceKey.SYS));
        sqlSessionTemplate.setTargetSqlSessionFactories(sqlSessionFactoryMap);
        return sqlSessionTemplate;
    }

    /**
     * 创建数据源
     *
     * @param dataSource
     * @return
     */
    private SqlSessionFactory createSqlSessionFactory(AtomikosNonXADataSourceBean dataSource) throws Exception {

        log.info("初始化数据源: {}", dataSource.getUser());
        dataSource.init();

        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/**/*.xml"));
        sqlSessionFactory.setVfs(SpringBootVFS.class);

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(true);

        sqlSessionFactory.setConfiguration(configuration);
        sqlSessionFactory.setPlugins(fetchInterceptors());

        // 重写了 GlobalConfig 的 MyGlobalConfig 注入到 sqlSessionFactory 使其生效
        MyGlobalConfig globalConfig = new MyGlobalConfig();
        globalConfig.setBanner(false);
        globalConfig.setMetaObjectHandler(autoSetMetaObjectAdvice);

        sqlSessionFactory.setGlobalConfig(globalConfig);
        sqlSessionFactory.afterPropertiesSet();

        return sqlSessionFactory.getObject();
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
            log.info("添加拦截器: {}", PackageUtils.compactPackage(interceptor.getClass()));
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
     */
    @Bean
    @Profile({"dev", "test"})
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        // SQL 格式化开关
        performanceInterceptor.setFormat(false);
        // SQL 最长执行时间，超过自动停止运行，单位毫秒
        performanceInterceptor.setMaxTime(5000);
        // SQL 告警时间，超过在控制台以红色打印，单位毫秒
        performanceInterceptor.setWarnTime(80);
        return performanceInterceptor;
    }

}