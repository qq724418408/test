package com.bocop.xfjr.util.enrypt;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import android.util.Base64;

/**
 * 使用说明：
 * 一、如果需要生成公钥和私钥
 * RSA mRSA = RSA.getInstance();
 * 查看生成的公钥私钥：
 * String publicKey = mRSA.getPublicKey();
 * String privateKey = mRSA.getPrivateKey();
 * 
 * 二、对数据加解密
 * 1、私钥加密
 * String encryptStr = RSA.encryptByPrivateKey(text, privateKey);
 * 2、公钥加密
 * String encryptStr = RSA.encryptByPublicKey(text, publicKey);
 * 3、私钥解密(只能解公钥加密的密文)
 * String decrypttStr = RSA.decryptByPrivateKey(text, privateKey);
 * 4、公钥解密(只能解私钥加密的密文)
 * String decrypttStr = RSA.decryptByPublicKey(text, publicKey);
 * 
 * 问题：
 * 公钥和私钥应该是每64个字符换行才行（在网站在线加解密的时候验证过）（待进一步验证）
 */
public class RSA {

    private static final String sRSATransformation = "RSA/ECB/PKCS1Padding";
    private static final RSA ourInstance = new RSA();
    private String publicKey;
    private String privateKey;
    
    public static RSA getInstance() {
        return ourInstance;
    }

    private RSA() {
    	generateRSAKeyPair();
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

	public static String br64(String s) {
		StringBuffer s1 = new StringBuffer(s);
		int index;
		for (index = 64; index < s1.length(); index += 64) {
			s1.insert(index, '\n');
		}
		return s1.toString();
	}
    
    /**
     * 私钥加密
     *
     * @param text       待加密数据
     * @param privateKey 密钥
     * @return 加密数据
     */
	public static String encryptByPrivateKey(String text, String privateKey) {
    	String encryptStr = null;
    	try {
    		byte[] privateKeyByte = privateKey.getBytes();
    		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyByte);
    		KeyFactory kf = KeyFactory.getInstance("RSA");
    		PrivateKey keyPrivate = kf.generatePrivate(keySpec);
    		Cipher cipher = Cipher.getInstance(sRSATransformation);
    		cipher.init(Cipher.ENCRYPT_MODE, keyPrivate);
    		encryptStr = new String(base64Encrypt(cipher.doFinal(text.getBytes())));
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return encryptStr;
    }
	
    /**
     * 公钥解密
     *
     * @param text      待解密数据
     * @param publicKey 公钥
     * @return 解密数据
     */
    public static String decryptByPublicKey(String text, String publicKey) {
        String decryptStr = null;
        try {
            byte[] publicKeyByte = publicKey.getBytes();
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyByte);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey keyPublic = kf.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance(sRSATransformation);
            cipher.init(Cipher.DECRYPT_MODE, keyPublic);
            decryptStr = new String(cipher.doFinal(base64Decrypt(text.getBytes())));
            return decryptStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptStr;
    }

    /**
     * 公钥对字符串进行加密
     *
     * @param text 原文
     * @return 加密数据
     */
	public static String encryptByPublicKey(String text, String publicKey) {
        String encryptStr = null;
        try {
        	byte[] publicKeyByte = publicKey.getBytes();
            byte[] decode = Base64.decode(publicKeyByte, Base64.DEFAULT);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decode);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey keyPublic = kf.generatePublic(keySpec);
            Cipher cp = Cipher.getInstance(sRSATransformation);
            cp.init(Cipher.ENCRYPT_MODE, keyPublic);
            byte[] doFinal = cp.doFinal(text.getBytes());
            encryptStr = new String(base64Encrypt(doFinal));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptStr;
    }

    /**
     * 私钥解密
     *
     * @param text       待解密数据
     * @param privateKey 私钥
     * @return 解密数据
     */
    public static String decryptByPrivateKey(String text, String privateKey) {
        String decryptStr = null;
        try {
        	byte[] privateKeyByte = privateKey.getBytes();
            byte[] decode = Base64.decode(privateKeyByte, Base64.DEFAULT);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decode);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey keyPrivate = kf.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance(sRSATransformation);
            cipher.init(Cipher.DECRYPT_MODE, keyPrivate);
            decryptStr = new String(cipher.doFinal(base64Decrypt(text.getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptStr;
    }

    //##############################################################################################

    /**
     * base64加密
     *
     * @param text 加密内容
     * @return 加密后的内容
     */
    private static byte[] base64Encrypt(byte[] text) {
        return Base64.encode(text, Base64.DEFAULT);
    }

    /**
     * base64解密
     *
     * @param text 解密内容
     * @return 解密后得到的内容
     */
    private static byte[] base64Decrypt(byte[] text) {
        return Base64.decode(text, Base64.DEFAULT);
    }

    /**
     * 随机生成RSA密钥对
     * 使用：
     * 公钥
     * RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
     * 私钥
     * RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
     *
     * @return 随机生成的密钥对
     */
    private KeyPair generateRSAKeyPair() {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            keyPair = kpg.genKeyPair();
            RSAPublicKey publicKeyRSA = (RSAPublicKey) keyPair.getPublic();
            byte[] encoded = publicKeyRSA.getEncoded();
            byte[] pukb = Base64.encode(encoded, Base64.DEFAULT);
            publicKey = new String(pukb);
            RSAPrivateKey privateKeyRSA = (RSAPrivateKey) keyPair.getPrivate();
            byte[] prkb = Base64.encode(privateKeyRSA.getEncoded(), Base64.DEFAULT);
            privateKey = new String(prkb);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyPair;
    }

}
