package com.uuuuuuuuuuuuuuu.search.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("spring.elasticsearch") //描述了当前pojo对应的配置文件前缀
public class ElasticsearchProperties {
    private String host = "127.0.0.1:9200";
    private String username;
    private String password;
    private String scheme;
    private String clusterName;//集群

}
