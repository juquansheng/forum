package com.wengegroup.database.component;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Classname BeanConfig
 */
@Configuration
public class BeanConfig {


    /**
     * mybatisplus 乐观锁需要此组件
     * @return 乐观锁拦截器
     */
    @Bean
    @ConditionalOnMissingBean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * mybatis-plus 分页插件
     * @return 分页拦截器
     */
    @Bean
    @ConditionalOnMissingBean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }


    /**
     * 配置线程池
     * @return  executor
     */
    @Bean
    @Primary
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //获取到服务器的cpu核心数
        int processorsNum = Runtime.getRuntime().availableProcessors();
        //核心线程池大小
        executor.setCorePoolSize(5);
        //最大线程数
        executor.setMaxPoolSize(100);
        //队列程度
        executor.setQueueCapacity(1000);
        //队列空闲时间
        executor.setKeepAliveSeconds(1000);
        //线程名称前缀
        executor.setThreadNamePrefix("task-asyn");
        //配置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }



}
