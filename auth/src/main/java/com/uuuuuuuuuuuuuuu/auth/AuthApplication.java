package com.uuuuuuuuuuuuuuu.auth;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.uuuuuuuuuuuuuuu.mongodb.MongodbConfiguration;
import com.uuuuuuuuuuuuuuu.mongodb.config.MongoConfig;
import org.elasticsearch.cluster.coordination.ElasticsearchNodeCommand;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@EnableFeignClients("com.uuuuuuuuuuuuuuu.feign.feign")
@SpringBootApplication(exclude = {
        DruidDataSourceAutoConfigure.class,
        MongoDataAutoConfiguration.class,
        MongoAutoConfiguration.class,
        ElasticsearchRestClientAutoConfiguration.class})
@MapperScan(basePackages = {"com.uuuuuuuuuuuuuuu.core.mapper.*"})
@ComponentScan(basePackages = {
        "com.uuuuuuuuuuuuuuu.core.service",
        "com.uuuuuuuuuuuuuuu.core.service"})
@EnableAuthorizationServer
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
