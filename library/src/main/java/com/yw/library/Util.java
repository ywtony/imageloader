package com.yw.library;

import java.security.MessageDigest;

/**
 * 工具类
 * create by yangwei
 * on 2020-03-01 22:46
 */
public class Util {
    /**
     * 把url转换为一个hash值字符串
     *
     * @param url
     * @return
     */
    public static String hashKeyFromUrl(String url) {
        String cacheKey = null;
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(url.getBytes());
            cacheKey = bytesToHexString(messageDigest.digest());
        } catch (Exception e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    /**
     * 将byte数组转换为16进制字符串
     *
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
