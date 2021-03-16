package com.dysy.bysj.util;

import com.dysy.bysj.common.Constants;
import com.dysy.bysj.common.exception.UnknownAlgorithmException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author: Dai Junfeng
 * @create: 2020-09-19
 **/
@Component
public class EncrypUtils {

    // 默认散列次数
    private static final int DEFAULT_ITERATIONS = 1;
    // 默认加密算法
    private static final String DEFAULT_ALGORITHM_NAME = "MD5";
    // 加密算法 MD2\MD5\SHA1\SHA256\SHA384\SHA512
    private String algorithmName;

    public String encryp(String algorithmName, Object source) {
        return this.encryp(algorithmName, source, null, 1);
    }

    public String encryp(String algorithmName, Object source, Object salt) {
        return this.encryp(algorithmName, source, salt, 1);
    }

    public String encryp(Object source, Object salt) {
        return this.encryp(null, source, salt, 1);
    }

    public String encryp(Object source, Object salt, int hashIterations) {
        return this.encryp(null, source, salt, hashIterations);
    }

    /**
     * 加密
     * @param algorithmName 加密算法名称
     * @param source 原文
     * @param salt 盐
     * @param hashIterations 散列次数
     * @return
     */
    public String encryp(String algorithmName, Object source, Object salt, int hashIterations) {
        if (StringUtils.isEmpty(algorithmName)) {
            this.algorithmName = DEFAULT_ALGORITHM_NAME;
        } else {
            this.algorithmName = algorithmName;
        }
        hashIterations = Math.max(DEFAULT_ITERATIONS, hashIterations);
        byte[] saltBytes = null;
        if (salt != null) {
            saltBytes = String.valueOf(salt).getBytes();
        }
        byte[] byteSource = String.valueOf(source).getBytes();
        byte[] hashed = this.hash(byteSource, saltBytes, hashIterations);
        return byte2hex(hashed);
    }

    /**
     * 哈希散列
     * @param source 原文
     * @param salt 盐
     * @param hashIterations 散列次数
     * @return
     */
    protected byte[] hash(byte[] source, byte[] salt, int hashIterations) {
        MessageDigest digest = this.getDigest(this.algorithmName);
        if (salt != null) {
            digest.reset();
            digest.update(salt);
        }

        byte[] hashed = digest.digest(source);
        int iterations = hashIterations - 1;

        for(int i = 0; i < iterations; ++i) {
            digest.reset();
            hashed = digest.digest(hashed);
        }

        return hashed;
    }

    /**
     * 得到 MessageDigest 实例
     * @param algorithmName 加密算法名称
     * @return
     */
    protected MessageDigest getDigest(String algorithmName) {
        try {
            return MessageDigest.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException e) {
            String msg = "No native '" + algorithmName + "' MessageDigest instance available on the current JVM.";
            throw new UnknownAlgorithmException(msg);
        }

    }

    protected String byte2hex(byte[] bytes) {
        StringBuffer hs = new StringBuffer(bytes.length);
        String stmp = "";
        for (int i = 0; i < bytes.length; i++) {
            stmp = (Integer.toHexString(bytes[i] & 0XFF));
            if (stmp.length() == 1)
                hs = hs.append("0").append(stmp);
            else
                hs = hs.append(stmp);
        }
        return hs.toString();
    }

    public String base64Encode(String source) {
        if (StringUtils.isEmpty(source)) {
            return "";
        }
        String encode = "";
        try {
            encode = Base64.getEncoder().encodeToString(source.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encode;

    }

    public String base64Decode(String encode) {
        if (StringUtils.isEmpty(encode)) {
            return "";
        }
        String decode = "";
        try {
            decode = new String(Base64.getDecoder().decode(encode), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decode;
    }

    public String getSalt() {
        return CommonUtils.getRandomSequence(Constants.RANDOM_SALT_LENGTH);
    }
}
