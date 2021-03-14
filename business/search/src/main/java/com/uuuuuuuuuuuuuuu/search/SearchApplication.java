package com.uuuuuuuuuuuuuuu.search;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.uuuuuuuuuuuuuuu.mongodb.MongodbConfiguration;
import com.uuuuuuuuuuuuuuu.mongodb.config.MongoConfig;
import com.uuuuuuuuuuuuuuu.oauth2resource.annotation.ForumResourceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@ForumResourceApplication
@SpringBootApplication(exclude= {DruidDataSourceAutoConfigure.class,
        DynamicDataSourceAutoConfiguration.class,
        MongoDataAutoConfiguration.class,
        MongoAutoConfiguration.class,
        MongodbConfiguration.class,
        DataSourceAutoConfiguration.class})
@EnableScheduling
@EnableEurekaClient
@EnableFeignClients(basePackages={"com.uuuuuuuuuuuuuuu.feign.feign"})
public class SearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }

}
