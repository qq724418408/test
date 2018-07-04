package com.bocop.xfjr.util.enrypt;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * Copy Right Information : Forms Syntron <br>
 * Project : s <br>
 * JDK version used : JDK1.7 <br>
 * Description : RSA Encrypt/Decrypt<br>
 * Comments Name : RSASecurity <br>
 * Author : LL<br>
 * Date : 2017年2月7日 <br>
 * Version : 1.0 <br>
 */
public class RSASecurity {
	/** 指定加密算法为RSA */
	private final static String ALGORITHM = "RSA";
	private final static String sRSATransformation = "RSA/ECB/PKCS1Padding";
	/** 密钥长度，用来初始化 */
	private static final int KEYSIZE = 1024;
	/** 公钥 **/
	private Key publicKey;
	/** 私钥 **/
	private Key privateKey;

	/**
	 * 初始化密钥对路径及其值
	 * 
	 * @throws Exception
	 */
	public RSASecurity() throws GeneralSecurityException {
		generateKeyPair();
	}

	public String getPublicKeyString() {
		return new String(Base64.encodeBase64(publicKey.getEncoded()));
	}

	public String getPrivateKeyString() {
		return new String(Base64.encodeBase64(privateKey.getEncoded()));
	}

	public Key getPublicKey() {
		return publicKey;
	}

	public Key getPrivateKey() {
		return privateKey;
	}

	/**
	 * 生成/读取密钥
	 * 
	 * @throws Exception
	 */
	private void generateKeyPair() throws GeneralSecurityException {
		// 获得密匙对对象
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		// 根据密钥长度初始化KeyPairGenerator对象
		keyPairGenerator.initialize(KEYSIZE);
		// 生成密匙对
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		publicKey = keyPair.getPublic();
		privateKey = keyPair.getPrivate();
	}

	private static Key getPublicKey(String key) throws GeneralSecurityException {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(key.getBytes()));
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		return keyFactory.generatePublic(keySpec);
	}

	public static Key getPrivateKey(String key) throws GeneralSecurityException {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(key.getBytes()));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(keySpec);
	}

	/**
	 * 加密
	 * 
	 * @param source
	 *            原文
	 * @return 密文
	 * @throws Exception
	 */
	public String encrypt(String source) throws GeneralSecurityException {
		// 得到Cipher对象来实现对源数据的RSA加密
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		// 加密操作
		byte[] encrypted = cipher.doFinal(source.getBytes());

		return new String(Base64.encodeBase64(encrypted));
	}

	/**
	 * 加密
	 * 
	 * @param source
	 *            原文
	 * @param Key
	 *            公钥
	 * @return 密文
	 * @throws Exception
	 */
	public static String encrypt(String source, String publicKey) throws GeneralSecurityException {
		// 得到Cipher对象来实现对源数据的RSA加密
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
		// 加密操作
		byte[] encrypted = cipher.doFinal(source.getBytes());
		return new String(Base64.encodeBase64(encrypted));
	}

	/**
	 * 解密
	 * 
	 * @param encrypted
	 *            密文
	 * @return 原文
	 * @throws Exception
	 */
	public String decrypt(String encrypted) throws GeneralSecurityException {
		/** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		// 解密
		byte[] source = cipher.doFinal(Base64.decodeBase64(encrypted.getBytes()));
		return new String(source);
	}

	/**
	 * 解密
	 * 
	 * @param cryptograph
	 *            密文
	 * @param Key
	 *            私钥
	 * @return 原文
	 * @throws Exception
	 */
	public static String decrypt(String encrypted, String privateKey) throws GeneralSecurityException {
		/** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
		// 解密
		byte[] source = cipher.doFinal(Base64.decodeBase64(encrypted.getBytes()));
		return new String(source);
	}

	/**
	 * 公钥解密
	 * 
	 * @param cryptograph
	 *            密文
	 * @param Key
	 *            私钥
	 * @return 原文
	 */
	public static String decryptByPublic(String encrypted, String key) {
		/** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(sRSATransformation);
			cipher.init(Cipher.DECRYPT_MODE, getPublicKey(key));
			// 解密
			byte[] source = cipher.doFinal(Base64.decodeBase64(encrypted.getBytes()));
			return new String(source);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

}