package com.microtomato.hirun.framework.mybatis.config;

import com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
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
import com.microtomato.hirun.framework.mybatis.DataSourceWrapper;
import com.microtomato.hirun.framework.mybatis.MyGlobalConfig;
import com.microtomato.hirun.framework.mybatis.MySqlSessionTemplate;
import com.microtomato.hirun.framework.mybatis.aop.RowBoundsLimitInterceptor;
import com.microtomato.hirun.framework.mybatis.threadlocal.ShardTableContextHolder;
import com.microtomato.hirun.framework.util.PackageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.Environment;
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
import org.springframework.core.io.Resource;
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
    private RowBoundsLimitInterceptor rowBoundsLimitInterceptor;

    private Map<String, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<>(16);
    private Map<String, DataSourceWrapper> dataSourceWrapperMap = new HashMap<>(16);

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.atomikos.sys")
    public AtomikosNonXADataSourceBean sysDataSource() {
        AtomikosNonXADataSourceBean bean = DataSourceBuilder.create().type(AtomikosNonXADataSourceBean.class).build();
        bean.setConcurrentConnectionValidation(false);
        return bean;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.atomikos.ins")
    public AtomikosNonXADataSourceBean insDataSource() {
        AtomikosNonXADataSourceBean bean = DataSourceBuilder.create().type(AtomikosNonXADataSourceBean.class).build();
        bean.setConcurrentConnectionValidation(false);
        return bean;
    }

    @Bean(name = "sqlSessionTemplate")
    public MySqlSessionTemplate customSqlSessionTemplate() throws Exception {

        recording(DataSourceKey.SYS, sysDataSource());
        recording(DataSourceKey.INS, insDataSource());

        MySqlSessionTemplate sqlSessionTemplate = new MySqlSessionTemplate(sqlSessionFactoryMap.get(DataSourceKey.SYS));
        sqlSessionTemplate.setTargetSqlSessionFactories(sqlSessionFactoryMap);
        sqlSessionTemplate.setTargetDataSourceWrappers(dataSourceWrapperMap);
        return sqlSessionTemplate;
    }

    /**
     * 记录映射关系，方便从数据源名获取到 SqlSessionFactory 和 AtomikosNonXADataSourceBean，
     * 获取 AtomikosNonXADataSourceBean 目的是做数据源的心跳检查。
     *
     *  key -> SqlSessionFactory
     *  key -> AtomikosNonXADataSourceBean
     *
     * @param key
     * @param atomikosNonXADataSourceBean
     * @throws Exception
     */
    private void recording(String key, AtomikosNonXADataSourceBean atomikosNonXADataSourceBean) throws Exception {
        DataSourceWrapper dataSourceWrapper = new DataSourceWrapper();
        dataSourceWrapper.setAtomikosNonXADataSourceBean(atomikosNonXADataSourceBean);
        dataSourceWrapper.setValidationQuery(atomikosNonXADataSourceBean.getTestQuery());
        sqlSessionFactoryMap.put(key, createSqlSessionFactory(atomikosNonXADataSourceBean, true));
        dataSourceWrapperMap.put(key, dataSourceWrapper);
        /** 设置为 null，避免 testOnBorrow 带来性能开销 */
        atomikosNonXADataSourceBean.setTestQuery(null);
    }

    /**
     * 创建数据源
     *
     * @param dataSource
     * @param reuse      是否复用前面的 Configuration 以加速启动过程。
     * @return
     */
    private SqlSessionFactory createSqlSessionFactory(AtomikosNonXADataSourceBean dataSource, boolean reuse) throws Exception {

        long start = System.currentTimeMillis();
        log.info("数据源初始化开始: {}", dataSource.toString());
        dataSource.init();

        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setVfs(SpringBootVFS.class);

        SqlSessionFactory factory = getFirstOrNull(sqlSessionFactoryMap);
        if (null == factory || false == reuse) {
            ReusableConfiguration configuration = new ReusableConfiguration();
            configuration.setJdbcTypeForNull(JdbcType.NULL);
            configuration.setMapUnderscoreToCamelCase(true);
            configuration.setCacheEnabled(true);
            sqlSessionFactory.setConfiguration(configuration);
            sqlSessionFactory.setDataSource(dataSource);
            sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/**/*.xml"));
            sqlSessionFactory.setPlugins(fetchInterceptors());

        } else {
            ReusableConfiguration rc1 = (ReusableConfiguration) factory.getConfiguration();
            ReusableConfiguration rc2 = (ReusableConfiguration) rc1.clone();
            Environment environment = rc1.getEnvironment();
            Environment env = new Environment(environment.getId(), environment.getTransactionFactory(), dataSource);
            rc2.setEnvironment(env);

            sqlSessionFactory.setConfiguration(rc2);
            sqlSessionFactory.setDataSource(dataSource);
            sqlSessionFactory.setMapperLocations(new Resource[0]);
        }

        // 重写了 GlobalConfig 的 MyGlobalConfig 注入到 sqlSessionFactory 使其生效
        MyGlobalConfig globalConfig = new MyGlobalConfig();
        globalConfig.setBanner(false);
        globalConfig.setMetaObjectHandler(autoSetMetaObjectAdvice);

        sqlSessionFactory.setGlobalConfig(globalConfig);
        sqlSessionFactory.afterPropertiesSet();

        SqlSessionFactory rtn = sqlSessionFactory.getObject();
        log.info("数据源初始化完成: {} ，耗时: {} ms", dataSource.toString(), (System.currentTimeMillis() - start));
        return rtn;
    }

    private SqlSessionFactory getFirstOrNull(Map<String, SqlSessionFactory> map) {
        SqlSessionFactory sqlSessionFactory = null;
        for (Map.Entry<String, SqlSessionFactory> entry : map.entrySet()) {
            sqlSessionFactory = entry.getValue();
            if (null != sqlSessionFactory) {
                break;
            }
        }
        return sqlSessionFactory;
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

        if (null != rowBoundsLimitInterceptor) {
            interceptors.add(rowBoundsLimitInterceptor);
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

//    @Bean
//    public ServletRegistrationBean druidServlet() {
//        log.info("Init Druid Servlet Configuration ");
//        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
//        // IP白名单，不设默认都可以
////        servletRegistrationBean.addInitParameter("allow", "192.168.2.25,127.0.0.1");
//        // IP黑名单(共同存在时，deny优先于allow)
//        servletRegistrationBean.addInitParameter("deny", "192.168.1.100");
//        //控制台管理用户
//        servletRegistrationBean.addInitParameter("loginUsername", "root");
//        servletRegistrationBean.addInitParameter("loginPassword", "root");
//        //是否能够重置数据 禁用HTML页面上的“Reset All”功能
//        servletRegistrationBean.addInitParameter("resetEnable", "false");
//        return servletRegistrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
//        //添加过滤规则
//        filterRegistrationBean.addUrlPatterns("/*");
//        //添加不需要忽略的格式信息
//        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
//        return filterRegistrationBean;
//    }

}