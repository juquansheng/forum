package com.uuuuuuuuuuuuuuu.mongodb.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * description: MongoConfig
 * date: 2020/10/27 15:37
 * author: juquansheng
 * version: 1.0 <br>
 */
@Slf4j
@Component
@Configuration
public class MongoConfig {
    // 注入配置实体
    @Autowired
    private ApplicationMongoDBConfig applicationMongoDBConfigSource;

    /**
     * 因为Mongodb允许存在多个同名的用户存在，但同名的用户认证db必须不能相同
     */
    public static ApplicationMongoDBConfig applicationMongoDBConfig;


    @PostConstruct
    public void init(){
        applicationMongoDBConfig = applicationMongoDBConfigSource;
        log.info("MongoConfig类初始参数---------------------------applicationMongoDBConfig: {}",
                applicationMongoDBConfig.toString());
    }

    // 覆盖默认的MongoDbFactory
    @Bean
    MongoDatabase mongoDbFactory() {//MongoDbFactory mongoDbFactory() {
        //客户端配置（连接数、副本集群验证）
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.connectionsPerHost(applicationMongoDBConfig.getConnectionsPerHost());
        builder.minConnectionsPerHost(applicationMongoDBConfig.getMinConnectionsPerHost());
        if (StringUtils.isNotBlank(applicationMongoDBConfig.getReplicaSet())) {
            builder.requiredReplicaSetName(applicationMongoDBConfig.getReplicaSet());
            log.info("replicaSet:"+applicationMongoDBConfig.getReplicaSet());
        }
        builder.threadsAllowedToBlockForConnectionMultiplier(
                applicationMongoDBConfig.getThreadsAllowedToBlockForConnectionMultiplier());
        builder.serverSelectionTimeout(applicationMongoDBConfig.getServerSelectionTimeout());
        builder.maxWaitTime(applicationMongoDBConfig.getMaxWaitTime());
        builder.maxConnectionIdleTime(applicationMongoDBConfig.getMaxConnectionIdleTime());
        builder.maxConnectionLifeTime(applicationMongoDBConfig.getMaxConnectionLifeTime());
        builder.connectTimeout(applicationMongoDBConfig.getConnectTimeout());
        builder.socketTimeout(applicationMongoDBConfig.getSocketTimeout());
        builder.sslEnabled(applicationMongoDBConfig.getSslEnabled());
        builder.sslInvalidHostNameAllowed(applicationMongoDBConfig.getSslInvalidHostNameAllowed());
        builder.alwaysUseMBeans(applicationMongoDBConfig.getAlwaysUseMBeans());
        builder.heartbeatFrequency(applicationMongoDBConfig.getHeartbeatFrequency());
        builder.minHeartbeatFrequency(applicationMongoDBConfig.getMinHeartbeatFrequency());
        builder.heartbeatConnectTimeout(applicationMongoDBConfig.getHeartbeatConnectTimeout());
        builder.heartbeatSocketTimeout(applicationMongoDBConfig.getHeartbeatSocketTimeout());
        builder.localThreshold(applicationMongoDBConfig.getLocalThreshold());

        MongoClientOptions mongoClientOptions = builder.build();

        // MongoDB地址列表
        List<ServerAddress> serverAddresses = new ArrayList<>();
        for (String host : applicationMongoDBConfig.getHosts()) {
            Integer index = applicationMongoDBConfig.getHosts().indexOf(host);
            Integer port = applicationMongoDBConfig.getPorts().get(index);

            ServerAddress serverAddress = new ServerAddress(host, port);
            serverAddresses.add(serverAddress);
        }
        log.info("serverAddresses:" + serverAddresses.toString());

        // 连接认证
        List<MongoCredential> mongoCredentialList = new ArrayList<>();
        if (applicationMongoDBConfig.getUsername() != null) {
            mongoCredentialList.add(MongoCredential.createScramSha1Credential(
                    applicationMongoDBConfig.getUsername(),
                    applicationMongoDBConfig.getAuthenticationDatabase() != null ? applicationMongoDBConfig.getAuthenticationDatabase() : applicationMongoDBConfig.getDb(),
                    applicationMongoDBConfig.getPassword().toCharArray()));
        }
        log.info("mongoCredentialList:" + mongoCredentialList.toString());

        //创建客户端和Factory
        MongoClient mongoClient = new MongoClient(serverAddresses, mongoCredentialList, mongoClientOptions);
        MongoDatabase database = mongoClient.getDatabase(applicationMongoDBConfig.getDb());
        return mongoClient.getDatabase(applicationMongoDBConfig.getDb());
        //return new SimpleMongoDbFactory(mongoClient, applicationMongoDBConfig.getDb());
    }
}
