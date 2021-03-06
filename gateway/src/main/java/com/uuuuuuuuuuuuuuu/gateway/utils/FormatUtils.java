package com.uuuuuuuuuuuuuuu.gateway.utils;

/**
 * 格式化工具类
 */
public interface FormatUtils {

    /**
     * 将字符串用中括号括起来
     * @param s 字符串
     * @return [s]
     */
    static String wrapStringWithBracket(String s) {
        return "[" + s + "] ";
    }

}
