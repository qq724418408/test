package com.bocop.xfjr.util.enrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1 {

    /**
     * sha1加密
     *
     * @param text 目标文本
     * @return 对目标文本使用sha1算法加密后得到的文本
     */
    public static String sha1(String text) {
        String encryptText = null;
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] digest = sha1.digest(text.getBytes());
            encryptText = bytes2HexString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptText;
    }
 
    /**
     * 字节数组转16进制字符串
     *
     * @param bytes
     * @return
     */
    public static String bytes2HexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
