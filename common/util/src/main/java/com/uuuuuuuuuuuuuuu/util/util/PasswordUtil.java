package com.uuuuuuuuuuuuuuu.util.util;




public class PasswordUtil {
    /**
     * 安全密码，作为盐值用于用户密码的加密
     */
    public static final String SECURITY_KEY = "yuuki";

    /**
     * AES 加密
     *
     * @param password 未加密的密码
     * @param salt     盐值，默认使用用户名就可
     * @return
     * @throws Exception
     */
    public static String encrypt(String password, String salt) throws Exception {
        return AesSecretUtil.encryptForString(MD5Util.MD5Encode(salt + SECURITY_KEY,"utf-8"), password);
    }

    /**
     * AES 解密
     *
     * @param encryptPassword 加密后的密码
     * @param salt            盐值，默认使用用户名就可
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptPassword, String salt) throws Exception {
        return AesSecretUtil.decryptForString(MD5Util.MD5Encode(salt + SECURITY_KEY,"utf-8"), encryptPassword);
    }
}
