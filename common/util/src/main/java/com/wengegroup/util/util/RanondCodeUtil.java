package com.wengegroup.util.util;

import java.time.LocalDateTime;
import java.util.Random;


public class RanondCodeUtil {

    /**
     * 生成数字网格编号
     * @author: vanishor@163.com
     * @date: 2020/8/9 21:47
     * @operation: add
     * @param
     * @return: java.lang.String
     */
    public static String randomGridCode() {
        StringBuilder code = new StringBuilder("G");
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String currTimestamp = String.valueOf(System.currentTimeMillis());
        String subStr = currTimestamp.substring(currTimestamp.length() - 7, currTimestamp.length());
        Random random = new Random();
        int i = random.nextInt(9);
        return code.append(currTimestamp).toString();
    }


    public static void main(String[] args) {
        for (int i = 0; i < 10; i++)
            System.out.println(randomGridCode());
        for (int i = 0; i < 10; i++)
        System.out.println(LocalDateTime.now().getNano());
    }

}
