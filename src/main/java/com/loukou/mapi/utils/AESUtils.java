package com.loukou.mapi.utils;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;

public class AESUtils {
	private static final Logger logger = Logger.getLogger(AESUtils.class);
	
	public static final String KEY_ALGORITHM_AES = "AES";
	public static final String CIPHER_ALGORITHM_AES = "AES/ECB/PKCS5Padding";
		
	public static Key genKey() throws NoSuchAlgorithmException {
		KeyGenerator kgen;
		kgen = KeyGenerator.getInstance(KEY_ALGORITHM_AES);
		kgen.init(128, new SecureRandom());
		return kgen.generateKey();
	}
	
	public static byte[] decrypt(byte[] encryptedData, byte[] key) {

		try {
			SecretKeySpec sks = new SecretKeySpec(key, KEY_ALGORITHM_AES);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_AES);// 创建密码器  
	        cipher.init(Cipher.DECRYPT_MODE, sks);// 初始化  
	        return cipher.doFinal(encryptedData);
		}
		catch (Exception e) {
			logger.warn("AESUtils.decrypt FAILED.", e);
			throw new RuntimeException(e);
		}
	}
	
	public static String decrypt(String encryptedData, String key) {
		return new String(decrypt(DatatypeConverter.parseBase64Binary(encryptedData), DatatypeConverter.parseBase64Binary(key)));
	}
	
	public static byte[] encrypt(byte[] data, byte[] key) {
		try {
			SecretKeySpec sks = new SecretKeySpec(key, KEY_ALGORITHM_AES);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_AES);// 创建密码器  
            cipher.init(Cipher.ENCRYPT_MODE, sks);// 初始化  
            return cipher.doFinal(data);
		} 
		catch (Exception e) {
			logger.warn("AESUtils.encrypt FAILED.", e);
			throw new RuntimeException(e);
		}
	}
	
	public static String encrypt(String data, String key) {
		return DatatypeConverter.printBase64Binary(encrypt(data.getBytes(), DatatypeConverter.parseBase64Binary(key)));
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		// 该类的使用方法
		// 生成密钥
//		Key key = AESUtils.genKey();
//		String base64Key = DatatypeConverter.printBase64Binary(key.getEncoded());
//		System.out.println(base64Key);
				
		String base64Key = "70QuJbTMlI8KB4rJ7bmLQA==";
		
		String text = "plaintext";
		// 加密
		// 密文转换成字符串需要用base64编码
		String encodeString = encrypt(text, base64Key); 
		
		// 解密
		// 密文字符串转换成字节码需要用base64解码
		String textMock = decrypt(encodeString, base64Key);
		
		if (textMock.equals(text)) {
			System.out.println("AES 加解密成功！");
		}
		else {
			System.out.println("AES 加解密失败！");
		}
	}
	
}
