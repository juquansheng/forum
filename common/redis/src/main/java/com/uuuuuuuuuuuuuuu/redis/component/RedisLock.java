package com.uuuuuuuuuuuuuuu.redis.component;


import com.uuuuuuuuuuuuuuu.util.util.SpringUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.Collections;
import java.util.UUID;

/**
 * Redis可重入锁
 */
public class RedisLock {

	private static final StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
    private static final DefaultRedisScript<Long> LOCK_SCRIPT;
    private static final DefaultRedisScript<Object> UNLOCK_SCRIPT;
    static {
        LOCK_SCRIPT = new DefaultRedisScript<>();
        LOCK_SCRIPT.setScriptSource(new ResourceScriptSource(new ClassPathResource("lock.lua")));
        LOCK_SCRIPT.setResultType(Long.class);

        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setScriptSource(new ResourceScriptSource(new ClassPathResource("unlock.lua")));
    }
    /**
     * 获取锁
     * @param lockName 锁名称
     * @param releaseTime 超时时间(单位:秒)
     * @return key 解锁标识
     */
    public static String tryLock(String lockName,String releaseTime) {
    	String key = UUID.randomUUID().toString();
    	
        Long result = redisTemplate.execute(
                LOCK_SCRIPT,
                Collections.singletonList(lockName),
                key + Thread.currentThread().getId(), releaseTime);

        if(result != null && result.intValue() == 1) {
        	return key;
        }else {
        	return null;
        }
    }
    /**
     * 释放锁
     * @param lockName 锁名称
     * @param key 解锁标识
     */
    public static void unlock(String lockName,String key) {
        redisTemplate.execute(
                UNLOCK_SCRIPT,
                Collections.singletonList(lockName),
                key + Thread.currentThread().getId(), null);
    }
}
