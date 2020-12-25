package com.wengegroup.util.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Gson
 */
public class PatternMatcherUtils {

    /**
     * ^ 匹配输入字符串开始的位置
     * \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
     * $ 匹配输入字符串结尾的位置
     */
    private static final Pattern HK_PATTERN = Pattern.compile("^(5|6|8|9)\\d{7}$");
    private static final Pattern CHINA_PATTERN = Pattern.compile("^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$");
    private static final Pattern NUM_PATTERN = Pattern.compile("[0-9]+");
    private static final Pattern EM_PATTERN =Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");



    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isEmail(String str) throws PatternSyntaxException {
        Matcher m = EM_PATTERN.matcher(str);
        return m.matches();
    }

    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneLegal(String str) throws PatternSyntaxException {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 145,147,149
     * 15+除4的任意数(不要写^4，这样的话字母也会被认为是正确的)
     * 166
     * 17+3,5,6,7,8
     * 18+任意数
     * 198,199
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        Matcher m = CHINA_PATTERN.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {

        Matcher m = HK_PATTERN.matcher(str);
        return m.matches();
    }

    /**
     * 判断是否是正整数的方法
     */
    public static boolean isNumeric(String string) {
        return NUM_PATTERN.matcher(string).matches();
    }



    //数字
    public static boolean is_number(String number) {
        if (number == null) return false;
        return number.matches("[+-]?[1-9]+[0-9]*(\\.[0-9]+)?");
    }

    //字母
    public static boolean is_alpha(String alpha) {
        if (alpha == null) return false;
        return alpha.matches("[a-zA-Z]+");
    }

    //中文
    public static boolean is_chinese(String chineseContent) {
        if (chineseContent == null) return false;
        return chineseContent.matches("[\u4e00-\u9fa5]");
    }

    /*判断字符串中是否仅包含字母数字和汉字
      *各种字符的unicode编码的范围：
            * 汉字：[0x4e00,0x9fa5]（或十进制[19968,40869]）
            * 数字：[0x30,0x39]（或十进制[48, 57]）
            *小写字母：[0x61,0x7a]（或十进制[97, 122]）
            * 大写字母：[0x41,0x5a]（或十进制[65, 90]）*/
    public static boolean isLetterDigitOrChinese(String str) {
        String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";
        return str.matches(regex);
    }

    //字母和数字
    public static boolean isLetterDigit(String str) {
        String regex = "^[A-Za-z0-9]+$";
        return str.matches(regex);
    }


    //匹配x位以上顺增 　　regex.Pattern = "(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){3,}+\d
    public static boolean lengthNumAsc(String str,int length) {
        String regex = ".*(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){"+length+",}+\\d.*";
        return str.matches(regex);
    }
    //匹配x位以上顺降 　　regex.Pattern = "(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){3,}+\d
    public static boolean lengthNumDesc(String str,int length) {
        String regex = "(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){"+length+",}\\d";
        return str.matches(regex);
    }
    //' 匹配x位顺增或顺降 　　regex.Pattern = "(?:(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){5}|(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){5})\d"
    public static boolean lengthNumAscOrDesc(String str,int length) {
        String regex = ".*(?:(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){"+length+",}|(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){"+length+",})\\d.*";
        return str.matches(regex);
    }
    //匹配x位以上的重复数字 　　([\d])\1{2,}
    public static boolean lengthNumSame(String str,int length) {
        length = length -1;
        String regex = ".*([\\d])\\1{"+length+",}.*";
        return str.matches(regex);
    }
    //匹配x位以上的重复字母 　
    public static boolean lengthEnglishLetterSame(String str,int length) {
        length = length -1;
        String regex = ".*([A-Za-z])\\1{"+length+",}.*";
        return str.matches(regex);
    }


    public static boolean lengthLetterAndNumAsc(String value, int length){
        //是否不合法
        boolean isValidate = false;
        //
        int i = 0;
        //计数器
        int counter = 1;
        //
        for(; i < value.length() -1;) {
            //当前ascii值
            int currentAscii = Integer.valueOf(value.charAt(i));
            //下一个ascii值
            int nextAscii = Integer.valueOf(value.charAt(i + 1));
            //满足区间进行判断
            if( (rangeInDefined(currentAscii, 48, 57) || rangeInDefined(currentAscii, 65, 90) || rangeInDefined(currentAscii, 97, 122))
                    && (rangeInDefined(nextAscii, 48, 57) || rangeInDefined(nextAscii, 65, 90) || rangeInDefined(nextAscii, 97, 122)) ) {
                //计算两数之间差一位则为连续
                if(Math.abs((nextAscii - currentAscii)) == 1){
                    //计数器++
                    counter++;
                }else{
                    //否则计数器重新计数
                    counter = 1;
                }
            }
            //满足连续数字或者字母
            if(counter >= length) return !isValidate;
            //
            i++;
        }

        //
        return isValidate;
    }
    //效果和正序一样，便于阅读
    public static boolean lengthLetterAndNumDesc(String value, int length){
        StringBuffer sb = new StringBuffer(value);
        value = sb.reverse().toString();
        return lengthLetterAndNumAsc(value,length);
    }
    /**
     * 判断一个数字是否在某个区间
     * @param current 当前比对值
     * @param min   最小范围值
     * @param max   最大范围值
     * @return
     */
    public static boolean rangeInDefined(int current, int min, int max) {
        //
        return Math.max(min, current) == Math.min(current, max);
    }

    //匹配日期类型的数字 　　(19|20)[\d]{2}(1[0-2]|0?[1-9])(31|2[0-9]|1[0-9]|0?[0-9])
    public static boolean dateTpy(String str) {
        String regex = "(19|20)[\\d]{2}(1[0-2]|0?[1-9])(31|2[0-9]|1[0-9]|0?[0-9])";
        return str.matches(regex);
    }

    //密码强类型 如果符合则为true　　^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,}$
    public static boolean passwordHigh(String str) {
        String regex = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,}$";
        return str.matches(regex);
    }

    //可以包含字母数字和常用特殊字符，但不能是纯字母纯数字
    public static boolean passwordNormal(String str) {
        String regex = "^(?=.*\\d)(?=.*[A-Za-z])[\\x20-\\x7e]{8,16}$";
        return str.matches(regex);
    }

}
