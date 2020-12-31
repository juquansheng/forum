package com.uuuuuuuuuuuuuuu.gateway.utils;

import java.util.UUID;

/**
 * ID生成工具类
 */
public class GenerateIdUtils {

    /**
     * 使用UUID生成RequestId
     * @return RequestId
     */
    public static String requestIdWithUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
