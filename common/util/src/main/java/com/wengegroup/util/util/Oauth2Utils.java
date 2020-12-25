package com.wengegroup.util.util;


public class Oauth2Utils {

    /**
     * 根据authorization头 解析token
     * @param authorization 认证头
     * @return token
     */
    public static String getToken(String authorization) {
        if(!authorization.toLowerCase().startsWith("bearer")) {
            return null;
        }
        return authorization.split(" ")[1];
    }


}
