package com.wengegroup.util.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {
    //token设置过期时间
    public static final long TOKEN_EXPIRE_DATE=10*60*1000L;
    //token秘钥
    public static final String TOKEN_SECRET = "mxd-token";
    //refreshToken设置过期时间
    public static final long REFRESH_TOKEN_EXPIRE_DATE=30*24*60*60*1000L;
    //refreshToken秘钥
    public static final String REFRESH_TOKEN_SECRET = "mxd-refreshToken";

    //token在redis中的key
    public static final String TOKEN_KEY = "mxd_token_key_";

    //refreshToken在redis中的key
    public static final String REFRESH_TOKEN_KEY = "mxd_refreshToken_key_";

    public static String token (String username,String password){

        String token = "";
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis()+TOKEN_EXPIRE_DATE);
            //秘钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            //设置头部信息
            Map<String,Object> header = new HashMap<>();
            header.put("typ","JWT");
            header.put("alg","HS256");
            //携带username，password信息，生成签名
            token = JWT.create()
                    .withHeader(header)
                    .withClaim("username",username)
                    .withClaim("password",password).withExpiresAt(date)
                    .sign(algorithm);
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
        return token;
    }

    //暂时没有用
    public static boolean verify(String token){
        /**
         * @desc   验证token，通过返回true
         * @create 2019/1/18/018 9:39
         * @params [token]需要校验的串
         **/
        try {
            decodeToken(token);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

    public static DecodedJWT decodeToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public static DecodedJWT decodeRefreshToken(String refreshToken) {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(refreshToken);
    }

    public static String obtainAccount(String token) {
        DecodedJWT jwt = decodeToken(token);
        return jwt.getClaim("username").asString();
    }


    public static String refreshToken (String username,String password){

        String token = "";
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis()+REFRESH_TOKEN_EXPIRE_DATE);
            //秘钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(REFRESH_TOKEN_SECRET);
            //设置头部信息
            Map<String,Object> header = new HashMap<>();
            header.put("typ","JWT");
            header.put("alg","HS256");
            //携带username，password信息，生成签名
            token = JWT.create()
                    .withHeader(header)
                    .withClaim("username",username)
                    .withClaim("password",password).withExpiresAt(date)
                    .sign(algorithm);
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
        return token;
    }

    //暂时没有用
    public static boolean verifyRefreshToken(String token){
        /**
         * @desc   验证token，通过返回true
         * @create 2019/1/18/018 9:39
         * @params [token]需要校验的串
         **/
        try {
            Algorithm algorithm = Algorithm.HMAC256(REFRESH_TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

}
