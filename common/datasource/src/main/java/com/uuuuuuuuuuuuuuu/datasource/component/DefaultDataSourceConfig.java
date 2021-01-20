package com.uuuuuuuuuuuuuuu.datasource.component;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * description: DefaultDataSourceConfig
 * date: 2020/12/1 17:01
 * author: juquansheng
 * version: 1.0 <br>
 */
@Configuration
@MapperScan(basePackages = DefaultDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "defaultSqlSessionFactory")
public class DefaultDataSourceConfig {
    // 指定当前数据源扫描的Mapper(Dao)包

    static final String PACKAGE = "com.uuuuuuuuuuuuuuu.core.mapper.forum";
    static final String MAPPER_LOCATION = "classpath*:mapper/blog/*.xml";

    //  获取配置文件里面当前数据源的的配置信息
    @Value("${spring.datasource.dynamic.datasource.master.url}")
    private String url;
    @Value("${spring.datasource.dynamic.datasource.master.username}")
    private String user;
    @Value("${spring.datasource.dynamic.datasource.master.password}")
    private String password;
    @Value("${spring.datasource.dynamic.datasource.master.driver-class-name}")
    private String driverClass;


    @Bean(name = "defaultDataSource")
    @Primary
    public DataSource defaultDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "defaultTransactionManager")
    @Primary
    public DataSourceTransactionManager defaultTransactionManager() {
        return new DataSourceTransactionManager(defaultDataSource());
    }


    @Bean(name = "defaultSqlSessionFactory")
    @Primary
    public MybatisSqlSessionFactoryBean defaultSqlSessionFactory(@Qualifier("defaultDataSource") DataSource defaultDataSource)
            throws Exception {
        final MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(defaultDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(DefaultDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory;
    }

}
