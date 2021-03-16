package com.dysy.bysj.util;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * @author: Dai Junfeng
 * @create: 2020-09-19
 **/
public class CommonUtils {

    /**
     * 生成指定长度的随机字符串
     * @param length 长度
     * @return
     */
    public static String getRandomSequence(int length) {
        if (length <= 0) {
            return "";
        }
        String sequence = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(sequence.length());
            sb.append(sequence.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 浮点数相加
     * @param d1
     * @param d2
     * @return
     */
    public static String add(double d1, double d2) {
        String st;
        DecimalFormat df = new DecimalFormat("0.00");
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        st = df.format(b1.add(b2).doubleValue());
        return st;
    }

    /**
     * 匹配字符, 可使用通配
     * @param src 原字符串
     * @param pattern 匹配模式
     * @return
     */
    public static boolean isMatch(String src, String pattern) {
        int i = 0;
        int j = 0;
        int starIndex = -1;
        int iIndex = -1;

        while (i < src.length()) {
            if (j < pattern.length() && (pattern.charAt(j) == '?' || pattern.charAt(j) == pattern.charAt(i))) {
                ++i;
                ++j;
            } else if (j < pattern.length() && pattern.charAt(j) == '*') {
                starIndex = j;
                iIndex = i;
                j++;
            } else if (starIndex != -1) {
                j = starIndex + 1;
                i = iIndex+1;
                iIndex++;
            } else {
                return false;
            }
        }

        while (j < pattern.length() && pattern.charAt(j) == '*') {
            ++j;
        }

        return j == pattern.length();
    }

    /**
     * 验证手机号格式
     * @param phone
     * @return
     */
    public static boolean isValidatedPhone(String phone) {
        String pattern = "^1([358]\\d|4[01456789]|6[2567]|7[012345678]|9[012356789])\\d{8}$";
        if (StringUtils.isEmpty(phone) || !phone.matches(pattern)) {
            return false;
        }
        return true;
    }

    /**
     * 验证邮箱格式
     * @param email
     * @return
     */
    public static boolean isValidatedEmail(String email) {
        String pattern = "^\\w+([-\\.]\\w+)*@[A-z\\d]+(\\.[A-z\\d]{2,6}){1,2}$";
        if (StringUtils.isEmpty(email) || !email.matches(pattern)) {
            return false;
        }
        return true;
    }

    /**
     * 生成指定长度的数字序列
     * @return
     */
    public static String generateNumber(int length) {
        if (length <= 0) {
            return "";
        }
        String sequence = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(sequence.length());
            sb.append(sequence.charAt(index));
        }
        return sb.toString();
    }
}
