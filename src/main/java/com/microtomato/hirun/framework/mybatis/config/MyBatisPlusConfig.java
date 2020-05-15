package com.microtomato.hirun.framework.mybatis.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.microtomato.hirun.framework.aop.AutoSetMetaObjectAdvice;
import com.microtomato.hirun.framework.interceptor.SqlPerformanceInterceptor;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.aop.LimitInterceptor;
import com.microtomato.hirun.framework.mybatis.MyGlobalConfig;
import com.microtomato.hirun.framework.mybatis.MySqlSessionTemplate;
import com.microtomato.hirun.framework.mybatis.threadlocal.ShardTableContextHolder;
import com.microtomato.hirun.framework.util.PackageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.*;

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
    private SqlPerformanceInterceptor sqlPerformanceInterceptor;

    @Autowired
    private AutoSetMetaObjectAdvice autoSetMetaObjectAdvice;

    @Autowired
    private LimitInterceptor limitInterceptor;

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.atomikos.sys")
    public AtomikosDataSourceBean sysDataSource() {
        return DataSourceBuilder.create().type(AtomikosDataSourceBean.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.atomikos.ins")
    public AtomikosDataSourceBean insDataSource() {
        return DataSourceBuilder.create().type(AtomikosDataSourceBean.class).build();
    }

    @Bean(name = "sqlSessionTemplate")
    public MySqlSessionTemplate customSqlSessionTemplate() throws Exception {
        Map<String, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<String, SqlSessionFactory>(16) {{
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
    private SqlSessionFactory createSqlSessionFactory(AtomikosDataSourceBean dataSource) throws Exception {

//        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSource);
//        // 指定 xml 文件路径
//        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/**/*.xml"));
//        return factoryBean.getObject();


        log.info("初始化数据源: {}", dataSource.toString());
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

        if (null != sqlPerformanceInterceptor) {
            interceptors.add(sqlPerformanceInterceptor);
        }

        if (null != limitInterceptor) {
            interceptors.add(limitInterceptor);
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
        paginationInterceptor.setSqlParserList(Collections.singletonList(shardTableParser()));
        return paginationInterceptor;
    }

    /**
     * 分表解析器
     *
     * @return
     */
    private ISqlParser shardTableParser() {

        log.info("配置分表解析器...");

        DynamicTableNameParser dynamicTableNameParser = new DynamicTableNameParser();
        Map<String, ITableNameHandler> tableNameHandlerMap = new HashMap<>(16);
        dynamicTableNameParser.setTableNameHandlerMap(tableNameHandlerMap);

        // 指定要分表的表名
        tableNameHandlerMap.put("sys_steven", (metaObject, sql, tableName) -> StringUtils.joinWith("_", tableName, ShardTableContextHolder.peek()));

        tableNameHandlerMap.keySet().forEach(tableName -> log.info("  分表: {} ", tableName));
        return dynamicTableNameParser;
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
    public SqlPerformanceInterceptor performanceInterceptor() {
        SqlPerformanceInterceptor sqlPerformanceInterceptor = new SqlPerformanceInterceptor();
        // SQL 格式化开关
        sqlPerformanceInterceptor.setFormat(false);
        // SQL 最长执行时间，超过自动停止运行，单位毫秒
        sqlPerformanceInterceptor.setMaxTime(5000);
        // SQL 告警时间，超过在控制台以红色打印，单位毫秒
        sqlPerformanceInterceptor.setWarnTime(80);
        return sqlPerformanceInterceptor;
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        log.info("Init Druid Servlet Configuration ");
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // IP白名单，不设默认都可以
//        servletRegistrationBean.addInitParameter("allow", "192.168.2.25,127.0.0.1");
        // IP黑名单(共同存在时，deny优先于allow)
        servletRegistrationBean.addInitParameter("deny", "192.168.1.100");
        //控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername", "root");
        servletRegistrationBean.addInitParameter("loginPassword", "root");
        //是否能够重置数据 禁用HTML页面上的“Reset All”功能
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        //添加过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        //添加不需要忽略的格式信息
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

}