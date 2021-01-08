package com.uuuuuuuuuuuuuuu.mongodb.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * description: ApplicationMongoDBConfig
 * date: 2020/10/26 17:00
 * author: juquansheng
 * version: 1.0 <br>
 */
@Component
@ConfigurationProperties(prefix = "mongodb")
@Data
@ToString
public class ApplicationMongoDBConfig {
    /**
     * #用户登录db 因为Mongodb允许存在多个同名的用户存在，但同名的用户认证db必须不能相同
     */
    private String authenticationDatabase;
    /**
     * 连接到数据库 操作的数据库
     */
    private String db;
    /**
     *
     */
    private String username;
    /**
     *
     */
    private String password;
    /**
     *
     */
    private List<String> hosts;
    /**
     *
     */
    private List<Integer> ports;
    /**
     * 客户端的连接数
     */
    private Integer connectionsPerHost;
    /**
     * 客户端最小连接数
     */
    private Integer minConnectionsPerHost;
    /**
     *
     */
    private String replicaSet;


    private Integer maxConnectionsPerHost = 100;
    private Integer threadsAllowedToBlockForConnectionMultiplier = 5;
    private Integer serverSelectionTimeout = 30000;
    private Integer maxWaitTime = 120000;
    private Integer maxConnectionIdleTime = 0;
    private Integer maxConnectionLifeTime = 0;
    private Integer connectTimeout = 10000;
    private Integer socketTimeout = 0;
    private Boolean socketKeepAlive = false;
    private Boolean sslEnabled = false;
    private Boolean sslInvalidHostNameAllowed = false;
    private Boolean alwaysUseMBeans = false;
    private Integer heartbeatConnectTimeout = 20000;
    private Integer heartbeatSocketTimeout = 20000;
    private Integer minHeartbeatFrequency = 500;
    private Integer heartbeatFrequency = 10000;
    private Integer localThreshold = 15;
}
