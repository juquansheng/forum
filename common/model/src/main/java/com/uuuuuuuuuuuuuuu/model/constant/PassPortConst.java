package com.uuuuuuuuuuuuuuu.model.constant;
/**
 *@describe
 *@author  juquansheng
 *@date  2020/7/4
 */
public class PassPortConst {

    /**
     * 短信/邮箱登录验证码
     */
    public static final String VERIFY_CODE_KEY = "verifyCodeKey";
    /**
     * 短信/邮箱修改密码验证码
     */
    public static final String VERIFY_CODE_MODIFY_PWD = "verifyCodeKeyModifyPWD";
    /**
     * 短信/邮箱修改密码验证码
     */
    public static final String VERIFY_CODE_LOGIN = "verifyCodeKeyLogin";

    public static final String TYPE_REGISTER = "1";
    public static final String TYPE_MODIFY_PWD = "2";
    public static final String TYPE_LOGIN = "3";

    /**
     * 图片验证码
     */
    public static final String CAPTCHA_CODE_KEY = "captchaCodeKey";
    /**
     * 登陆方式
     */
    public static final int LOGIN_PHONE = 1;
    public static final int LOGIN_ACCOUNT = 2;
    public static final int LOGIN_EMAIL = 3;

    public static final String JWT_SECURITY = "groundControlToMajorTom";


}
