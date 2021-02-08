package com.uuuuuuuuuuuuuuu.mongodb.config;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.internal.MongoClientImpl;
import com.mongodb.connection.Cluster;
import com.mongodb.connection.ConnectionPoolSettings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
    SimpleMongoClientDatabaseFactory simpleMongoClientDatabaseFactory() {//MongoDbFactory mongoDbFactory() {
        //客户端配置（连接数、副本集群验证）
        //log.info("-----------------低版本配置------------------");
        /*MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
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
        com.mongodb.MongoClient mongoClient = new com.mongodb.MongoClient(serverAddresses, mongoCredentialList, mongoClientOptions);
        //return mongoClient.getDatabase(applicationMongoDBConfig.getDb());
        return new SimpleMongoDbFactory(mongoClient, applicationMongoDBConfig.getDb());*/


        log.info("-----------------高版本配置------------------");
         /*String connectionString = "mongodb://user:password@host:port/database";
        if (Objects.isNull(user)) {
        connectionString = "mongodb://host:port/database";
    }
    connectionString = connectionString.replaceAll("user", user)
            .replaceAll("password", password)
                .replaceAll("host", host)
                .replaceAll("port", port)
                .replaceAll("database", database);*/


        // MongoDB地址列表
        List<ServerAddress> serverAddressList = new ArrayList<>();
        for (String host : applicationMongoDBConfig.getHosts()) {
            Integer index = applicationMongoDBConfig.getHosts().indexOf(host);
            Integer port = applicationMongoDBConfig.getPorts().get(index);

            ServerAddress serverAddress = new ServerAddress(host, port);
            serverAddressList.add(serverAddress);
        }
        log.info("serverAddresses:" + serverAddressList.toString());

        MongoClientSettings settings = null;
        ConnectionPoolSettings poolSetting =ConnectionPoolSettings.builder().
                maxWaitTime(applicationMongoDBConfig.getMaxWaitTime(), TimeUnit.MILLISECONDS).build();
        if(StringUtils.isNotBlank(applicationMongoDBConfig.getUsername())) {
            MongoCredential credential = MongoCredential.createScramSha1Credential(applicationMongoDBConfig.getUsername(),
                    applicationMongoDBConfig.getDb(), applicationMongoDBConfig.getPassword().toCharArray());
            settings = MongoClientSettings.builder()
                    .credential(credential)
                    .applyToConnectionPoolSettings(builder->builder.applySettings(poolSetting))
                    .applyToClusterSettings(builder -> builder.hosts(serverAddressList)).build();
        }else {
            settings = MongoClientSettings.builder().applyToConnectionPoolSettings(builder->builder.applySettings(poolSetting))
                    .applyToClusterSettings(builder -> builder.hosts(serverAddressList)).build();
        }
        //创建客户端和Factory
        MongoClient mongoClient = MongoClients.create(settings);
        return new SimpleMongoClientDatabaseFactory(mongoClient, applicationMongoDBConfig.getDb());
    }




}
