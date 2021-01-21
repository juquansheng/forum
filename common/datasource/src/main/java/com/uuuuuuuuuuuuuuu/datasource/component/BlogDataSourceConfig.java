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
@MapperScan(basePackages = BlogDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "blogSqlSessionFactory")
public class BlogDataSourceConfig {
    // 指定当前数据源扫描的Mapper(Dao)包

    static final String PACKAGE = "com.uuuuuuuuuuuuuuu.core.mapper.blog";
    static final String MAPPER_LOCATION = "classpath*:mapper/blog/*.xml";

    //  获取配置文件里面当前数据源的的配置信息
    @Value("${spring.datasource.dynamic.datasource.blog.url}")
    private String url;
    @Value("${spring.datasource.dynamic.datasource.blog.username}")
    private String user;
    @Value("${spring.datasource.dynamic.datasource.blog.password}")
    private String password;
    @Value("${spring.datasource.dynamic.datasource.blog.driver-class-name}")
    private String driverClass;


    @Bean(name = "blogDataSource")
    public DataSource blogDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "blogTransactionManager")
    public DataSourceTransactionManager blogTransactionManager() {
        return new DataSourceTransactionManager(blogDataSource());
    }


    @Bean(name = "blogSqlSessionFactory")
    public MybatisSqlSessionFactoryBean blogSqlSessionFactory(@Qualifier("blogDataSource") DataSource blogDataSource)
            throws Exception {
        final MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(blogDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(BlogDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory;
    }

}
