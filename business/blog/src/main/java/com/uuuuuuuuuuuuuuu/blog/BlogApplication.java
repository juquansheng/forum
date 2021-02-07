package com.uuuuuuuuuuuuuuu.blog;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.uuuuuuuuuuuuuuu.oauth2resource.annotation.ForumResourceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@ForumResourceApplication
@SpringBootApplication(exclude = {
        DruidDataSourceAutoConfigure.class,
        ElasticsearchRestClientAutoConfiguration.class})
@EnableScheduling
@EnableEurekaClient
@EnableFeignClients(basePackages={"com.uuuuuuuuuuuuuuu.feign.feign"})
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

}
