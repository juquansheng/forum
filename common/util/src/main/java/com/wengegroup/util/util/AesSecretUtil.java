package com.wengegroup.util.util;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * AES加密工具类
 */
public class AesSecretUtil {

    private final static Logger logger = LoggerFactory.getLogger(AesSecretUtil.class);

    /** 秘钥的大小 */
    private static final int KEYSIZE = 128;

    public static final String DATAKEY = "SECRET_KEY";

    private static final String KEY_ALGORITHM = "AES";

    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * @Description: AES加密
     * @param data
     *            - 待加密内容
     * @param key
     *            - 加密秘钥
     */
    public static byte[] encrypt(String data, String key) {
        byte[] result = null;
        if (StringUtils.isNotBlank(data)) {
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                // 选择一种固定算法，为了避免不同java实现的不同算法，生成不同的密钥，而导致解密失败
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                random.setSeed(key.getBytes());
                keyGenerator.init(KEYSIZE, random);
                SecretKey secretKey = keyGenerator.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
                // 创建密码器
                Cipher cipher = Cipher.getInstance("AES");
                byte[] byteContent = data.getBytes("utf-8");
                // 初始化
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                result = cipher.doFinal(byteContent);
            } catch (Exception e) {
                logger.error("func[encrypt] Exception [{} - {}] stackTrace[{}] params[{}]", new Object[] {e.getCause(),
                    e.getMessage(), Arrays.deepToString(e.getStackTrace()), data + "---" + key});
            }
        }
        return result;
    }

    /**
     * AES加密
     *
     * @param passwd  加密的密钥
     * @param content 需要加密的字符串
     * @return 返回Base64转码后的加密数据
     * @throws Exception
     */
    public static String encryptForString(String passwd, String content) throws Exception {
        // 创建密码器
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

        byte[] byteContent = content.getBytes("utf-8");

        // 初始化为加密模式的密码器
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(passwd));

        // 加密
        byte[] result = cipher.doFinal(byteContent);

        //通过Base64转码返回
        return Base64.encodeBase64String(result);
    }
    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String password) throws NoSuchAlgorithmException {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        // javax.crypto.BadPaddingException: Given final block not properly padded解决方案
        // https://www.cnblogs.com/zempty/p/4318902.html - 用此法解决的
        // https://www.cnblogs.com/digdeep/p/5580244.html - 留作参考吧
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        //AES 要求密钥长度为 128
        kg.init(128, random);

        //生成一个密钥
        SecretKey secretKey = kg.generateKey();
        // 转换为AES专用密钥
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
    }
    /**
     * @Description: AES加密，返回String
     * @param data
     *            - 待加密内容
     */
    public static String encryptToStr(String data) {
        return StringUtils.isNotBlank(data) ? parseByte2HexStr(encrypt(data, DATAKEY)) : null;
    }

    /**
     * @Description: AES解密
     * @param data
     *            - 待解密字节数组
     * @param key
     *            - 秘钥
     */
    public static byte[] decrypt(byte[] data, String key) {
        byte[] result = null;
        if (ArrayUtils.isNotEmpty(data)) {
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                // 选择一种固定算法，为了避免不同java实现的不同算法，生成不同的密钥，而导致解密失败
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                random.setSeed(key.getBytes());
                keyGenerator.init(KEYSIZE, random);
                SecretKey secretKey = keyGenerator.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
                // 创建密码器
                Cipher cipher = Cipher.getInstance("AES");
                // 初始化
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
                result = cipher.doFinal(data);
            } catch (Exception e) {
                logger.error("func[decrypt] Exception [{} - {}] stackTrace[{}] params[{}]", new Object[] {e.getCause(),
                    e.getMessage(), Arrays.deepToString(e.getStackTrace()), data + "---" + key});
            }
        }
        return result;
    }

    /**
     * AES解密
     *
     * @param passwd    加密的密钥
     * @param encrypted 已加密的密文
     * @return 返回解密后的数据
     * @throws Exception
     */
    public static String decryptForString(String passwd, String encrypted) throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

        //使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(passwd));

        //执行操作
        byte[] result = cipher.doFinal(Base64.decodeBase64(encrypted));

        return new String(result, "utf-8");
    }


    /**
     * @Description: AES解密，返回String
     * @param enCryptdata
     *            - 待解密字节数组
     */
    public static String decryptToStr(String enCryptdata) {
        return StringUtils.isNotBlank(enCryptdata) ? new String(decrypt(parseHexStr2Byte(enCryptdata), DATAKEY)) : null;
    }

    /**
     * @Description: 将二进制转换成16进制
     * @param buf
     *            - 二进制数组
     */
    public static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * @Description: 将16进制转换为二进制
     * @param hexStr
     *            - 16进制字符串
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        byte[] result = null;
        if (hexStr.length() >= 1) {
            result = new byte[hexStr.length() / 2];
            for (int i = 0; i < hexStr.length() / 2; i++) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte)(high * 16 + low);
            }
        }
        return result;
    }

}