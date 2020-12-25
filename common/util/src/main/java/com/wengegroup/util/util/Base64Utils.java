package com.wengegroup.util.util;

import cn.hutool.core.codec.Base64;


public class Base64Utils extends Base64 {

    /**
     * 解密形如 Basic MTgzMjYxODYyMjQ6MDgyMG5jZjkyNzA= 的basic认证信息 字符串[0] username  字符串[1] 密码
     * @param encodedStr 加密的字符串
     * @return 解密的明文
     */
    public static String[] obtainPrinciple(String encodedStr) {
        String temp = encodedStr.replaceFirst("Basic ","");
        String decodeStr = null;
        try {
            decodeStr = decodeStr(temp);
        } catch (Exception e) {
            throw new RuntimeException("swagger解密失败");
        }
        String[] strings = new String[2];
        strings[0] = decodeStr.split(":")[0];
        strings[1] = decodeStr.split(":")[1];
        return strings;
     }

}
